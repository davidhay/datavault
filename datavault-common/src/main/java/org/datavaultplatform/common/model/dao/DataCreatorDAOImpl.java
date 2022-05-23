package org.datavaultplatform.common.model.dao;

import java.util.Collections;
import javax.transaction.Transactional;
import org.datavaultplatform.common.model.DataCreator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DataCreatorDAOImpl implements DataCreatorDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCreatorDAOImpl.class);

    private final SessionFactory sessionFactory;

    public DataCreatorDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(List<DataCreator> dataCreators) {
        Session session = this.sessionFactory.getCurrentSession();
        for (DataCreator pdc : dataCreators) {
            session.persist(pdc);
        }
    }

    @Override
    public DataCreator findById(String Id) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DataCreator.class);
        criteria.add(Restrictions.eq("id", Id));
        DataCreator creator = (DataCreator) criteria.uniqueResult();
        return creator;
    }

    @Override
    public void save(DataCreator item) {
        save(Collections.singletonList(item));
    }

    @Override
    public void update(DataCreator dataCreator) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(dataCreator);
    }

    @Override
    public List<DataCreator> list() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(DataCreator.class);
        List<DataCreator> result = criteria.list();
        return result;
    }

    @Override
    public void delete(String id) {
        DataCreator creator = findById(id);
        Session session = this.sessionFactory.getCurrentSession();
        session.delete(creator);
    }
}
