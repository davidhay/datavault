package org.datavaultplatform.common.model.dao;

import java.util.List;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.model.ArchiveStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class ArchiveStoreDAOImpl implements ArchiveStoreDAO {

    private static final Logger logger = LoggerFactory.getLogger(ArchiveStoreDAOImpl.class);

    private final SessionFactory sessionFactory;

    public ArchiveStoreDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(ArchiveStore archiveStore) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(archiveStore);
    }
 
    @Override
    public void update(ArchiveStore archiveStore) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(archiveStore);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ArchiveStore> list() {        
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ArchiveStore.class);
        List<ArchiveStore> archiveStores = criteria.list();
        return archiveStores;
    }
    
    @Override
    public ArchiveStore findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ArchiveStore.class);
        criteria.add(Restrictions.eq("id",Id));
        ArchiveStore archiveStore = (ArchiveStore)criteria.uniqueResult();
        return archiveStore;
    }

    @Override
    public ArchiveStore findForRetrieval() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ArchiveStore.class);
        criteria.add(Restrictions.eq("retrieveEnabled",true));
        ArchiveStore archiveStore = (ArchiveStore)criteria.uniqueResult();
        return archiveStore;
    }

    @Override
    public void deleteById(String Id) {
        logger.info("Deleting Archivestore with id " + Id);

        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ArchiveStore.class);
        criteria.add(Restrictions.eq("id", Id));
        ArchiveStore archiveStore = (ArchiveStore)criteria.uniqueResult();
        session.delete(archiveStore);
    }

}
