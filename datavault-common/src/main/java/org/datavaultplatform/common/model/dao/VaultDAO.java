package org.datavaultplatform.common.model.dao;

import java.util.List;

import org.datavaultplatform.common.model.Vault;
 
public interface VaultDAO extends BaseDAO<Vault> {

    void saveOrUpdateVault(Vault vault);

    List<Vault> list(String userId, String sort, String order, String offset, String maxResult);

    List<Vault> search(String userId, String query, String sort, String order, String offset, String maxResult);

    int count(String userId);

    int getRetentionPolicyCount(int status);

    int getTotalNumberOfVaults(String userId);

    int getTotalNumberOfVaults(String userId, String query);

    List<Object[]> getAllProjectsSize();
}