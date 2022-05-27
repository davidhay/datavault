package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.Dataset;
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
public class DatasetDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  DatasetDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    Dataset dataset1 = getDataset1();

    Dataset dataset2 = getDataset2();

    dao.save(dataset1);
    assertNotNull(dataset1.getID());
    assertEquals(1, count());

    dao.save(dataset2);
    assertNotNull(dataset2.getID());
    assertEquals(2, count());

    Dataset foundById1 = dao.findById(dataset1.getID()).get();
    assertEquals(dataset1.getID(), foundById1.getID());

    Dataset foundById2 = dao.findById(dataset2.getID()).get();
    assertEquals(dataset2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    Dataset archive1 = getDataset1();

    Dataset archive2 = getDataset2();

    dao.save(archive1);
    assertNotNull(archive1.getID());
    assertEquals(1, count());

    dao.save(archive2);
    assertNotNull(archive2.getID());
    assertEquals(2, count());

    List<Dataset> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(archive1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(archive2.getID())).count());
  }


  @Test
  void testUpdate() {
    Dataset dataset = getDataset1();

    dao.save(dataset);

    dataset.setName("111-updated");

    dao.update(dataset);

    Dataset found = dao.findById(dataset.getID()).get();
    assertEquals(dataset.getName(), found.getName());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Datasets`");
    assertEquals(0, count());
  }

  private Dataset getDataset1() {
    Dataset ret = new Dataset();
    ret.setID("ds-1");
    ret.setName("111");
    ret.setCrisId("crs-id-1");
    return ret;
  }

  private Dataset getDataset2() {
    Dataset ret = new Dataset();
    ret.setID("ds-2");
    ret.setName("222");
    ret.setCrisId("crs-id-2");
    return ret;
  }

  long count() {
    return dao.count();
  }
}
