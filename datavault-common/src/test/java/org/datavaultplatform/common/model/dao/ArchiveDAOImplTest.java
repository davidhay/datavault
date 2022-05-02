package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.sql.DataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.services.RolesAndPermissionsService;
import org.datavaultplatform.broker.services.VaultsService;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.common.model.Archive;
import org.datavaultplatform.common.model.ArchiveStore;
import org.datavaultplatform.common.model.Deposit;
import org.datavaultplatform.common.model.dao.ArchiveDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.TransactionManager;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

@SpringBootTest
@AddTestProperties
@Slf4j
public class ArchiveDAOImplTest {

    @Autowired
    DataSource datasource;

    @Autowired
    ArchiveDAO archiveDAO;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    TransactionManager transactionManager;

    @Test
    void testSaveAndReadUser() {
        assertNull(archiveDAO.findById("1"));
        assertEquals(0, archiveDAO.count());

        Archive archive = new Archive();
        archive.setArchiveId("1");

        System.out.println(System.currentTimeMillis());
        archive.setCreationTime(new Date());
        archive.setArchiveStore(new ArchiveStore());
        archive.setDeposit(new Deposit());

        archiveDAO.save(archive);
        assertEquals(1, archiveDAO.count());
        Archive found = archiveDAO.findById("1");
        assertEquals(new Date(), found.getCreationTime());
        assertEquals(new ArchiveStore(), found.getArchiveStore());
        assertEquals(new Deposit(), found.getDeposit());

        //TODO : yuch - should be easier way to clean up - Create tested utility (if it doesn't already exist)
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(archive);
        tx.commit();
        session.close();
    }
}
