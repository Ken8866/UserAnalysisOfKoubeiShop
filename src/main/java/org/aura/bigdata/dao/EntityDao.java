package org.aura.bigdata.dao;

import org.aura.bigdata.model.Entity;

import java.io.Serializable;
import java.util.List;

public interface EntityDao {

    public void createTable(String tableName,String[] familyNames) throws Exception ;
    public void saveOrUpdate(Entity entity) throws Exception ;
    public Entity findEntity(Entity entity) throws Exception ;
    public List<Entity> findEntities(Entity entity) throws Exception ;
    public List<Entity> findAll(Entity entity) throws Exception ;
    public Long count(Entity entity) throws Exception ;
    public void deleteEntity(Entity entity) throws Exception ;
    public List<Entity> findEntitiesByRowRange(Entity entity) throws Exception;

}
