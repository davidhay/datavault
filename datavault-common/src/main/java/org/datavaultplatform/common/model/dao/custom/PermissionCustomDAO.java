package org.datavaultplatform.common.model.dao.custom;

import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.PermissionModel;

import java.util.Collection;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PermissionCustomDAO {

    void synchronisePermissions();

    PermissionModel find(Permission permission);

    Collection<PermissionModel> findAll();

    List<PermissionModel> findByType(PermissionModel.PermissionType type);
}
