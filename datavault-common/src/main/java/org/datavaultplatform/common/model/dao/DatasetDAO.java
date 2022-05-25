package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.Dataset;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DatasetDAO extends BaseDAO<Dataset> {
}
