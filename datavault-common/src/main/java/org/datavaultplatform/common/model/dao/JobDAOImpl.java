package org.datavaultplatform.common.model.dao;

import java.util.List;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.datavaultplatform.common.model.Job;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JobDAOImpl extends BaseDaoImpl<Job,String> implements JobDAO {

    public JobDAOImpl(EntityManager em) {
        super(Job.class, em);
    }

    @Override
    public Job save(Job job) {
        Session session = this.getCurrentSession();
        session.persist(job);
        return job;
    }
    
    @Override
    public Job update(Job job) {
        Session session = this.getCurrentSession();
        session.update(job);
        return job;
    }
 
    @Override
    public List<Job> list() {        
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Job.class);
        List<Job> jobs = criteria.list();
        return jobs;
    }
    
    @Override
    public Optional<Job> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Job.class);
        criteria.add(Restrictions.eq("id",Id));
        Job job = (Job)criteria.uniqueResult();
        return Optional.ofNullable(job);
    }

    @Override
    public long count() {
        Session session = this.getCurrentSession();
        return count(session, Job.class);
    }
}
