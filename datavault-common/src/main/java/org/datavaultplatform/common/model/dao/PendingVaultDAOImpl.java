package org.datavaultplatform.common.model.dao;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.PendingVault;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.util.DaoUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PendingVaultDAOImpl extends BaseDaoImpl<PendingVault,String> implements PendingVaultDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(PendingVaultDAOImpl.class);

    public PendingVaultDAOImpl(EntityManager em) {
        super(PendingVault.class, em);
    }

    @Override
    public PendingVault save(PendingVault pendingVault) {
        Session session = this.getCurrentSession();
        session.persist(pendingVault);
        return pendingVault;
    }

    @Override
    public Optional<PendingVault> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(PendingVault.class);
        criteria.add(Restrictions.eq("id", Id));
        PendingVault vault = (PendingVault)criteria.uniqueResult();
        return Optional.ofNullable(vault);
    }

    @Override
    public PendingVault update(PendingVault vault) {
        Session session = this.getCurrentSession();
        session.update(vault);
        return vault;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<PendingVault> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(PendingVault.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("creationTime"));
        List<PendingVault> vaults = criteria.list();
        return vaults;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PendingVault> list(String userId, String sort, String order, String offset, String maxResult) {
        Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createPendingVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return new ArrayList<>();
        }
        Criteria criteria = criteriaBuilder.build();
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        order(sort, order, criteria);
        
        if((offset != null && maxResult != null) || !maxResult.equals("0")) {
        	criteria.setMaxResults(Integer.parseInt(maxResult));
        	criteria.setFirstResult(Integer.parseInt(offset));
        }

        List<PendingVault> vaults = criteria.list();
        return vaults;
    }
    
    @Override
	public int getTotalNumberOfPendingVaults(String userId, String confirmed) {
		Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createPendingVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        if (confirmed != null && ! confirmed.equals("null") && ! confirmed.equals("")){
            Boolean conf = new Boolean(confirmed);
            criteria.add(Restrictions.eq("confirmed", conf));
        }
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setProjection(Projections.rowCount());
        int totalNumberOfVaults = ((Long) criteria.uniqueResult()).intValue();
        return totalNumberOfVaults;
	}

	/**
	 * Retrieve Total NUmber of rows after applying the filter
	 */
	@Override
	public int getTotalNumberOfPendingVaults(String userId, String query, String confirmed) {
		Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createPendingVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        if (query != null && !query.equals("")) {
            criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%"), Restrictions.ilike("name", "%" + query + "%"), Restrictions.ilike("description", "%" + query + "%")));
        }
        if (confirmed != null && ! confirmed.equals("null") && ! confirmed.equals("")){
            Boolean conf = new Boolean(confirmed);
            criteria.add(Restrictions.eq("confirmed", conf));
        }
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setProjection(Projections.rowCount());
        int totalNumberOfVaults = ((Long) criteria.uniqueResult()).intValue();
        return totalNumberOfVaults;
	}
    
    @SuppressWarnings("unchecked")
    @Override
    public List<PendingVault> search(String userId, String query, String sort, String order, String offset, String maxResult, String confirmed) {
        Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createPendingVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return new ArrayList<>();
        }
        Criteria criteria = criteriaBuilder.build();
        if( ! (query == null || query.equals("")) ) {
            criteria.add(Restrictions.or(
                    Restrictions.ilike("id", "%" + query + "%"),
                    Restrictions.ilike("name", "%" + query + "%"),
                    Restrictions.ilike("description", "%" + query + "%")));
        }

        if (confirmed != null && ! confirmed.equals("null") && ! confirmed.equals("")){
            Boolean conf = new Boolean(confirmed);
            criteria.add(Restrictions.eq("confirmed", conf));
        }
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        order(sort, order, criteria);
        if((offset != null && maxResult != null) || !maxResult.equals("0")) {
        	criteria.setMaxResults(Integer.parseInt(maxResult));
        	criteria.setFirstResult(Integer.parseInt(offset));
        }

        List<PendingVault> vaults = criteria.list();
        return vaults;
    }

    @Override
    public int count(String userId) {
        Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createPendingVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return count.intValue();
    }
    
    private SchoolPermissionCriteriaBuilder createPendingVaultCriteriaBuilder(String userId, Session session, Permission permission) {
	    return new SchoolPermissionCriteriaBuilder()
                .setCriteriaType(PendingVault.class)
                .setCriteriaName("pendingvault")
                .setSession(session)
                .setTypeToSchoolAliasGenerator(criteria -> criteria.createAlias("pendingvault.group", "group"))
                .setSchoolIds(DaoUtils.getPermittedSchoolIds(session, userId, permission));
    }
    
    private void order(String sort, String order, Criteria criteria) {
        // Default to ascending order
        boolean asc = ("desc".equals(order))?false:true;

//        // See if there is a valid sort option
        if ("user".equals(sort)) {
            if (asc) {
                criteria.addOrder(Order.asc("user.id"));
            } else {
                criteria.addOrder(Order.desc("user.id"));
            }
        } else {
            if (asc) {
                criteria.addOrder(Order.asc(sort));
            } else {
                criteria.addOrder(Order.desc(sort));
            }
        }
    }

    @Override
    public void deleteById(String Id) {

        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(PendingVault.class);
        criteria.add(Restrictions.eq("id", Id));
        PendingVault pv = (PendingVault) criteria.uniqueResult();
        //session.delete(pv);
        //session.flush();
        //session.close();

        session.delete(pv);
    }
}
