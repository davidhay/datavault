package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.RetentionPolicy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RetentionPolicyDAO extends AbstractDAO<RetentionPolicy,Integer> {
}
