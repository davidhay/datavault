package org.datavaultplatform.common.model.dao;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.*;
import org.datavaultplatform.common.util.DaoUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class RetrieveDAOImpl implements RetrieveDAO {

    private final SessionFactory sessionFactory;

    public RetrieveDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(Retrieve retrieve) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(retrieve);
    }

    @Override
    public void update(Retrieve retrieve) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(retrieve);
    }

    @Override
    public List<Retrieve> list() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Retrieve> list(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createRetrieveCriteriaBuilder(userId, session, Permission.CAN_VIEW_RETRIEVES);
        if (criteriaBuilder.hasNoAccess()) {
            return new ArrayList<>();
        }
        Criteria criteria = criteriaBuilder.build();
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("timestamp"));
        List<Retrieve> retrieves = criteria.list();
        return retrieves;
    }

    @Override
    public Retrieve findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Retrieve.class);
        criteria.add(Restrictions.eq("id", Id));
        Retrieve retrieve = (Retrieve) criteria.uniqueResult();
        return retrieve;
    }

    @Override
    public int count(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createRetrieveCriteriaBuilder(userId, session, Permission.CAN_VIEW_RETRIEVES);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return count.intValue();
    }

    @Override
    public int queueCount(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createRetrieveCriteriaBuilder(userId, session, Permission.CAN_VIEW_QUEUES);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        criteria.add(Restrictions.eq("status", Retrieve.Status.NOT_STARTED));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        return count.intValue();
    }

    @Override
    public int inProgressCount(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createRetrieveCriteriaBuilder(userId, session, Permission.CAN_VIEW_IN_PROGRESS);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        criteria.add(Restrictions.and(Restrictions.ne("status", Retrieve.Status.NOT_STARTED), Restrictions.ne("status", Retrieve.Status.COMPLETE)));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        return count.intValue();
    }

    @Override
    public List<Retrieve> inProgress() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Retrieve.class);
        criteria.add(Restrictions.and(Restrictions.ne("status", Retrieve.Status.NOT_STARTED), Restrictions.ne("status", Retrieve.Status.COMPLETE)));
        criteria.addOrder(Order.asc("timestamp"));
        List<Retrieve> retrieves = criteria.list();
        return retrieves;
    }

    private SchoolPermissionCriteriaBuilder createRetrieveCriteriaBuilder(String userId, Session session, Permission permission) {
        return new SchoolPermissionCriteriaBuilder()
                .setSession(session)
                .setCriteriaType(Retrieve.class)
                .setCriteriaName("retrieve")
                .setTypeToSchoolAliasGenerator(criteria ->
                        criteria.createAlias("retrieve.deposit", "deposit")
                                .createAlias("deposit.vault", "vault")
                                .createAlias("vault.group", "group"))
                .setSchoolIds(DaoUtils.getPermittedSchoolIds(session, userId, permission));
    }
}
