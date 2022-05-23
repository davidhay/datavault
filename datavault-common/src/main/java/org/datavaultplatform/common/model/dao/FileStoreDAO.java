package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.FileStore;
 
public interface FileStoreDAO extends BaseDAO<FileStore> {

    void deleteById(String Id);
}
