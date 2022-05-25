package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.AuditChunkStatus;
import org.datavaultplatform.common.model.dao.custom.AuditChunkStatusCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface AuditChunkStatusDAO extends BaseDAO<AuditChunkStatus>, AuditChunkStatusCustomDAO {
}
