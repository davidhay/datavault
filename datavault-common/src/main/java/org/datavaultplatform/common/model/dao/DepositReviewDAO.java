package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.DepositReview;

import java.util.List;

public interface DepositReviewDAO extends BaseDAO<DepositReview> {

    List<DepositReview> search(String query);

    int count();
}
