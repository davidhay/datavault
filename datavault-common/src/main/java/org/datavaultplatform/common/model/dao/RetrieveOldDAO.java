package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.Retrieve;
import org.datavaultplatform.common.model.dao.custom.RetrieveOLDCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RetrieveOldDAO extends BaseDAO<Retrieve>, RetrieveOLDCustomDAO {
}
