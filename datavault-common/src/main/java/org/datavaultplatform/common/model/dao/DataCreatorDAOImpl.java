package org.datavaultplatform.common.model.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.datavaultplatform.common.model.DataCreator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DataCreatorDAOImpl extends BaseDaoImpl<DataCreator,String> implements DataCreatorDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCreatorDAOImpl.class);

    public DataCreatorDAOImpl(EntityManager em) {
        super(DataCreator.class, em);
    }

    @Override
    public void save(List<DataCreator> dataCreators) {
        Session session = this.getCurrentSession();
        for (DataCreator pdc : dataCreators) {
            session.persist(pdc);
        }
    }

    @Override
    public Optional<DataCreator> findById(String Id) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DataCreator.class);
        criteria.add(Restrictions.eq("id", Id));
        DataCreator creator = (DataCreator) criteria.uniqueResult();
        return Optional.ofNullable(creator);
    }

    @Override
    public DataCreator save(DataCreator item) {
        save(Collections.singletonList(item));
        return item;
    }

    @Override
    public DataCreator update(DataCreator dataCreator) {
        Session session = this.getCurrentSession();
        session.update(dataCreator);
        return dataCreator;
    }

    @Override
    public List<DataCreator> list() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DataCreator.class);
        List<DataCreator> result = criteria.list();
        return result;
    }

    @Override
    public void delete(String id) {
        Optional<DataCreator> creator = findById(id);
        Session session = this.getCurrentSession();
        session.delete(creator);
    }
}
