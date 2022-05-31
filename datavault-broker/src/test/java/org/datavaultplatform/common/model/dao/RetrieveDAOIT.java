package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.Retrieve;
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
public class RetrieveDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  RetrieveDAO dao;

  @Test
  void testWriteThenRead() {
    Retrieve retrieve1 = getRetrieve1();

    Retrieve retrieve2 = getRetrieve2();

    dao.save(retrieve1);
    assertNotNull(retrieve1.getID());
    assertEquals(1, count());

    dao.save(retrieve2);
    assertNotNull(retrieve2.getID());
    assertEquals(2, count());

    Retrieve foundById1 = dao.findById(retrieve1.getID()).get();
    assertEquals(retrieve1.getID(), foundById1.getID());

    Retrieve foundById2 = dao.findById(retrieve2.getID()).get();
    assertEquals(retrieve2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    Retrieve retrieve1 = getRetrieve1();

    Retrieve retrieve2 = getRetrieve2();

    dao.save(retrieve1);
    assertNotNull(retrieve1.getID());
    assertEquals(1, count());

    dao.save(retrieve2);
    assertNotNull(retrieve2.getID());
    assertEquals(2, count());

    List<Retrieve> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(retrieve1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(retrieve2.getID())).count());
  }

  @Test
  void testUpdate() {
    Retrieve retrieve1 = getRetrieve1();

    dao.save(retrieve1);

    retrieve1.setNote("updated-note");

    dao.update(retrieve1);

    Retrieve found = dao.findById(retrieve1.getID()).get();
    assertEquals(retrieve1.getNote(), found.getNote());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Retrieves`");
    assertEquals(0, count());
  }

  private Retrieve getRetrieve1() {
    Retrieve result = new Retrieve();
    result.setHasExternalRecipients(false);
    result.setNote("note-1");
     return result;
  }

  private Retrieve getRetrieve2() {
    Retrieve result = new Retrieve();
    result.setHasExternalRecipients(false);
    result.setNote("note-2");
    return result;
  }

  long count() {
    return dao.count();
  }
}
