package org.datavaultplatform.common.model.dao;

import java.util.List;
import org.datavaultplatform.common.model.AuditChunkStatus;
import org.datavaultplatform.common.model.dao.custom.AuditChunkStatusCustomDAO;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AuditChunkStatusDAO extends BaseDAO<AuditChunkStatus>, AuditChunkStatusCustomDAO {
  @Override
  default List<AuditChunkStatus> list() {
    return findAll(Sort.by(Order.asc("timestamp")));
  }

}
