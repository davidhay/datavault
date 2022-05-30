package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import org.datavaultplatform.common.model.PendingDataCreator;
import org.springframework.transaction.annotation.Transactional;


public interface PendingDataCreatorCustomDAO extends BaseCustomDAO {
    void save(List<PendingDataCreator> pendingDataCreators);
}
