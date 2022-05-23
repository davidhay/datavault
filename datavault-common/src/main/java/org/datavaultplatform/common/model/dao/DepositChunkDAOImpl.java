package org.datavaultplatform.common.model.dao;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.DepositChunk;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DepositChunkDAOImpl implements DepositChunkDAO {

    private final SessionFactory sessionFactory;

    public DepositChunkDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(DepositChunk chunk) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(chunk);
    }
    
    @Override
    public void update(DepositChunk chunk) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(chunk);
    }

    @Override
    public List<DepositChunk> list() {
        return list("id");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DepositChunk> list(String sort) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DepositChunk.class);
        // See if there is a valid sort option
        if ("id".equals(sort)) {
            criteria.addOrder(Order.asc("id"));
        } else if ("status".equals(sort)) {
            criteria.addOrder(Order.asc("status"));
        } else {
            criteria.addOrder(Order.asc("creationTime"));
        }

        List<DepositChunk> chunks = criteria.list();
        return chunks;
    }
    
    @Override
    public DepositChunk findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DepositChunk.class);
        criteria.add(Restrictions.eq("id",Id));
        DepositChunk chunk = (DepositChunk)criteria.uniqueResult();
        return chunk;
    }
}
