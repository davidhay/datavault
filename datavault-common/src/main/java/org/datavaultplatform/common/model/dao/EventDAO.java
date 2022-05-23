package org.datavaultplatform.common.model.dao;

import java.util.List;
import org.datavaultplatform.common.event.Event;
import org.datavaultplatform.common.model.Vault;

public interface EventDAO extends BaseDAO<Event> {

    List<Event> list(String sort);

    List<Event> findVaultEvents(Vault vault);
    
    int count();
    
}
