package org.datavaultplatform.common.model.dao;

import java.util.List;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * User: Robin Taylor
 * Date: 10/02/2016
 * Time: 11:01
 */

@Repository
@Transactional
public class ClientDAOImpl extends BaseDaoImpl implements ClientDAO {

    private static final Logger logger = LoggerFactory.getLogger(ClientDAOImpl.class);

    private final SessionFactory sessionFactory;

    public ClientDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(Client client) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(client);
    }

    @Override
    public void update(Client client) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(client);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Client.class);
        List<Client> clients = criteria.list();
        return clients;
    }

    @Override
    public Client findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Client.class);
        criteria.add(Restrictions.eq("id",Id));
        Client client = (Client)criteria.uniqueResult();
        return client;
    }

    @Override
    public Client findByApiKey(String apiKey) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Client.class);
        criteria.add(Restrictions.eq("apiKey", apiKey));
        Client client = (Client)criteria.uniqueResult();
        return client;
    }

    @Override
    public int count() {
        Session session = this.sessionFactory.getCurrentSession();
        return count(session, Client.class);
    }
}
