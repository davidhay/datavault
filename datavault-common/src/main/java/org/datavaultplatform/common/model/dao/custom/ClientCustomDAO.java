package org.datavaultplatform.common.model.dao.custom;

import org.datavaultplatform.common.model.Client;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ClientCustomDAO {

    Client findByApiKey(String Apikey);
}
