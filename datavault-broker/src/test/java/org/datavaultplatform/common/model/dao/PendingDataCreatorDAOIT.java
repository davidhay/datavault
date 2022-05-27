package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.PendingDataCreator;
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
public class PendingDataCreatorDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  PendingDataCreatorDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    PendingDataCreator pdc1 = getPendingDataCreator1();

    PendingDataCreator pdc2 = getPendingDataCreator2();

    dao.save(pdc1);
    assertNotNull(pdc1.getId());
    assertEquals(1, count());

    dao.save(pdc2);
    assertNotNull(pdc2.getId());
    assertEquals(2, count());

    PendingDataCreator foundById1 = dao.findById(pdc1.getId()).get();
    assertEquals(pdc1.getId(), foundById1.getId());

    PendingDataCreator foundById2 = dao.findById(pdc2.getId()).get();
    assertEquals(pdc2.getId(), foundById2.getId());
  }

  @Test
  void testList() {
    PendingDataCreator archive1 = getPendingDataCreator1();

    PendingDataCreator archive2 = getPendingDataCreator2();

    dao.save(archive1);
    assertNotNull(archive1.getId());
    assertEquals(1, count());

    dao.save(archive2);
    assertNotNull(archive2.getId());
    assertEquals(2, count());

    List<PendingDataCreator> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(archive1.getId())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(archive2.getId())).count());
  }


  @Test
  void testUpdate() {
    PendingDataCreator pdc = getPendingDataCreator1();

    dao.save(pdc);

    pdc.setName("111-updated");

    dao.update(pdc);

    PendingDataCreator found = dao.findById(pdc.getId()).get();
    assertEquals(pdc.getName(), found.getName());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `PendingDataCreators`");
    assertEquals(0, count());
  }

  private PendingDataCreator getPendingDataCreator1() {
    PendingDataCreator pdc = new PendingDataCreator();
    pdc.setName("111");
    return pdc;
  }

  private PendingDataCreator getPendingDataCreator2() {
    PendingDataCreator pdc = new PendingDataCreator();
    pdc.setName("222");
    return pdc;
  }

  long count() {
    return dao.count();
  }

  @Test
  void testSaveList(){
    PendingDataCreator pdc1 = getPendingDataCreator1();

    PendingDataCreator pdc2 = getPendingDataCreator2();

    dao.save(Arrays.asList(pdc1,pdc2));

    assertNotNull(pdc1.getId());
    assertNotNull(pdc2.getId());
    assertEquals(2, count());

    PendingDataCreator found1 = dao.findById(pdc1.getId()).get();
    PendingDataCreator found2 = dao.findById(pdc2.getId()).get();

    assertEquals(pdc1.getName(), found1.getName());
    assertEquals(pdc2.getName(), found2.getName());
  }
}
