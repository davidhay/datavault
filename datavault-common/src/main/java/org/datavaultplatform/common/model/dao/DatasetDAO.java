package org.datavaultplatform.common.model.dao;

import java.util.List;
import org.datavaultplatform.common.model.BillingInfo;
import org.datavaultplatform.common.model.Dataset;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DatasetDAO extends BaseDAO<Dataset> {

  /**
   * TODO - looks like bug
   */
  @Override
  default List<Dataset> list() {
    return findAll(Sort.by(Order.asc("sort")));
  }
}
