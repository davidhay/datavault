package org.datavaultplatform.common.model.dao.custom;

import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.common.model.ArchiveStore;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Slf4j
public class ArchiveStoreCustomDAOImpl extends BaseCustomDAOImpl implements
    ArchiveStoreCustomDAO {

    public ArchiveStoreCustomDAOImpl(EntityManager em) {
        super(em);
    }

    @Override
    public ArchiveStore findForRetrieval() {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(ArchiveStore.class);
        criteria.add(Restrictions.eq("retrieveEnabled",true));
        ArchiveStore archiveStore = (ArchiveStore)criteria.uniqueResult();
        return archiveStore;
    }
}
