package org.datavaultplatform.common.model.dao;

import java.util.List;
import org.datavaultplatform.common.model.BillingInfo;
import org.datavaultplatform.common.model.dao.custom.BillingCustomDAO;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BillingDAO extends BaseDAO<BillingInfo>, BillingCustomDAO {

  /**
   * TODO - this looks like a bug
   */
  @Override
  default List<BillingInfo> list() {
    return findAll(Sort.by(Order.asc("creationTime")));
  }
}