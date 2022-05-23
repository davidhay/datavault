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
public class AuditDAOTest extends BaseReuseDatabaseTest {

  @Autowired
  AuditDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    Audit arc1 = getAudit1();

    Audit arc2 = getAudit2();

    dao.save(arc1);
    assertNotNull(arc1.getID());
    assertEquals(1, count());

    dao.save(arc2);
    assertNotNull(arc2.getID());
    assertEquals(2, count());

    Audit foundById1 = dao.findById(arc1.getID()).get();
    assertEquals(arc1.getID(), foundById1.getID());

    Audit foundById2 = dao.findById(arc2.getID()).get();
    assertEquals(arc2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    Audit arc1 = getAudit1();

    Audit arc2 = getAudit2();

    dao.save(arc1);
    assertEquals(1, count());

    dao.save(arc2);
    assertEquals(2, count());

    List<Audit> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(arc1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(arc2.getID())).count());
  }


  @Test
  void testUpdate() {
    Audit arc1 = getAudit1();

    dao.save(arc1);

    arc1.setNote("updated-not");

    dao.update(arc1);

    Audit found = dao.findById(arc1.getID()).get();
    assertEquals(arc1.getNote(), found.getNote());
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
    Audit audit = new Audit();
    audit.setNote("note-one");
    return audit;
  }

  private Audit getAudit2() {
    Audit audit = new Audit();
    audit.setNote("note-two");
    return audit;
  }

  long count() {
    return dao.count();
  }

}
