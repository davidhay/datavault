package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import org.datavaultplatform.common.model.Group;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GroupCustomDAO {

    List<Group> list(String userId);

    int count(String userId);
}
