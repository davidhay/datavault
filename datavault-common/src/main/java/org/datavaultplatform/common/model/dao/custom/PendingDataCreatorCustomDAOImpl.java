package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.common.model.PendingDataCreator;
import org.hibernate.Session;

@Slf4j

public class PendingDataCreatorCustomDAOImpl
    extends BaseCustomDAOImpl implements PendingDataCreatorCustomDAO {

    public PendingDataCreatorCustomDAOImpl(EntityManager em) {
        super(em);
    }

    @Override
    public void save(List<PendingDataCreator> pendingDataCreators) {
        Session session = this.getCurrentSession();
        for (PendingDataCreator pdc : pendingDataCreators) {
            session.persist(pdc);
        }
    }
}
