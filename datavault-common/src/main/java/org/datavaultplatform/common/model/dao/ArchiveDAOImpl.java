package org.datavaultplatform.common.model.dao;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.Archive;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public class ArchiveDAOImpl extends BaseDaoImpl<Archive,String> implements ArchiveDAO {

    @Autowired
    public ArchiveDAOImpl(EntityManager em) {
        super(Archive.class , em);
    }

    @Override
    public Archive save(Archive archive) {
        Session session = getCurrentSession();
        session.persist(archive);
        return archive;
    }

    @Override
    public Archive update(Archive archive) {
        Session session = this.getCurrentSession();
        session.update(archive);
        return archive;
    }

    @Override
    public List<Archive> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Archive.class);
        List<Archive> archives = criteria.list();
        return archives;
    }

    @Override
    public Optional<Archive> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Archive.class);
        criteria.add(Restrictions.eq("id",Id));
        Archive archive = (Archive)criteria.uniqueResult();
        return Optional.ofNullable(archive);
    }

}
