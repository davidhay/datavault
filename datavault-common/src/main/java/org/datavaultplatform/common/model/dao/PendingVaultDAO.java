package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.PendingVault;
import org.datavaultplatform.common.model.dao.custom.PendingVaultCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PendingVaultDAO extends BaseDAO<PendingVault>, PendingVaultCustomDAO {
}
