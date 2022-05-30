package org.datavaultplatform.common.model.dao.custom;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.common.model.DataCreator;
import org.hibernate.Session;

@Slf4j
public class DataCreatorCustomDAOImpl
    extends BaseCustomDAOImpl implements DataCreatorCustomDAO {

    public DataCreatorCustomDAOImpl(EntityManager em) {
        super(em);
    }

    @Override
    public void save(List<DataCreator> dataCreators) {
        Session session = this.getCurrentSession();
        for (DataCreator pdc : dataCreators) {
            session.persist(pdc);
        }
    }
}
