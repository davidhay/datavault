package org.datavaultplatform.common.model.dao;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.VaultReview;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class VaultReviewDAOImpl extends BaseDaoImpl<VaultReview,String> implements VaultReviewDAO {

    public VaultReviewDAOImpl(EntityManager em) {
        super(VaultReview.class, em);
    }

    @Override
    public VaultReview save(VaultReview vaultReview) {
        Session session = this.getCurrentSession();
        session.persist(vaultReview);
        return vaultReview;
    }
    
    @Override
    public VaultReview update(VaultReview vaultReview) {
        Session session = this.getCurrentSession();
        session.update(vaultReview);
        return vaultReview;
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<VaultReview> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(VaultReview.class);
        List<VaultReview> vaultReviews = criteria.list();
        return vaultReviews;
    }

    @Override
    public Optional<VaultReview> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(VaultReview.class);
        criteria.add(Restrictions.eq("id",Id));
        VaultReview vaultReview = (VaultReview)criteria.uniqueResult();
        return Optional.ofNullable(vaultReview);
    }

    @Override
    public List<VaultReview> search(String query) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(VaultReview.class);
        criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%")));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<VaultReview> vaultReviews = criteria.list();
        return vaultReviews;
    }

    @Override
    public long count() {
        Session session = this.getCurrentSession();
        return count(session, VaultReview.class);
    }
}
