package org.datavaultplatform.common.model.dao;

import java.util.Arrays;
import java.util.Collections;
import javax.transaction.Transactional;
import org.datavaultplatform.common.model.PendingDataCreator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class PendingDataCreatorDAOImpl implements PendingDataCreatorDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(PendingDataCreatorDAOImpl.class);

    private final SessionFactory sessionFactory;

    public PendingDataCreatorDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(List<PendingDataCreator> pendingDataCreators) {
        Session session = this.sessionFactory.getCurrentSession();
        for (PendingDataCreator pdc : pendingDataCreators) {
            session.persist(pdc);
        }
    }

    @Override
    public PendingDataCreator findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(PendingDataCreator.class);
        criteria.add(Restrictions.eq("id", Id));
        PendingDataCreator creator = (PendingDataCreator) criteria.uniqueResult();
        return creator;
    }

    @Override
    public void save(PendingDataCreator item) {
        save(Collections.singletonList(item));
    }

    @Override
    public void update(PendingDataCreator pendingDataCreator) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(pendingDataCreator);
    }

    @Override
    public List<PendingDataCreator> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(PendingDataCreator.class);
        List<PendingDataCreator> result = criteria.list();
        return result;
    }

    @Override
    public void delete(String id) {
        PendingDataCreator creator = findById(id);
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(creator);
    }
}
