package org.datavaultplatform.common.model.dao;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.VaultReview;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class VaultReviewDAOImpl extends BaseDaoImpl implements VaultReviewDAO {

    private final SessionFactory sessionFactory;

    public VaultReviewDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(VaultReview vaultReview) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(vaultReview);
    }
    
    @Override
    public void update(VaultReview vaultReview) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(vaultReview);
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<VaultReview> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(VaultReview.class);
        List<VaultReview> vaultReviews = criteria.list();
        return vaultReviews;
    }

    @Override
    public VaultReview findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(VaultReview.class);
        criteria.add(Restrictions.eq("id",Id));
        VaultReview vaultReview = (VaultReview)criteria.uniqueResult();
        return vaultReview;
    }

    @Override
    public List<VaultReview> search(String query) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(VaultReview.class);
        criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%")));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<VaultReview> vaultReviews = criteria.list();
        return vaultReviews;
    }

    @Override
    public int count() {
        Session session = this.sessionFactory.getCurrentSession();
        return count(session, VaultReview.class);
    }
}
