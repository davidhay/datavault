package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.Retrieve;

import java.util.List;

public interface RetrieveDAO extends BaseDAO<Retrieve> {
    
    List<Retrieve> list(String userId);

    int count(String userId);

    int inProgressCount(String userId);

    List<Retrieve> inProgress();

    int queueCount(String userId);
}
