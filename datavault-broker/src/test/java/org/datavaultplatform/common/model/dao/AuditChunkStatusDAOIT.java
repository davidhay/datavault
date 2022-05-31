package org.datavaultplatform.common.model.dao;

import static org.datavaultplatform.broker.test.TestUtils.NOW;
import static org.datavaultplatform.broker.test.TestUtils.ONE_WEEK_AGO;
import static org.datavaultplatform.broker.test.TestUtils.TWO_WEEKS_AGO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.AuditChunkStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class AuditChunkStatusDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  AuditChunkStatusDAO dao;

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
  void testListIsSortedByAscendingTimestamp() {
    AuditChunkStatus auditChunkStatus1 = getAuditChunkStatus1();

    AuditChunkStatus auditChunkStatus2 = getAuditChunkStatus2();

    AuditChunkStatus auditChunkStatus3 = getAuditChunkStatus3();

    dao.save(auditChunkStatus1);
    assertNotNull(auditChunkStatus1.getID());
    assertEquals(1, count());

    dao.save(auditChunkStatus2);
    assertNotNull(auditChunkStatus2.getID());
    assertEquals(2, count());

    dao.save(auditChunkStatus3);
    assertNotNull(auditChunkStatus3.getID());
    assertEquals(3, count());

    List<AuditChunkStatus> items = dao.list();
    assertEquals(3, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(auditChunkStatus1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(auditChunkStatus2.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(auditChunkStatus3.getID())).count());

    // The AuditChunkStatus should be ordered by Ascending Timestamp
    assertEquals(
        Arrays.asList(
            auditChunkStatus3.getID(),
            auditChunkStatus1.getID(),
            auditChunkStatus2.getID()),
        items.stream().map(AuditChunkStatus::getID).collect(Collectors.toList()));
  }

  @Test
  void testUpdate() {
    AuditChunkStatus auditChunkStatus1 = getAuditChunkStatus1();

    dao.save(auditChunkStatus1);

    auditChunkStatus1.setNote("111-updated");

    dao.update(auditChunkStatus1);

    AuditChunkStatus found = dao.findById(auditChunkStatus1.getID()).get();
    assertEquals(auditChunkStatus1.getNote(), found.getNote());
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
    result.setTimestamp(ONE_WEEK_AGO);
    result.setNote("111");
    return result;
  }

  private AuditChunkStatus getAuditChunkStatus2() {
    AuditChunkStatus result = new AuditChunkStatus();
    result.setTimestamp(NOW);
    result.setNote("222");
    return result;
  }

  private AuditChunkStatus getAuditChunkStatus3() {
    AuditChunkStatus result = new AuditChunkStatus();
    result.setTimestamp(TWO_WEEKS_AGO);
    result.setNote("333");
    return result;
  }


  long count() {
    return dao.count();
  }
}
