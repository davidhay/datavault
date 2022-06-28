package org.datavaultplatform.common.model.dao.old;

import org.datavaultplatform.common.model.Deposit;
import org.datavaultplatform.common.model.dao.BaseDAO;
import org.datavaultplatform.common.model.dao.custom.DepositOLDCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DepositOldDAO extends BaseDAO<Deposit>, DepositOLDCustomDAO {
}
