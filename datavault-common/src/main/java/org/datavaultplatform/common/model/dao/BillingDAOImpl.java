package org.datavaultplatform.common.model.dao;

import java.util.List;

import javax.transaction.Transactional;
import org.datavaultplatform.common.model.BillingInfo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class BillingDAOImpl extends BaseDaoImpl implements BillingDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingDAOImpl.class);

    private final SessionFactory sessionFactory;

    public BillingDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(BillingInfo billing) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(billing);
    }
 
    @Override
    public void update(BillingInfo billing) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(billing);
    }
    
    @Override
    public void saveOrUpdateVault(BillingInfo billing) {        
        Session session = this.sessionFactory.getCurrentSession();
        session.saveOrUpdate(billing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BillingInfo> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(BillingInfo.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("creationTime"));
        List<BillingInfo> vaults = criteria.list();
        return vaults;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BillingInfo> list(String sort, String order, String offset, String maxResult) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(BillingInfo.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if("asc".equals(order)){
            criteria.addOrder(Order.asc(sort));
        } else {
            criteria.addOrder(Order.desc(sort));
        }
        if((offset != null && maxResult != null) || !maxResult.equals("0")) {
        	criteria.setMaxResults(Integer.parseInt(maxResult));
        	criteria.setFirstResult(Integer.parseInt(offset));
        }

        List<BillingInfo> vaults = criteria.list();
        return vaults;
    }
    
    @Override
    public BillingInfo findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(BillingInfo.class);
        criteria.add(Restrictions.eq("id", Id));
        BillingInfo vault = (BillingInfo)criteria.uniqueResult();
        return vault;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BillingInfo> search(String query, String sort, String order, String offset, String maxResult) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(BillingInfo.class);
        criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%"), Restrictions.ilike("name", "%" + query + "%"), Restrictions.ilike("description", "%" + query + "%")));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if(order.equals("desc")){
            criteria.addOrder(Order.desc(sort));
        } else {
            criteria.addOrder(Order.asc(sort));
        }
        if((offset != null && maxResult != null) || !maxResult.equals("0")) {
        	criteria.setMaxResults(Integer.parseInt(maxResult));
        	criteria.setFirstResult(Integer.parseInt(offset));
        }

        List<BillingInfo> vaults = criteria.list();
        return vaults;
    }

    @Override
    public int count() {
        Session session = this.sessionFactory.getCurrentSession();
        return count(session, BillingInfo.class);
    }

	@Override
	public Long getTotalNumberOfVaults() {
		Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(BillingInfo.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setProjection(Projections.rowCount());
        Long totalNoOfRows = (Long) criteria.uniqueResult();

        return totalNoOfRows;
	}
	/**
	 * Retrieve Total NUmber of rows after applying the filter
	 */
	public Long getTotalNumberOfVaults(String query) {
		Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(BillingInfo.class);
        criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%"), Restrictions.ilike("name", "%" + query + "%"), Restrictions.ilike("description", "%" + query + "%")));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setProjection(Projections.rowCount());
        Long totalNoOfRows = (Long) criteria.uniqueResult();

        return totalNoOfRows;
	}

}
