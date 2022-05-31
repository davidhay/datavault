package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.Audit;
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
public class AuditDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  AuditDAO dao;

  @Test
  void testWriteThenRead() {
    Audit audit1 = getAudit1();

    Audit audit2 = getAudit2();

    dao.save(audit1);
    assertNotNull(audit1.getID());
    assertEquals(1, count());

    dao.save(audit2);
    assertNotNull(audit2.getID());
    assertEquals(2, count());

    Audit foundById1 = dao.findById(audit1.getID()).get();
    assertEquals(audit1.getID(), foundById1.getID());

    Audit foundById2 = dao.findById(audit2.getID()).get();
    assertEquals(audit2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    Audit audit1 = getAudit1();

    Audit audit2 = getAudit2();

    dao.save(audit1);
    assertNotNull(audit1.getID());
    assertEquals(1, count());

    dao.save(audit2);
    assertNotNull(audit2.getID());
    assertEquals(2, count());

    List<Audit> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(audit1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(audit2.getID())).count());
  }


  @Test
  void testUpdate() {
    Audit audit1 = getAudit1();

    dao.save(audit1);

    audit1.setNote("111-updated");

    dao.update(audit1);

    Audit found = dao.findById(audit1.getID()).get();
    assertEquals(audit1.getNote(), found.getNote());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Audits`");
    assertEquals(0, count());
  }

  private Audit getAudit1() {
    Audit result = new Audit();
    result.setNote("111");
    return result;
  }

  private Audit getAudit2() {
    Audit result = new Audit();
    result.setNote("222");
    return result;
  }

  long count() {
    return dao.count();
  }
}
