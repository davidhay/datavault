package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.ArchiveStore;
 
public interface ArchiveStoreDAO extends BaseDAO<ArchiveStore> {

    ArchiveStore findForRetrieval();

    void deleteById(String Id);
    
}
