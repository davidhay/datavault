package org.datavaultplatform.common.model.dao;

import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class BaseDaoImpl<T,ID> extends SimpleJpaRepository<T,ID> {

  private final EntityManager em;

  public BaseDaoImpl(Class<T> domainClass, EntityManager em) {
    super(domainClass, em);
    this.em = em;
  }

  public Session getCurrentSession() {
    return em.unwrap(Session.class);
  }

  protected long count(Session session, Class<?> clazz) {
    Long count = (Long)session.createCriteria(clazz).setProjection(Projections.rowCount()).uniqueResult();
    return count;
  }
}
