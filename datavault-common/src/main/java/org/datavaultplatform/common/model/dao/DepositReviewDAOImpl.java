package org.datavaultplatform.common.model.dao;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.DepositReview;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DepositReviewDAOImpl extends BaseDaoImpl<DepositReview,String> implements
    DepositReviewDAO {

    public DepositReviewDAOImpl( EntityManager em ) {
        super(DepositReview.class, em);
    }

    @Override
    public DepositReview save(DepositReview depositReview) {
        Session session = this.getCurrentSession();
        session.persist(depositReview);
        return depositReview;
    }
    
    @Override
    public DepositReview update(DepositReview depositReview) {
        Session session = this.getCurrentSession();
        session.update(depositReview);
        return depositReview;
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<DepositReview> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DepositReview.class);
        List<DepositReview> depositReviews = criteria.list();
        return depositReviews;
    }

    @Override
    public Optional<DepositReview> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DepositReview.class);
        criteria.add(Restrictions.eq("id",Id));
        DepositReview depositReview = (DepositReview)criteria.uniqueResult();
        return Optional.ofNullable(depositReview);
    }

    @Override
    public List<DepositReview> search(String query) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DepositReview.class);
        criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%")));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<DepositReview> depositReviews = criteria.list();
        return depositReviews;
    }

    @Override
    public long count() {
        Session session = this.getCurrentSession();
        return count(session, DepositReview.class);
    }
}
