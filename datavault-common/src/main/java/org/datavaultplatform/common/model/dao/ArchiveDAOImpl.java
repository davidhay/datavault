package org.datavaultplatform.common.model.dao;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.Archive;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public class ArchiveDAOImpl implements ArchiveDAO {

    private final SessionFactory sessionFactory;

    public ArchiveDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Archive archive) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(archive);
    }

    @Override
    public void update(Archive archive) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(archive);
    }

    @Override
    public List<Archive> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Archive.class);
        List<Archive> archives = criteria.list();
        return archives;
    }

    @Override
    public Archive findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Archive.class);
        criteria.add(Restrictions.eq("id",Id));
        Archive archive = (Archive)criteria.uniqueResult();
        return archive;
    }

}
