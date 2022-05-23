package org.datavaultplatform.common.model.dao;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.*;
import org.datavaultplatform.common.util.RoleUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RoleAssignmentDaoImpl extends BaseDaoImpl<RoleAssignment,Long> implements
    RoleAssignmentDAO {

    public RoleAssignmentDaoImpl(EntityManager em) {
        super(RoleAssignment.class, em);
    }

    @Override
    public boolean roleAssignmentExists(RoleAssignment roleAssignment) {
        Session session = this.getCurrentSession();

        Criteria criteria = session.createCriteria(RoleAssignment.class);
        criteria.add(Restrictions.eq("role", roleAssignment.getRole()));
        criteria.add(Restrictions.eq("userId", roleAssignment.getUserId()));
        if (roleAssignment.getSchoolId() != null) {

            criteria.add(Restrictions.eq("schoolId", roleAssignment.getSchoolId()));
        }
        if (roleAssignment.getVaultId() != null) {
            criteria.add(Restrictions.eq("vaultId", roleAssignment.getVaultId()));
        }
        if (roleAssignment.getPendingVaultId() != null) {
            criteria.add(Restrictions.eq("pendingVaultId", roleAssignment.getPendingVaultId()));
        }
        return criteria.uniqueResult() != null;
    }

    @Override
    public RoleAssignment save(RoleAssignment roleAssignment) {
        Session session = this.getCurrentSession();
        session.persist(roleAssignment);
        return roleAssignment;
    }

    @Override
    public Optional<RoleAssignment> findById(Long id) {
        Session session = this.getCurrentSession();

        Criteria criteria = session.createCriteria(RoleAssignment.class);
        criteria.add(Restrictions.eq("id", id));
        RoleAssignment roleAssignment = (RoleAssignment) criteria.uniqueResult();
        return Optional.ofNullable(roleAssignment);
    }

    @Override
    public Set<Permission> findUserPermissions(String userId) {
        Session session = getCurrentSession();
        Query<PermissionModel> query = session.createQuery("SELECT DISTINCT role.permissions \n" +
            "FROM org.datavaultplatform.common.model.RoleAssignment ra\n" +
            "INNER JOIN ra.role as role\n" +
            "WHERE ra.userId = :userId");
        query.setParameter("userId", userId);

        return query.list()
            .stream()
            .map(PermissionModel::getPermission)
            .collect(Collectors.toSet());
    }

    @Override
    public List<RoleAssignment> list() {
        Session session = getCurrentSession();

        Criteria criteria = session.createCriteria(RoleAssignment.class);
        List<RoleAssignment> roleAssignments = criteria.list();
        return roleAssignments;
    }

    @Override
    public List<RoleAssignment> findBySchoolId(String schoolId) {
        Session session = getCurrentSession();

        List<RoleAssignment> schoolAssignments = findBy(session, "schoolId", schoolId);
        return schoolAssignments;
    }

    private <T> T findObjectById(Session session, Class<T> type, String idName, Object idValue) {
        Criteria criteria = session.createCriteria(type);
        criteria.add(Restrictions.eq(idName, idValue));
        return (T) criteria.uniqueResult();
    }

    private List<RoleAssignment> findBy(Session session, String columnName, Object columnValue) {
        Criteria criteria = session.createCriteria(RoleAssignment.class);
        criteria.add(Restrictions.eq(columnName, columnValue));
        return (List<RoleAssignment>) criteria.list();
    }

    @Override
    public List<RoleAssignment> findByVaultId(String vaultId) {
        Session session = getCurrentSession();

        List<RoleAssignment> vaultAssignments = findBy(session, "vaultId", vaultId);
        return vaultAssignments;
    }

    @Override
    public List<RoleAssignment> findByPendingVaultId(String vaultId) {
        Session session = getCurrentSession();
        List<RoleAssignment> vaultAssignments = findBy(session, "pendingVaultId", vaultId);
        return vaultAssignments;
    }

    @Override
    public List<RoleAssignment> findByUserId(String userId) {
        Session session = getCurrentSession();
        List<RoleAssignment> schoolAssignments = findBy(session, "userId", userId);
        return schoolAssignments;
    }

    @Override
    public List<RoleAssignment> findByRoleId(Long roleId) {
        Session session = getCurrentSession();
        RoleModel role = findObjectById(session, RoleModel.class, "id", roleId);
        List<RoleAssignment> assignments = findBy(session, "role", role);
        return assignments;
    }

    @Override
    public boolean hasPermission(String userId, Permission permission) {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(RoleAssignment.class, "assignment");
        criteria.createAlias("assignment.role", "role");
        criteria.createAlias("role.permissions", "permission");
        criteria.add(Restrictions.eq("assignment.userId", userId));
        criteria.add(Restrictions.eq("permission.id", permission.getId()));
        return criteria.list().size() > 0;
    }

    @Override
    public boolean isAdminUser(String userId) {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(RoleAssignment.class, "assignment");
        criteria.createAlias("assignment.role", "role");
        criteria.add(Restrictions.eq("assignment.userId", userId));
        criteria.add(Restrictions.eq("role.name", RoleUtils.IS_ADMIN_ROLE_NAME));
        return criteria.list().size() > 0;
    }

    @Override
    public RoleAssignment update(RoleAssignment roleAssignment) {
        Session session = this.getCurrentSession();
        session.update(roleAssignment);
        return roleAssignment;
    }

    @Override
    public void delete(Long id) {
        Session session = this.getCurrentSession();
        findById(id).ifPresent(session::delete);
    }
}
