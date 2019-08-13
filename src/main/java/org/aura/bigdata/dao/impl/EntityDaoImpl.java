package org.aura.bigdata.dao.impl;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.aura.bigdata.dao.EntityDao;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.aura.bigdata.dao.base.HbaseDaoBase;
import org.aura.bigdata.model.Entity;
import org.aura.bigdata.model.UserPay;
import org.aura.bigdata.utils.QueryCondition;

import java.util.*;

public class EntityDaoImpl<T> extends HbaseDaoBase implements EntityDao {

    private T t ;

    /**
     * insert an row data
     * @param entity
     * @throws Exception
     */
    @Override
    public void saveOrUpdate(Entity entity) throws Exception{
        Table table = getTable(entity.getName());
        Map<String, Map<String,String>> cell = entity.getCell();
        Set<Map.Entry<String, Map<String, String>>> entries = cell.entrySet();
        for(Map.Entry<String, Map<String, String>> entry: entries){
            Put put = new Put(Bytes.toBytes(entity.getRow()));
            String family = entry.getKey();
            Set<Map.Entry<String, String>> qualifierValues = entry.getValue().entrySet();
            for(Map.Entry<String, String>  qualifierValue: qualifierValues){
                put.addColumn(Bytes.toBytes(family),Bytes.toBytes(qualifierValue.getKey()),Bytes.toBytes(qualifierValue.getValue()));
            }
            table.put(put);
        }
        table.close();
        closeConn();
    }

    /**
     * find an row data
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public Entity findEntity(Entity entity) throws Exception {
        Entity<T> resultEntity = null ;
        Table table = getTable(entity.getName());
        Get get = new Get(Bytes.toBytes(entity.getRow()));
        Result result = table.get(get);
        resultEntity = parseEntity(result);
        table.close();
        closeConn();
        return resultEntity;
    }

    /**
     * scan some row data by specific condition
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public List<Entity> findEntities(Entity entity) throws Exception {
        List<Entity> entities = null ;
        Table table= getTable(entity.getName());
        Scan scan = new Scan();
        QueryCondition queryCondition = entity.getQueryCondition();
        String[] time_stamp_range = queryCondition.getTime_stamp_range();

        List<Filter> filters = new ArrayList<>();

        Filter filter1 = new SingleColumnValueFilter(Bytes.toBytes(queryCondition.getColumn_family()),Bytes.toBytes("time_stamp"), CompareFilter.CompareOp.GREATER,Bytes.toBytes(time_stamp_range[0]));
        Filter filter2 = new SingleColumnValueFilter(Bytes.toBytes(queryCondition.getColumn_family()),Bytes.toBytes("time_stamp"), CompareFilter.CompareOp.LESS,Bytes.toBytes(time_stamp_range[1]));
        Filter filter3 = new SingleColumnValueFilter(Bytes.toBytes(queryCondition.getColumn_family()),Bytes.toBytes("user_id"), CompareFilter.CompareOp.EQUAL,Bytes.toBytes(queryCondition.getUser_id()));
        Filter filter4 = new SingleColumnValueFilter(Bytes.toBytes(queryCondition.getColumn_family()),Bytes.toBytes("shop_id"), CompareFilter.CompareOp.EQUAL,Bytes.toBytes("90"));

        filters.add(filter1) ;
        filters.add(filter2) ;
        filters.add(filter3) ;

        FilterList filterList = new FilterList(filters);
        scan.setFilter(filterList);

        ResultScanner resultScanner = table.getScanner(scan);
        entities = parseEntityList(resultScanner);
        return entities;
    }

    /**
     * find all row data
     * @return
     * @throws Exception
     */
    @Override
    public List<Entity> findAll(Entity entity) throws Exception{
        List<Entity> entities = null ;
        Table table= getTable(entity.getName());
        Scan scan = new Scan();
        ResultScanner resultScanner = table.getScanner(scan);
        entities = parseEntityList(resultScanner);
        return entities ;
    }

    /**
     * count all rows
     * @param entity
     * @return
     * @throws Exception
     */
    public Long count(Entity entity) throws Exception{
        long rows = 0;
        Table table = getTable(entity.getName());
        Scan scan = new Scan();
        ResultScanner resultScanner = table.getScanner(scan);
        Iterator<Result> iterator = resultScanner.iterator();
        while(iterator.hasNext()){
            iterator.next();
            rows++ ;
        }
        table.close();
        closeConn();
        return rows ;
    }

    /**
     * delete an row data
     * @param entity
     * @throws Exception
     */
    @Override
    public void deleteEntity(Entity entity) throws Exception{
        Table table= getTable(entity.getName());
        Delete delete = new Delete(Bytes.toBytes(entity.getRow()));
        table.delete(delete);
        table.close();
        closeConn();
    }

    /**
     * parse a result to entity
     * @param result
     * @return
     * @throws Exception
     */
    private Entity parseEntity(Result result) throws Exception{
        List<Cell> cells = result.listCells();
        Map<String,Map<String,String>> entityCells = new HashMap<String,Map<String,String>>();
        Entity<T> resultEntity = new Entity<T>(); ;
        String row = "" ;
        for (Cell cell :cells){
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            row = Bytes.toString(CellUtil.cloneRow(cell));
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value =  Bytes.toString(CellUtil.cloneValue(cell));

            if(entityCells.containsKey(family)){
                entityCells.get(family).put(qualifier,value);
            }else{
                HashMap<String,String> qualifierValueMap = new HashMap<String,String>();
                qualifierValueMap.put(qualifier,value);
                entityCells.put(family,qualifierValueMap);
            }
        }
        resultEntity.setRow(row);
        resultEntity.setCell(entityCells);
        return resultEntity ;
    }

    /**
     * parse a resultScanner
     * @param resultScanner
     * @return
     * @throws Exception
     */
    private List<Entity> parseEntityList(ResultScanner resultScanner) throws Exception{
        List<Entity> entities = null ;
        Iterator<Result> iterator = resultScanner.iterator();
        if(iterator.hasNext()){
            entities = new ArrayList<>();
        }
        while(iterator.hasNext()){
            Result result = iterator.next();
            Entity tmpEntity = parseEntity(result);
            entities.add(tmpEntity);
        }
        return entities ;
    }

    /**
     * create a table
     * @param tableName
     * @param familyNames
     * @throws Exception
     */
    public void  createTable(String tableName,String[] familyNames) throws Exception{
        super.createTable(tableName,familyNames);
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
