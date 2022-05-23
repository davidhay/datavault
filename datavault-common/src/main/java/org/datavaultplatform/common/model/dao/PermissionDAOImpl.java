package org.datavaultplatform.common.model.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.PermissionModel;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PermissionDAOImpl extends BaseDaoImpl<PermissionModel,String> implements PermissionDAO {

    public PermissionDAOImpl(EntityManager em) {
        super(PermissionModel.class, em);
    }

    @Override
    public void synchronisePermissions() {
        Session session = this.getCurrentSession();

        // First, remove any permissions that are no longer found in the Permission enum from all roles...
        String commaSeparatedPermissionIds = Arrays.stream(Permission.values())
                .map(Permission::getId)
                .collect(Collectors.joining(","));
        String deleteUnknownRolePermissionsSql = "DELETE FROM Role_permissions WHERE NOT FIND_IN_SET(permission_id, '"
                + commaSeparatedPermissionIds
                + "');";
        session.createSQLQuery(deleteUnknownRolePermissionsSql).executeUpdate();

        // ...then remove those permissions themselves...
        String deleteUnknownPermissionsSql = "DELETE FROM Permissions WHERE NOT FIND_IN_SET(id, '"
                + commaSeparatedPermissionIds
                + "');";
        session.createSQLQuery(deleteUnknownPermissionsSql).executeUpdate();

        // ...then store default entries for anything found in the enum but not the database
        Criteria permissionEnumEntries = session.createCriteria(PermissionModel.class);
        Set<String> permissionIds = Arrays.stream(Permission.values())
                .map(Permission::getId)
                .collect(Collectors.toSet());
        permissionEnumEntries.add(Restrictions.in("id", permissionIds));
        Set<Permission> persistedPermissions = ((List<PermissionModel>) permissionEnumEntries.list()).stream()
                .map(PermissionModel::getPermission)
                .collect(Collectors.toSet());
        for (Permission p : Permission.values()) {
            if (!persistedPermissions.contains(p)) {
                session.persist(PermissionModel.createDefault(p));
            }
        }
    }

    @Override
    public PermissionModel find(Permission permission) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(PermissionModel.class);
        criteria.add(Restrictions.eq("id", permission.name()));
        PermissionModel permissionModel = (PermissionModel) criteria.uniqueResult();
        return permissionModel;
    }

    @Override
    public List<PermissionModel> findAll() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(PermissionModel.class);
        List<PermissionModel> permissions = criteria.list();
        return permissions;
    }

    @Override
    public List<PermissionModel> findByType(PermissionModel.PermissionType type) {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(PermissionModel.class);
        criteria.add(Restrictions.eq("type", type));
        List<PermissionModel> permissions = criteria.list();
        return permissions;
    }
}
