package org.datavaultplatform.common.model.dao;

import java.util.List;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.Client;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Robin Taylor
 * Date: 10/02/2016
 * Time: 11:01
 */

@Repository
@Transactional
public class ClientDAOImpl extends BaseDaoImpl<Client,String> implements ClientDAO {

    private static final Logger logger = LoggerFactory.getLogger(ClientDAOImpl.class);

    public ClientDAOImpl(EntityManager em) {
        super(Client.class, em);
    }


    @Override
    public Client save(Client client) {
        Session session = this.getCurrentSession();
        session.persist(client);
        return client;
    }

    @Override
    public Client update(Client client) {
        Session session = this.getCurrentSession();
        session.update(client);
        return client;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Client.class);
        List<Client> clients = criteria.list();
        return clients;
    }

    @Override
    public Optional<Client> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Client.class);
        criteria.add(Restrictions.eq("id",Id));
        Client client = (Client)criteria.uniqueResult();
        return Optional.ofNullable(client);
    }

    @Override
    public Client findByApiKey(String apiKey) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Client.class);
        criteria.add(Restrictions.eq("apiKey", apiKey));
        Client client = (Client)criteria.uniqueResult();
        return client;
    }

    @Override
    public long count() {
        Session session = this.getCurrentSession();
        return count(session, Client.class);
    }
}
