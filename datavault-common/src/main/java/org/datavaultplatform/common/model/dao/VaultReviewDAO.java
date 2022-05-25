package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.VaultReview;
import org.datavaultplatform.common.model.dao.custom.VaultReviewCustomDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface VaultReviewDAO extends BaseDAO<VaultReview>, VaultReviewCustomDAO {
}
