package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.AuditChunkStatus;
import org.datavaultplatform.common.model.dao.AuditChunkStatusDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = DataVaultBrokerApp.class)
@AddTestProperties
@Slf4j
@TestPropertySource(properties = {
    "broker.email.enabled=false",
    "broker.controllers.enabled=false",
    "broker.rabbit.enabled=false",
    "broker.scheduled.enabled=false"
})
public class AuditChunkStatusDAOTest extends BaseReuseDatabaseTest {

  @Autowired
  AuditChunkStatusDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    AuditChunkStatus auditChunkStatus1 = getAuditChunkStatus1();

    AuditChunkStatus auditChunkStatus2 = getAuditChunkStatus2();

    dao.save(auditChunkStatus1);
    assertNotNull(auditChunkStatus1.getID());
    assertEquals(1, count());

    dao.save(auditChunkStatus2);
    assertNotNull(auditChunkStatus2.getID());
    assertEquals(2, count());

    AuditChunkStatus foundById1 = dao.findById(auditChunkStatus1.getID()).get();
    assertEquals(auditChunkStatus1.getID(), foundById1.getID());

    AuditChunkStatus foundById2 = dao.findById(auditChunkStatus2.getID()).get();
    assertEquals(auditChunkStatus2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    AuditChunkStatus AuditChunkStatus1 = getAuditChunkStatus1();

    AuditChunkStatus AuditChunkStatus2 = getAuditChunkStatus2();

    dao.save(AuditChunkStatus1);
    assertNotNull(AuditChunkStatus1.getID());
    assertEquals(1, count());

    dao.save(AuditChunkStatus2);
    assertNotNull(AuditChunkStatus2.getID());
    assertEquals(2, count());

    List<AuditChunkStatus> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(AuditChunkStatus1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(AuditChunkStatus2.getID())).count());
  }


  @Test
  void testUpdate() {
    AuditChunkStatus AuditChunkStatus1 = getAuditChunkStatus1();

    dao.save(AuditChunkStatus1);

    AuditChunkStatus1.setNote("111-updated");

    dao.update(AuditChunkStatus1);

    AuditChunkStatus found = dao.findById(AuditChunkStatus1.getID()).get();
    assertEquals(AuditChunkStatus1.getNote(), found.getNote());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `AuditChunkStatus`");
    assertEquals(0, count());
  }

  private AuditChunkStatus getAuditChunkStatus1() {
    AuditChunkStatus result = new AuditChunkStatus();
    result.setNote("111");
    return result;
  }

  private AuditChunkStatus getAuditChunkStatus2() {
    AuditChunkStatus result = new AuditChunkStatus();
    result.setNote("222");
    return result;
  }

  long count() {
    return dao.count();
  }
}
