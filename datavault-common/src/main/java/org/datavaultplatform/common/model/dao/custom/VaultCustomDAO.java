package org.datavaultplatform.common.model.dao.custom;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import java.util.List;

import org.datavaultplatform.common.model.Vault;
import org.springframework.transaction.annotation.Transactional;


public interface VaultCustomDAO extends BaseCustomDAO {

    List<Vault> list(String userId, String sort, String order, String offset, String maxResult);

    List<Vault> search(String userId, String query, String sort, String order, String offset, String maxResult);

    int count(String userId);

    int getRetentionPolicyCount(int status);

    int getTotalNumberOfVaults(String userId);

    int getTotalNumberOfVaults(String userId, String query);

    List<Object[]> getAllProjectsSize();
}