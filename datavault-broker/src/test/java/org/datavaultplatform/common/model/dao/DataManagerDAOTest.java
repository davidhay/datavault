package org.datavaultplatform.common.model.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.DataManager;
import org.datavaultplatform.common.model.dao.DataManagerDAO;
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
public class DataManagerDAOTest extends BaseReuseDatabaseTest {

  @Autowired
  DataManagerDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    DataManager dm1 = getDataManager1();

    DataManager dm2 = getDataManager2();

    dao.save(dm1);
    assertNotNull(dm1.getID());
    assertEquals(1, count());

    dao.save(dm2);
    assertNotNull(dm2.getID());
    assertEquals(2, count());

    DataManager foundById1 = dao.findById(dm1.getID()).get();
    assertEquals(dm1.getID(), foundById1.getID());

    DataManager foundById2 = dao.findById(dm2.getID()).get();
    assertEquals(dm2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    DataManager dm1 = getDataManager1();

    DataManager dm2 = getDataManager2();

    dao.save(dm1);
    assertNotNull(dm1.getID());
    assertEquals(1, count());

    dao.save(dm2);
    assertNotNull(dm2.getID());
    assertEquals(2, count());

    List<DataManager> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(dm1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(dm2.getID())).count());
  }


  @Test
  void testUpdate() {
    DataManager dm1 = getDataManager1();

    dao.save(dm1);

    dm1.setUUN("111-updated");

    dao.update(dm1);

    DataManager found = dao.findById(dm1.getID()).get();
    assertEquals(dm1.getUUN(), found.getUUN());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `DataManagers`");
    assertEquals(0, count());
  }

  private DataManager getDataManager1() {
    DataManager dm = new DataManager();
    dm.setUUN("111");
    return dm;
  }

  private DataManager getDataManager2() {
    DataManager dm = new DataManager();
    dm.setUUN("222");
    return dm;
  }

  long count() {
    return dao.count();
  }
}
