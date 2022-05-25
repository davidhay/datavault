package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.common.model.PendingDataCreator;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
public class PendingDataCreatorCustomDAOImpl
    extends BaseCustomDaoImpl implements PendingDataCreatorCustomDAO {

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
