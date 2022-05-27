package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.DataCreator;
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
public class DataCreatorDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  DataCreatorDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    DataCreator dc1 = getDataCreator1();

    DataCreator dc2 = getDataCreator2();

    dao.save(dc1);
    assertNotNull(dc1.getId());
    assertEquals(1, count());

    dao.save(dc2);
    assertNotNull(dc2.getId());
    assertEquals(2, count());

    DataCreator foundById1 = dao.findById(dc1.getId()).get();
    assertEquals(dc1.getId(), foundById1.getId());

    DataCreator foundById2 = dao.findById(dc2.getId()).get();
    assertEquals(dc2.getId(), foundById2.getId());
  }

  @Test
  void testList() {
    DataCreator archive1 = getDataCreator1();

    DataCreator archive2 = getDataCreator2();

    dao.save(archive1);
    assertNotNull(archive1.getId());
    assertEquals(1, count());

    dao.save(archive2);
    assertNotNull(archive2.getId());
    assertEquals(2, count());

    List<DataCreator> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(archive1.getId())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(archive2.getId())).count());
  }


  @Test
  void testUpdate() {
    DataCreator archive1 = getDataCreator1();

    dao.save(archive1);

    archive1.setName("111-updated");

    dao.update(archive1);

    DataCreator found = dao.findById(archive1.getId()).get();
    assertEquals(archive1.getName(), found.getName());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `DataCreators`");
    assertEquals(0, count());
  }

  private DataCreator getDataCreator1() {
    DataCreator archive = new DataCreator();
    archive.setName("111");
    return archive;
  }

  private DataCreator getDataCreator2() {
    DataCreator archive = new DataCreator();
    archive.setName("222");
    return archive;
  }

  long count() {
    return dao.count();
  }

  @Test
  void testSaveList() {

    DataCreator dataCreator1 = getDataCreator1();

    DataCreator dataCreator2 = getDataCreator2();

    dao.save(Arrays.asList(dataCreator1, dataCreator2));

    assertNotNull(dataCreator1.getId());
    assertNotNull(dataCreator2.getId());
    assertEquals(2, count());

    DataCreator found1 = dao.findById(dataCreator1.getId()).get();
    assertEquals(dataCreator1.getName(), found1.getName());

    DataCreator found2 = dao.findById(dataCreator2.getId()).get();
    assertEquals(dataCreator2.getName(), found2.getName());
  }
}
