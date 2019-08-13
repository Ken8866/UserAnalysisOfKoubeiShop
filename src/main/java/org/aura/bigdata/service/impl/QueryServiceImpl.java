package org.aura.bigdata.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hbase.security.User;
import org.aura.bigdata.AppConstants;
import org.aura.bigdata.AppUtils;
import org.aura.bigdata.dao.EntityDao;
import org.aura.bigdata.dao.impl.EntityDaoImpl;
import org.aura.bigdata.model.Entity;
import org.aura.bigdata.model.ShopInfo;
import org.aura.bigdata.model.UserPay;
import org.aura.bigdata.model.UserView;
import org.aura.bigdata.service.QueryService;
import org.aura.bigdata.utils.QueryCondition;

import java.util.List;
import java.util.Map;

/**
 * Hbase Service Implement
 * @param <T>
 */
public class QueryServiceImpl<T> implements QueryService<T> {

    private T t ;
    private String reqString ;
    private String resString ;
    private String svcType ;
    private EntityDao entityDao = new EntityDaoImpl<T>();
    private Entity<T> entity = new Entity<T>() ;

    @Override
    public String execute(String reqString,String svcType) throws Exception {
        this.svcType = svcType ;
        this.reqString = reqString ;
        switch(svcType){
            case AppConstants.SVC_TYPE_COUNT : count() ; break;
            case AppConstants.SVC_TYPE_DELETE : delete() ; break;
            case AppConstants.SVC_TYPE_CREATE_TABLE : createTable() ; break;
            case AppConstants.SVC_TYPE_SAVE_OR_UPDATE : saveOrUpdate() ; break;
            case AppConstants.SVC_TYPE_FIND : find() ; break;
            case AppConstants.SVC_TYPE_FIND_ALL : findAll() ; break;
            case AppConstants.SVC_TYPE_FIND_ENTITIES : findEntities(); break ;
        }
        return resString;
    }

    @Override
    public T getT() {
        return t;
    }

    @Override
    public void setT(T t) {
        this.t = t;
    }

    private void count() throws Exception {
        Entity<T> entity = new Entity();
        entity.setT(t);
        entity.setName(t.getClass().getSimpleName().toLowerCase());
        Long count = entityDao.count(entity);
        this.resString = String.valueOf(count) ;
    }

    private void delete() throws Exception {

    }

    private void createTable() throws Exception {
        Entity entity = JSON.parseObject(this.reqString,Entity.class);
        entity.setName(t.getClass().getSimpleName().toLowerCase());
        entityDao.createTable(t.getClass().getSimpleName().toLowerCase(),entity.getColumnFamily());
        this.resString = "create table successfully ！！！" ;
    }

    private void saveOrUpdate() throws Exception {
        List<Entity> entities = JSON.parseArray(this.reqString,Entity.class);
        for(Entity entity:entities){
            entity.setName(t.getClass().getSimpleName().toLowerCase());
            entityDao.saveOrUpdate(entity);
        }
        this.resString = "save or update sucessful" ;
    }

    private void find() throws Exception {
        Entity entity = JSON.parseObject(this.reqString,Entity.class);
        entity.setT(t);
        entity.setName(t.getClass().getSimpleName().toLowerCase());
        entity.setRow(entity.getRow());

        Entity resEntity = entityDao.findEntity(entity);
        Map<String,Map<String,String>> cell = resEntity.getCell();
        String shopId = cell.get("colFmly").get("shop_id");
        ShopInfo shopInfo = AppUtils.getShopInfo(shopId);
        resEntity.setShopInfo(shopInfo);

        this.resString = JSON.toJSONString(resEntity);

    }

    private void findEntities() throws Exception {
        Entity entity = JSON.parseObject(this.reqString,Entity.class);
        entity.setT(t);
        entity.setName(t.getClass().getSimpleName().toLowerCase());

        List<Entity> resEntities = entityDao.findEntities(entity);
        if(resEntities == null){
            this.resString = "no data can be find" ;
        }else{
            for(Entity obj:resEntities){
                Map<String,Map<String,String>> cell = obj.getCell();
                String shopId = cell.get("colFmly").get("shop_id");
                ShopInfo shopInfo = AppUtils.getShopInfo(shopId);
                obj.setShopInfo(shopInfo);

            }
            this.resString = JSON.toJSONString(resEntities);
        }

    }

    private void findAll() throws Exception {

    }


}
