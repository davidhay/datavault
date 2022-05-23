package org.datavaultplatform.common.model.dao;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.DepositReview;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DepositReviewDAOImpl extends BaseDaoImpl implements DepositReviewDAO {

    private final SessionFactory sessionFactory;

    public DepositReviewDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(DepositReview depositReview) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(depositReview);
    }
    
    @Override
    public void update(DepositReview depositReview) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(depositReview);
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<DepositReview> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DepositReview.class);
        List<DepositReview> depositReviews = criteria.list();
        return depositReviews;
    }

    @Override
    public DepositReview findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DepositReview.class);
        criteria.add(Restrictions.eq("id",Id));
        DepositReview depositReview = (DepositReview)criteria.uniqueResult();
        return depositReview;
    }

    @Override
    public List<DepositReview> search(String query) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DepositReview.class);
        criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%")));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<DepositReview> depositReviews = criteria.list();
        return depositReviews;
    }

    @Override
    public int count() {
        Session session = this.sessionFactory.getCurrentSession();
        return count(session, DepositReview.class);
    }
}
