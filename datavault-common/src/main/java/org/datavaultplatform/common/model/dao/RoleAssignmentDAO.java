package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.RoleAssignment;

import java.util.List;
import java.util.Set;

public interface RoleAssignmentDAO extends AbstractDAO<RoleAssignment,Long> {

    boolean roleAssignmentExists(RoleAssignment roleAssignment);

    Set<Permission> findUserPermissions(String userId);

    List<RoleAssignment> findBySchoolId(String schoolId);

    List<RoleAssignment> findByVaultId(String vaultId);

    List<RoleAssignment> findByPendingVaultId(String vaultId);

    List<RoleAssignment> findByUserId(String userId);

    List<RoleAssignment> findByRoleId(Long roleId);

    boolean hasPermission(String userId, Permission permission);

    boolean isAdminUser(String userId);

    void delete(Long id);
}
