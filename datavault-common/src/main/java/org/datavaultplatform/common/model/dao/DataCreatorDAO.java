package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.DataCreator;

import java.util.List;

public interface DataCreatorDAO extends BaseDAO<DataCreator> {
    void save(List<DataCreator> dataCreators);

    void delete(String id);
}
