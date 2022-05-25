package org.datavaultplatform.common.model.dao;

import java.util.List;
import org.datavaultplatform.common.model.DepositChunk;
import org.datavaultplatform.common.model.dao.custom.DepositCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface DepositChunkDAO extends BaseDAO<DepositChunk>, DepositCustomDAO {

  @Override
  default List<DepositChunk> list() {
    return list("id");
  }

}
