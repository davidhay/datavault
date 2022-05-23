package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.VaultReview;

import java.util.List;

public interface VaultReviewDAO extends BaseDAO<VaultReview> {

    List<VaultReview> search(String query);

    int count();
}
