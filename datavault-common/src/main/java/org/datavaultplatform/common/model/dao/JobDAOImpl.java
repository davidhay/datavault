package org.datavaultplatform.common.model.dao;

import java.util.List;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.model.Job;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class JobDAOImpl extends BaseDaoImpl implements JobDAO {

    private final SessionFactory sessionFactory;

    public JobDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Job job) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(job);
    }
    
    @Override
    public void update(Job job) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(job);
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Job> list() {        
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Job.class);
        List<Job> jobs = criteria.list();
        return jobs;
    }
    
    @Override
    public Job findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Job.class);
        criteria.add(Restrictions.eq("id",Id));
        Job job = (Job)criteria.uniqueResult();
        return job;
    }

    @Override
    public int count() {
        Session session = this.sessionFactory.getCurrentSession();
        return count(session, Job.class);
    }
}
