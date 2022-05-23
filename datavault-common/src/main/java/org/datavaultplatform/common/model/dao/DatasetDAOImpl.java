package org.datavaultplatform.common.model.dao;

import java.util.List;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.model.Dataset;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DatasetDAOImpl implements DatasetDAO {

    private final SessionFactory sessionFactory;

    public DatasetDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Dataset dataset) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(dataset);
    }
    
    @Override
    public void update(Dataset dataset) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(dataset);
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Dataset> list() {        
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Dataset.class);
        criteria.addOrder(Order.asc("sort"));
        List<Dataset> policies = criteria.list();
        return policies;
    }
    
    @Override
    public Dataset findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Dataset.class);
        criteria.add(Restrictions.eq("id",Id));
        Dataset dataset = (Dataset)criteria.uniqueResult();
        return dataset;
    }
}
