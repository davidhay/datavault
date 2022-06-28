package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import org.datavaultplatform.common.model.Group;


public interface GroupOLDCustomDAO extends BaseCustomDAO {

    List<Group> list(String userId);

    int count(String userId);
}
