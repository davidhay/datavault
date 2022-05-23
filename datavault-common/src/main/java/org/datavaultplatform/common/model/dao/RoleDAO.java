package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.RoleModel;
import org.datavaultplatform.common.model.RoleType;

import java.util.Collection;
import java.util.List;

public interface RoleDAO extends AbstractDAO<RoleModel,Long> {

    void storeSpecialRoles();

    RoleModel getIsAdmin();

    RoleModel getDataOwner();

    RoleModel getDepositor();

    RoleModel getVaultCreator();

    RoleModel getNominatedDataManager();

    Collection<RoleModel> findAll(RoleType roleType);

    List<RoleModel> findAllEditableRoles();

    void delete(Long id);
}
