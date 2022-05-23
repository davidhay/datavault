package org.datavaultplatform.common.model.dao;

import java.util.List;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.model.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DataManagerDAOImpl implements DataManagerDAO {

    private static final Logger logger = LoggerFactory.getLogger(DataManagerDAOImpl.class);

    private final SessionFactory sessionFactory;

    public DataManagerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(DataManager dataManager) {
        Session session = this.sessionFactory.getCurrentSession();
        System.out.println("DOA save dataManager:"+dataManager.getUUN());
        session.persist(dataManager);
    }

    @Override
    public void update(DataManager item) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DataManager> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DataManager.class);
        List<DataManager> dataManagers = criteria.list();
        return dataManagers;
    }
    
    @Override
    public DataManager findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DataManager.class);
        criteria.add(Restrictions.eq("id",Id));
        DataManager dataManager = (DataManager)criteria.uniqueResult();
        return dataManager;
    }

    @Override
    public void deleteById(String Id) {
        logger.info("Deleting Data Manager with id " + Id);

        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DataManager.class);
        criteria.add(Restrictions.eq("id", Id));
        DataManager dataManager = (DataManager) criteria.uniqueResult();
        session.delete(dataManager);
    }

}


