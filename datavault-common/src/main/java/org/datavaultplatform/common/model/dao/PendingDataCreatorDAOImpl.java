package org.datavaultplatform.common.model.dao;

import java.util.Collections;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.PendingDataCreator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PendingDataCreatorDAOImpl extends BaseDaoImpl<PendingDataCreator,String> implements PendingDataCreatorDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(PendingDataCreatorDAOImpl.class);

    public PendingDataCreatorDAOImpl(
        EntityManager em) {
        super(PendingDataCreator.class, em);
    }

    @Override
    public void save(List<PendingDataCreator> pendingDataCreators) {
        Session session = this.getCurrentSession();
        for (PendingDataCreator pdc : pendingDataCreators) {
            session.persist(pdc);
        }
    }

    @Override
    public Optional<PendingDataCreator> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(PendingDataCreator.class);
        criteria.add(Restrictions.eq("id", Id));
        PendingDataCreator creator = (PendingDataCreator) criteria.uniqueResult();
        return Optional.ofNullable(creator);
    }

    @Override
    public PendingDataCreator save(PendingDataCreator item) {
        save(Collections.singletonList(item));
        return item;
    }

    @Override
    public PendingDataCreator update(PendingDataCreator pendingDataCreator) {
        Session session = this.getCurrentSession();
        session.update(pendingDataCreator);
        return pendingDataCreator;
    }

    @Override
    public List<PendingDataCreator> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(PendingDataCreator.class);
        List<PendingDataCreator> result = criteria.list();
        return result;
    }

    @Override
    public void delete(String id) {
        Session session = getCurrentSession();
        findById(id).ifPresent(creator -> {
            session.delete(creator);
        });
    }
}
