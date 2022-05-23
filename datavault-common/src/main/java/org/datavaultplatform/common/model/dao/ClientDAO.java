package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.Client;

/**
 * User: Robin Taylor
 * Date: 10/02/2016
 * Time: 10:59
 */

public interface ClientDAO extends BaseDAO<Client> {

    Client findByApiKey(String Apikey);

    int count();
}
