package org.datavaultplatform.common.model.dao;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.Audit;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AuditDAOImpl extends BaseDaoImpl<Audit,String> implements AuditDAO {

    public AuditDAOImpl(EntityManager em) {
        super(Audit.class, em);
    }

    @Override
    public Audit save(Audit audit) {
        Session session = this.getCurrentSession();
        session.persist(audit);
        return audit;
    }

    @Override
    public Audit update(Audit audit) {
        Session session = this.getCurrentSession();
        session.update(audit);
        return audit;
    }

    @Override
    public List<Audit> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Audit.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("timestamp"));
        List<Audit> audits = criteria.list();
        return audits;
    }

    @Override
    public Optional<Audit> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Audit.class);
        criteria.add(Restrictions.eq("id", Id));
        Audit audit = (Audit) criteria.uniqueResult();
        return Optional.ofNullable(audit);
    }

    @Override
    public long count() {
        Session session = this.getCurrentSession();
        return count(session, Audit.class);
    }
}