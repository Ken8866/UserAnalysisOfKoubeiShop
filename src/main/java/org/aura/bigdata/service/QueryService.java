package org.aura.bigdata.service;

/**
 * Hbase Service Interface
 */
public interface QueryService<T> {

    /**
     * Service Method
     * @param json
     * @return
     * @throws Exception
     */
    public String execute(String json,String svcType) throws Exception ;

    /**
     * generic Method get
     * @return
     */
    public T getT() ;

    /**
     * generic Method set
     * @param t
     */
    public void setT(T t) ;

}
