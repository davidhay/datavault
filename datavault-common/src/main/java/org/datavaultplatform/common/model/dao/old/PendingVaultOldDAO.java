package org.datavaultplatform.common.model.dao.old;

import java.util.List;
import org.datavaultplatform.common.model.PendingVault;
import org.datavaultplatform.common.model.dao.BaseDAO;
import org.datavaultplatform.common.model.dao.custom.PendingOLDVaultCustomDAO;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PendingVaultOldDAO extends BaseDAO<PendingVault>, PendingOLDVaultCustomDAO {

  @Override
  default List<PendingVault> list() {
    return findAll(Sort.by(Order.asc("creationTime")));
  }
}
