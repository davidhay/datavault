package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import org.datavaultplatform.common.model.DataCreator;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DataCreatorCustomDAO {
    void save(List<DataCreator> dataCreators);
}
