package org.datavaultplatform.common.model.dao;

import java.util.List;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.RetentionPolicy;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RetentionPolicyDAOImpl extends BaseDaoImpl<RetentionPolicy,Integer> implements
    RetentionPolicyDAO {

    public RetentionPolicyDAOImpl(EntityManager em) {
        super(RetentionPolicy.class, em);
    }

    @Override
    public RetentionPolicy save(RetentionPolicy retentionPolicy) {
        Session session = this.getCurrentSession();
        session.persist(retentionPolicy);
        return retentionPolicy;
    }
    
    @Override
    public RetentionPolicy update(RetentionPolicy retentionPolicy) {
        Session session = this.getCurrentSession();
        session.update(retentionPolicy);
        return retentionPolicy;
    }
 
    @Override
    public List<RetentionPolicy> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(RetentionPolicy.class);
        criteria.addOrder(Order.asc("name"));
        List<RetentionPolicy> policies = criteria.list();
        return policies;
    }
    
    @Override
    public Optional<RetentionPolicy> findById(Integer id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(RetentionPolicy.class);
        criteria.add(Restrictions.eq("id",id));
        RetentionPolicy retentionPolicy = (RetentionPolicy)criteria.uniqueResult();
        return Optional.ofNullable(retentionPolicy);
    }

    @Override
    public void delete(Integer id) {
        Session session = this.getCurrentSession();
        findById(id).ifPresent(session::delete);
    }
}
