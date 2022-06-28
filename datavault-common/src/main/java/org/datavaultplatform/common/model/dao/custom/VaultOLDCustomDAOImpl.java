package org.datavaultplatform.common.model.dao.custom;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.Vault;
import org.datavaultplatform.common.model.Vault_;
import org.datavaultplatform.common.model.dao.SchoolPermissionCriteriaBuilder;
import org.datavaultplatform.common.util.DaoUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Slf4j
public class VaultOLDCustomDAOImpl extends BaseCustomDAOImpl implements VaultOLDCustomDAO {

    public VaultOLDCustomDAOImpl(EntityManager em) {
        super(em);
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Vault> list(String userId, String sort, String order, String offset, String maxResult) {
        Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return new ArrayList<>();
        }
        Criteria criteria = criteriaBuilder.build();
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        order(sort, order, criteria);
        if((offset != null && maxResult != null) && !maxResult.equals("0")) {
        	criteria.setMaxResults(Integer.valueOf(maxResult));
        	criteria.setFirstResult(Integer.valueOf(offset));
        }

        List<Vault> vaults = criteria.list();
        return vaults;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vault> search(String userId, String query, String sort, String order, String offset, String maxResult) {
        Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return new ArrayList<>();
        }
        Criteria criteria = criteriaBuilder.build();
        if( ! (query == null || query.equals("")) ) {
            criteria.add(Restrictions.or(
                    Restrictions.ilike(Vault_.ID, "%" + query + "%"),
                    Restrictions.ilike(Vault_.NAME, "%" + query + "%"),
                    Restrictions.ilike(Vault_.DESCRIPTION, "%" + query + "%")));
        }
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        order(sort, order, criteria);
        if((offset != null && maxResult != null) && !maxResult.equals("0")) {
        	criteria.setMaxResults(Integer.valueOf(maxResult));
        	criteria.setFirstResult(Integer.valueOf(offset));
        }

        List<Vault> vaults = criteria.list();
        return vaults;
    }

    @Override
    public int count(String userId) {
        Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return count.intValue();
    }

    @Override
    public int getRetentionPolicyCount(int status) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Vault.class);
        criteria.add(Restrictions.eq(Vault_.RETENTION_POLICY_STATUS, status));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long)criteria.uniqueResult();
        return count.intValue();
    }

    private void order(String sort, String order, Criteria criteria) {
        // Default to ascending order
        boolean asc = ("desc".equals(order))?false:true;

        // See if there is a valid sort option
        if ("user".equals(sort)) {
            if (asc) {
                criteria.addOrder(Order.asc("user.id"));
            } else {
                criteria.addOrder(Order.desc("user.id"));
            }
        } else if ("groupID".equals(sort)) {
            //we already have an association to Group - adding another results in error
        	  //criteria.createAlias("group", "g");
            if (asc) {
                criteria.addOrder(Order.asc("group.id"));
            } else {
                criteria.addOrder(Order.desc("group.id"));
            }
        } else if ("crisID".equals(sort)) {
            criteria.createAlias("dataset", "d");
            if (asc) {
                criteria.addOrder(Order.asc("d.crisId"));
            } else {
                criteria.addOrder(Order.desc("d.crisId"));
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
	public int getTotalNumberOfVaults(String userId) {
		Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setProjection(Projections.rowCount());
        int totalNumberOfVaults = ((Long) criteria.uniqueResult()).intValue();
        return totalNumberOfVaults;
	}

	/**
	 * Retrieve Total NUmber of rows after applying the filter
	 */
	@Override
	public int getTotalNumberOfVaults(String userId, String query) {
		Session session = this.getCurrentSession();
        SchoolPermissionCriteriaBuilder criteriaBuilder = createVaultCriteriaBuilder(userId, session, Permission.CAN_MANAGE_VAULTS);
        if (criteriaBuilder.hasNoAccess()) {
            return 0;
        }
        Criteria criteria = criteriaBuilder.build();
        if (query != null && !query.equals("")) {
            criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%"), Restrictions.ilike("name", "%" + query + "%"), Restrictions.ilike("description", "%" + query + "%")));
        }
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setProjection(Projections.rowCount());
        int totalNumberOfVaults = ((Long) criteria.uniqueResult()).intValue();
        session.close();
        return totalNumberOfVaults;
	}

	@Override
	public List<Object[]> getAllProjectsSize() {
		Session session = this.getCurrentSession();
		Query<Object[]> query = session.createQuery("select v.projectId, sum(v.vaultSize) from Vault v group by v.projectId");
		return query.list();
	}

    private SchoolPermissionCriteriaBuilder createVaultCriteriaBuilder(String userId, Session session, Permission permission) {
	    return new SchoolPermissionCriteriaBuilder()
                .setCriteriaType(Vault.class)
                .setCriteriaName("vault")
                .setSession(session)
                .setTypeToSchoolAliasGenerator(criteria -> criteria.createAlias("vault.group", "group"))
                .setSchoolIds(DaoUtils.getPermittedSchoolIds(session, userId, permission));
    }


    public List<Vault> getAll() {
        Session sesh = getCurrentSession();
        Criteria criteria = sesh.createCriteria(Vault.class);

        /*
        CriteriaImpl criteriaImpl = (CriteriaImpl)criteria;
        SharedSessionContractImplementor session = criteriaImpl.getSession();
        SessionFactoryImplementor factory = session.getFactory();
        CriteriaQueryTranslator translator=new CriteriaQueryTranslator(factory,criteriaImpl,criteriaImpl.getEntityOrClassName(),CriteriaQueryTranslator.ROOT_SQL_ALIAS);
        String[] implementors = factory.getImplementors( criteriaImpl.getEntityOrClassName() );

        CriteriaJoinWalker walker = new CriteriaJoinWalker((OuterJoinLoadable)factory.getEntityPersister(implementors[0]),
            translator,
            factory,
            criteriaImpl,
            criteriaImpl.getEntityOrClassName(),
            session.getLoadQueryInfluencers()   );

        String sql=walker.getSQLString();
        log.info("SQL is [{}]", sql);
         */
        return criteria.list();
    }
}
