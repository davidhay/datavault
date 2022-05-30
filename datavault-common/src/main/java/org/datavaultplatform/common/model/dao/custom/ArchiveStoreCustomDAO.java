package org.datavaultplatform.common.model.dao.custom;

import org.datavaultplatform.common.model.ArchiveStore;
import org.springframework.transaction.annotation.Transactional;


public interface ArchiveStoreCustomDAO extends BaseCustomDAO {

    ArchiveStore findForRetrieval();
}
