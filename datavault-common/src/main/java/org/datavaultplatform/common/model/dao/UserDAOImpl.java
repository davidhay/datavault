package org.datavaultplatform.common.model.dao;

import java.util.List;
import javax.transaction.Transactional;
import org.datavaultplatform.common.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserDAOImpl extends BaseDaoImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(user);
    }
    
    @Override
    public void update(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(user);
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<User> list() {        
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        List<User> users = criteria.list();
        return users;
    }

    @Override
    public User findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("id",Id));
        User user = (User)criteria.uniqueResult();
        return user;
    }

    @Override
    public List<User> search(String query) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.or(Restrictions.ilike("id", "%" + query + "%"), Restrictions.ilike("firstname", "%" + query + "%"), Restrictions.ilike("lastname", "%" + query + "%")));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<User> users = criteria.list();
        return users;
    }

    @Override
    public int count() {
        Session session = this.sessionFactory.getCurrentSession();
        return count(session, User.class);
    }
}
