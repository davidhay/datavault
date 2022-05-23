package org.datavaultplatform.common.model.dao;


import java.util.List;

public interface AbstractDAO<T, ID> {

    void save(T item);

    void update(T item);

    List<T> list();

    T findById(ID Id);

}
