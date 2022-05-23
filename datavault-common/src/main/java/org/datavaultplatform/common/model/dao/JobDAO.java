package org.datavaultplatform.common.model.dao;

import org.datavaultplatform.common.model.Job;
 
public interface JobDAO extends BaseDAO<Job> {

    long count();
}
