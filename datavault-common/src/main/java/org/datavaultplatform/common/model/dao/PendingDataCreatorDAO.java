package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.PendingDataCreator;

import java.util.List;

public interface PendingDataCreatorDAO extends BaseDAO<PendingDataCreator> {
    void save(List<PendingDataCreator> pendingDataCreators);

    void delete(String id);
}
