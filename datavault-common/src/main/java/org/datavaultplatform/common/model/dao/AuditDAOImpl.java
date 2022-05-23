package org.datavaultplatform.common.model.dao;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.Audit;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class AuditDAOImpl extends BaseDaoImpl implements AuditDAO {

    private final SessionFactory sessionFactory;

    public AuditDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Audit Audit) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(Audit);
    }

    @Override
    public void update(Audit Audit) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(Audit);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Audit> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Audit.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("timestamp"));
        List<Audit> Audits = criteria.list();
        return Audits;
    }

    @Override
    public Audit findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Audit.class);
        criteria.add(Restrictions.eq("id", Id));
        Audit Audit = (Audit) criteria.uniqueResult();
        return Audit;
    }

    @Override
    public int count() {
        Session session = this.sessionFactory.getCurrentSession();
        return count(session, Audit.class);
    }
}