package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.Group;
import org.datavaultplatform.common.model.dao.custom.GroupCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface GroupDAO extends BaseDAO<Group>, GroupCustomDAO {
}
