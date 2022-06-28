package org.datavaultplatform.common.model.dao.old;
import java.util.List;
import org.datavaultplatform.common.model.Group;
import org.datavaultplatform.common.model.dao.BaseDAO;
import org.datavaultplatform.common.model.dao.custom.GroupOLDCustomDAO;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface GroupOldDAO extends BaseDAO<Group>, GroupOLDCustomDAO {

  @Override
  default List<Group> list() {
    return findAll(Sort.by(Order.asc("name")));
  }
}
