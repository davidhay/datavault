package org.datavaultplatform.common.model.dao;

import java.util.List;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.model.Dataset;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DatasetDAOImpl extends BaseDaoImpl<Dataset,String> implements DatasetDAO {

    public DatasetDAOImpl(EntityManager em) {
        super(Dataset.class, em);
    }

    @Override
    public Dataset save(Dataset dataset) {
        Session session = this.getCurrentSession();
        session.persist(dataset);
        return dataset;
    }
    
    @Override
    public Dataset update(Dataset dataset) {
        Session session = this.getCurrentSession();
        session.update(dataset);
        return dataset;
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Dataset> list() {        
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Dataset.class);
        criteria.addOrder(Order.asc("sort"));
        List<Dataset> policies = criteria.list();
        return policies;
    }
    
    @Override
    public Optional<Dataset> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Dataset.class);
        criteria.add(Restrictions.eq("id",Id));
        Dataset dataset = (Dataset)criteria.uniqueResult();
        return Optional.ofNullable(dataset);
    }
}
