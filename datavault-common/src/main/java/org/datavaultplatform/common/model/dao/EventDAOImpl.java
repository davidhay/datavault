package org.datavaultplatform.common.model.dao;

import java.util.List;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.Vault;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.event.Event;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class EventDAOImpl extends BaseDaoImpl implements EventDAO {
    
    private final SessionFactory sessionFactory;

    public EventDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Event event) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(event);
    }

    @Override
    public void update(Event item) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> list() {        
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Event.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Event> events = criteria.list();
        return events;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Event> list(String sort) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Event.class);
        // See if there is a valid sort option
        if ("id".equals(sort)) {
            criteria.addOrder(Order.asc("id"));
        } else {
            criteria.addOrder(Order.asc("timestamp"));
        }

        List<Event> events = criteria.list();
        return events;
    }
    
    @Override
    public Event findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Event.class);
        criteria.add(Restrictions.eq("id",Id));
        Event event = (Event)criteria.uniqueResult();
        return event;
    }

    @Override
    public List<Event> findVaultEvents(Vault vault) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Event.class);
        criteria.add(Restrictions.eq("vault",vault));
        criteria.addOrder(Order.asc("timestamp"));
        List<Event> events = criteria.list();
        return events;
    }
    
    @Override
    public int count() {
        Session session = this.sessionFactory.getCurrentSession();
        return count(session, Event.class);
    }
}
