package org.datavaultplatform.common.model.dao.custom;

import org.datavaultplatform.common.model.ArchiveStore;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArchiveStoreCustomDAO {

    ArchiveStore findForRetrieval();
}
