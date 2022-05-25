package org.datavaultplatform.common.model.dao.custom;

import javax.persistence.EntityManager;
import org.hibernate.Session;

public abstract class BaseCustomDaoImpl {

  private final EntityManager em;

  public BaseCustomDaoImpl(EntityManager em) {
    this.em = em;
  }

  public Session getCurrentSession() {
    return em.unwrap(Session.class);
  }

}
