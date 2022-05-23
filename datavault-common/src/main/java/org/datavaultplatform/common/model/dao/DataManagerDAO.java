package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.DataManager;
 
public interface DataManagerDAO extends BaseDAO<DataManager> {

    void deleteById(String Id);
}
