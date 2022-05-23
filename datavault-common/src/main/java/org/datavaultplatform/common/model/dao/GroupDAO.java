package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.Group;

import java.util.List;

public interface GroupDAO extends BaseDAO<Group> {

    void delete(Group group);

    List<Group> list(String userId);

    int count(String userId);
}
