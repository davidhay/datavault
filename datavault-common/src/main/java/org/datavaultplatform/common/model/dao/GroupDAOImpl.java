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
public class GroupDAOImpl implements GroupDAO {

    private final SessionFactory sessionFactory;

    public GroupDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Group group) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(group);
    }

    @Override
    public void update(Group group) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(group);
    }

    @Override
    public void delete(Group group) {
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(group);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Group> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Group.class);
        criteria.addOrder(Order.asc("name"));
        List<Group> groups = criteria.list();
        return groups;
    }

    @Override
    public List<Group> list(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createGroupCriteriaBuilder(userId, session, Permission.CAN_VIEW_SCHOOL_ROLE_ASSIGNMENTS);
        if (criteriaBuilder.hasNoAccess()) {
            return new ArrayList<>();
        }
        Criteria criteria = criteriaBuilder.build();
        List<Group> groups = criteria.addOrder(Order.asc("name")).list();
        return groups;
    }

    @Override
    public Group findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Group.class);
        criteria.add(Restrictions.eq("id", Id));
        Group group = (Group)criteria.uniqueResult();
        return group;
    }

    @Override
    public int count(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createGroupCriteriaBuilder(userId, session, Permission.CAN_VIEW_SCHOOL_ROLE_ASSIGNMENTS);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return count.intValue();
    }

    private SchoolPermissionCriteriaBuilder createGroupCriteriaBuilder(String userId, Session session, Permission permission) {
        return new SchoolPermissionCriteriaBuilder()
                .setCriteriaType(Group.class)
                .setCriteriaName("group")
                .setSession(session)
                .setSchoolIds(DaoUtils.getPermittedSchoolIds(session, userId, permission));
    }
}
