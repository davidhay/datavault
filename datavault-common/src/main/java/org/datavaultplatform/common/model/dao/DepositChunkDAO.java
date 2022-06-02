package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.DepositChunk;
import org.datavaultplatform.common.model.dao.custom.DepositCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DepositChunkDAO extends BaseDAO<DepositChunk>, DepositCustomDAO {
}
