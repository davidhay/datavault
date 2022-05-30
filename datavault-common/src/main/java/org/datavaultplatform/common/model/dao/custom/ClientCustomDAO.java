package org.datavaultplatform.common.model.dao.custom;

import org.datavaultplatform.common.model.Client;
import org.springframework.transaction.annotation.Transactional;


public interface ClientCustomDAO extends BaseCustomDAO {

    Client findByApiKey(String Apikey);
}
