package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import org.datavaultplatform.common.model.PendingDataCreator;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PendingDataCreatorCustomDAO {
    void save(List<PendingDataCreator> pendingDataCreators);
}
