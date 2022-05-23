package org.datavaultplatform.common.model.dao;

import java.util.List;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.Vault;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.event.Event;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class EventDAOImpl extends BaseDaoImpl<Event,String> implements EventDAO {

    public EventDAOImpl(EntityManager em) {
        super(Event.class, em);
    }

    @Override
    public Event save(Event event) {
        Session session = this.getCurrentSession();
        session.persist(event);
        return event;
    }

    @Override
    public Event update(Event item) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> list() {        
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Event.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Event> events = criteria.list();
        return events;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Event> list(String sort) {
        Session session = this.getCurrentSession();
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
    public Optional<Event> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Event.class);
        criteria.add(Restrictions.eq("id",Id));
        Event event = (Event)criteria.uniqueResult();
        return Optional.ofNullable(event);
    }

    @Override
    public List<Event> findVaultEvents(Vault vault) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Event.class);
        criteria.add(Restrictions.eq("vault",vault));
        criteria.addOrder(Order.asc("timestamp"));
        List<Event> events = criteria.list();
        return events;
    }
    
    @Override
    public long count() {
        Session session = this.getCurrentSession();
        return count(session, Event.class);
    }
}
