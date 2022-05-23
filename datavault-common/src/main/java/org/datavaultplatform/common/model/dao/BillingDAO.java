package org.datavaultplatform.common.model.dao;

import java.util.List;

import org.datavaultplatform.common.model.BillingInfo;
 
public interface BillingDAO extends BaseDAO<BillingInfo> {

    List<BillingInfo> list(String sort, String order, String offset, String maxResult);

    void saveOrUpdateVault(BillingInfo billing);

    List<BillingInfo> search(String query, String sort, String order, String offset, String maxResult);

    Long getTotalNumberOfVaults();

    Long getTotalNumberOfVaults(String query);

}