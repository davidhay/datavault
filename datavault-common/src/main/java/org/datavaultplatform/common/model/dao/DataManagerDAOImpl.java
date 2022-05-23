package org.datavaultplatform.common.model.dao;

import java.util.List;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.model.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DataManagerDAOImpl extends BaseDaoImpl<DataManager,String> implements DataManagerDAO {

    private static final Logger logger = LoggerFactory.getLogger(DataManagerDAOImpl.class);

    public DataManagerDAOImpl(EntityManager em) {
        super(DataManager.class, em);
    }

    @Override
    public DataManager save(DataManager dataManager) {
        Session session = this.getCurrentSession();
        System.out.println("DOA save dataManager:"+dataManager.getUUN());
        session.persist(dataManager);
        return dataManager;
    }

    @Override
    public DataManager update(DataManager item) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DataManager> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DataManager.class);
        List<DataManager> dataManagers = criteria.list();
        return dataManagers;
    }
    
    @Override
    public Optional<DataManager> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DataManager.class);
        criteria.add(Restrictions.eq("id",Id));
        DataManager dataManager = (DataManager)criteria.uniqueResult();
        return Optional.ofNullable(dataManager);
    }

    @Override
    public void deleteById(String Id) {
        logger.info("Deleting Data Manager with id " + Id);

        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DataManager.class);
        criteria.add(Restrictions.eq("id", Id));
        DataManager dataManager = (DataManager) criteria.uniqueResult();
        session.delete(dataManager);
    }

}


