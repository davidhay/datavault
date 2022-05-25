package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.Deposit;
import org.datavaultplatform.common.model.dao.custom.DepositCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface DepositDAO extends BaseDAO<Deposit>, DepositCustomDAO {
}
