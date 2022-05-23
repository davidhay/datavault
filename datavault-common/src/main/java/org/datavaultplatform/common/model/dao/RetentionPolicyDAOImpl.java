package org.datavaultplatform.common.model.dao;

import java.util.List;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.RetentionPolicy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class RetentionPolicyDAOImpl implements RetentionPolicyDAO {

    private final SessionFactory sessionFactory;

    public RetentionPolicyDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(RetentionPolicy retentionPolicy) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(retentionPolicy);
    }
    
    @Override
    public void update(RetentionPolicy retentionPolicy) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(retentionPolicy);
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<RetentionPolicy> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(RetentionPolicy.class);
        criteria.addOrder(Order.asc("name"));
        List<RetentionPolicy> policies = criteria.list();
        return policies;
    }
    
    @Override
    public RetentionPolicy findById(Integer id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(RetentionPolicy.class);
        criteria.add(Restrictions.eq("id",id));
        RetentionPolicy retentionPolicy = (RetentionPolicy)criteria.uniqueResult();
        return retentionPolicy;
    }

    @Override
    public void delete(Integer id) {
        RetentionPolicy policy = findById(id);
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(policy);
    }
}
