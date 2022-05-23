package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.Archive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
public class ArchiveDAOTest extends BaseReuseDatabaseTest {

  @Autowired
  ArchiveDAO dao;

  @Autowired
  JdbcTemplate template;

  Date now = new Date();

  @Test
  void testWriteThenRead() {
    Archive arc1 = getArchive1();

    Archive arc2 = getArchive2();

    dao.save(arc1);
    assertNotNull(arc1.getArchiveId());
    assertEquals(1, count());

    dao.save(arc2);
    assertNotNull(arc2.getArchiveId());
    assertEquals(2, count());

    Archive foundById1 = dao.findById(arc1.getId());
    assertEquals(arc1.getArchiveId(), foundById1.getArchiveId());

    Archive foundById2 = dao.findById(arc2.getId());
    assertEquals(arc2.getArchiveId(), foundById2.getArchiveId());
  }

  @Test
  void testList() {
    Archive arc1 = getArchive1();

    Archive arc2 = getArchive2();

    dao.save(arc1);
    assertEquals(1, count());

    dao.save(arc2);
    assertEquals(2, count());

    List<Archive> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1,items.stream().filter(dr -> dr.getId().equals(arc1.getId())).count());
    assertEquals(1,items.stream().filter(dr -> dr.getId().equals(arc2.getId())).count());
  }


  @Test
  void testUpdate() {
    Archive arc1 = getArchive1();

    dao.save(arc1);

    arc1.setArchiveId("updated-archive-id-1");

    dao.update(arc1);

    Archive found = dao.findById(arc1.getId());
    assertEquals(arc1.getArchiveId(), found.getArchiveId());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Archives`");
    assertEquals(0, count());
  }

  private Archive getArchive1() {
    Archive archive = new Archive();
    archive.setArchiveId("arc1-arc2");
    archive.setCreationTime(now);
    return archive;
  }

  private Archive getArchive2() {
    Archive archive = new Archive();
    archive.setArchiveId("user2-user2");
    archive.setCreationTime(now);
    return archive;
  }

  int count() {
    return template.queryForObject(
        "select count(*) from Archives", Integer.class);
  }

}
