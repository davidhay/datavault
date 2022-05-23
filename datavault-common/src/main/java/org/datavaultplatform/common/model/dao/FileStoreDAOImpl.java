package org.datavaultplatform.common.model.dao;

import java.util.List;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.model.FileStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FileStoreDAOImpl extends BaseDaoImpl<FileStore,String> implements FileStoreDAO {

    private static final Logger logger = LoggerFactory.getLogger(FileStoreDAOImpl.class);

    public FileStoreDAOImpl(EntityManager em) {
        super(FileStore.class, em);
    }

    @Override
    public FileStore save(FileStore fileStore) {
        Session session = this.getCurrentSession();
        session.persist(fileStore);
        return fileStore;
    }
 
    @Override
    public FileStore update(FileStore fileStore) {
        Session session = this.getCurrentSession();
        session.update(fileStore);
        return fileStore;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<FileStore> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(FileStore.class);
        List<FileStore> fileStores = criteria.list();
        return fileStores;
    }
    
    @Override
    public Optional<FileStore> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(FileStore.class);
        criteria.add(Restrictions.eq("id",Id));
        FileStore fileStore = (FileStore)criteria.uniqueResult();
        return Optional.ofNullable(fileStore);
    }

    @Override
    public void deleteById(String Id) {
        logger.info("Deleting Filstore with id " + Id);

        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(FileStore.class);
        criteria.add(Restrictions.eq("id", Id));
        FileStore fileStore = (FileStore) criteria.uniqueResult();
        session.delete(fileStore);
    }

}


