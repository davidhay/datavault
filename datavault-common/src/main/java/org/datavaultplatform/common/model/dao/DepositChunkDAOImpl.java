package org.datavaultplatform.common.model.dao;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.DepositChunk;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DepositChunkDAOImpl extends BaseDaoImpl<DepositChunk,String> implements
    DepositChunkDAO {

    public DepositChunkDAOImpl(EntityManager em) {
        super(DepositChunk.class, em);
    }

    @Override
    public DepositChunk save(DepositChunk chunk) {
        Session session = this.getCurrentSession();
        session.persist(chunk);
        return chunk;
    }
    
    @Override
    public DepositChunk update(DepositChunk chunk) {
        Session session = this.getCurrentSession();
        session.update(chunk);
        return chunk;
    }

    @Override
    public List<DepositChunk> list() {
        return list("id");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DepositChunk> list(String sort) {
        Session session = this.getCurrentSession();
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
    public Optional<DepositChunk> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DepositChunk.class);
        criteria.add(Restrictions.eq("id",Id));
        DepositChunk chunk = (DepositChunk)criteria.uniqueResult();
        return Optional.ofNullable(chunk);
    }
}
