package org.datavaultplatform.common.model.dao;


import java.util.List;

public interface BaseDAO<T> extends AbstractDAO<T,String> {

    void save(T item);

    void update(T item);

    List<T> list();

    T findById(String Id);

}
