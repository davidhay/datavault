package org.datavaultplatform.common.model.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class BaseDaoImpl {

  protected int count(Session session, Class<?> clazz){
    Long count = (Long)session.createCriteria(clazz).setProjection(Projections.rowCount()).uniqueResult();
    return count.intValue();
  }
}
