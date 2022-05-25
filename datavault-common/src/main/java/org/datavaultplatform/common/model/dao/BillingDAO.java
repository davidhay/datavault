package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.BillingInfo;
import org.datavaultplatform.common.model.dao.custom.BillingCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface BillingDAO extends BaseDAO<BillingInfo>, BillingCustomDAO {
}