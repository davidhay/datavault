package org.datavaultplatform.common.model.dao;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDAOImpl extends BaseDaoImpl<User,String> implements UserDAO {


    public UserDAOImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public User save(User user) {
        Session session = this.getCurrentSession();
        session.persist(user);
        return user;
    }
    
    @Override
    public User update(User user) {
        Session session = this.getCurrentSession();
        session.update(user);
        return user;
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<User> list() {        
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        List<User> users = criteria.list();
        return users;
    }

    @Override
    public Optional<User> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("id",Id));
        User user = (User)criteria.uniqueResult();
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> search(String query) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%"), Restrictions.ilike("firstname", "%" + query + "%"), Restrictions.ilike("lastname", "%" + query + "%")));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<User> users = criteria.list();
        return users;
    }

    @Override
    public long count() {
        Session session = this.getCurrentSession();
        return count(session, User.class);
    }
}
