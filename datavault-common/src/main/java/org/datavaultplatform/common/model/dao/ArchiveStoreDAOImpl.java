package org.datavaultplatform.common.model.dao;

import java.util.List;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.ArchiveStore;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ArchiveStoreDAOImpl extends BaseDaoImpl<ArchiveStore,String> implements ArchiveStoreDAO {

    private static final Logger logger = LoggerFactory.getLogger(ArchiveStoreDAOImpl.class);

    public ArchiveStoreDAOImpl(EntityManager em) {
        super(ArchiveStore.class, em);
    }

    @Override
    public ArchiveStore save(ArchiveStore archiveStore) {
        Session session = this.getCurrentSession();
        session.persist(archiveStore);
        return archiveStore;
    }
 
    @Override
    public ArchiveStore update(ArchiveStore archiveStore) {
        Session session = this.getCurrentSession();
        session.update(archiveStore);
        return archiveStore;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ArchiveStore> list() {        
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(ArchiveStore.class);
        List<ArchiveStore> archiveStores = criteria.list();
        return archiveStores;
    }
    
    @Override
    public Optional<ArchiveStore> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(ArchiveStore.class);
        criteria.add(Restrictions.eq("id",Id));
        ArchiveStore archiveStore = (ArchiveStore)criteria.uniqueResult();
        return Optional.ofNullable(archiveStore);
    }

    @Override
    public ArchiveStore findForRetrieval() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(ArchiveStore.class);
        criteria.add(Restrictions.eq("retrieveEnabled",true));
        ArchiveStore archiveStore = (ArchiveStore)criteria.uniqueResult();
        return archiveStore;
    }

    @Override
    public void deleteById(String Id) {
        logger.info("Deleting Archivestore with id " + Id);

        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(ArchiveStore.class);
        criteria.add(Restrictions.eq("id", Id));
        ArchiveStore archiveStore = (ArchiveStore)criteria.uniqueResult();
        session.delete(archiveStore);
    }

}
