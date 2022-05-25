package org.datavaultplatform.common.model.dao.custom;

import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.common.model.Client;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
public class ClientCustomDAOImpl extends BaseCustomDaoImpl implements ClientCustomDAO {

    public ClientCustomDAOImpl(EntityManager em) {
        super(em);
    }

    @Override
    public Client findByApiKey(String apiKey) {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(Client.class);
        criteria.add(Restrictions.eq("apiKey", apiKey));
        Client client = (Client)criteria.uniqueResult();
        return client;
    }

}
