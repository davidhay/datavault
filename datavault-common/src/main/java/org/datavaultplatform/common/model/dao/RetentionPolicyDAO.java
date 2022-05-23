package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.RetentionPolicy;

public interface RetentionPolicyDAO extends AbstractDAO<RetentionPolicy,Integer> {

    void delete(Integer id);
}
