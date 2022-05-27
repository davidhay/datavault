package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.Group;
import org.datavaultplatform.common.model.dao.GroupDAO;
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
public class GroupDAOTest extends BaseReuseDatabaseTest {

  @Autowired
  GroupDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    Group job1 = getGroup1();

    Group job2 = getGroup2();

    dao.save(job1);
    assertNotNull(job1.getID());
    assertEquals(1, count());

    dao.save(job2);
    assertNotNull(job2.getID());
    assertEquals(2, count());

    Group foundById1 = dao.findById(job1.getID()).get();
    assertEquals(job1.getID(), foundById1.getID());

    Group foundById2 = dao.findById(job2.getID()).get();
    assertEquals(job2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    Group group1 = getGroup1();

    Group group2 = getGroup2();

    dao.save(group1);
    assertNotNull(group1.getID());
    assertEquals(1, count());

    dao.save(group2);
    assertNotNull(group2.getID());
    assertEquals(2, count());

    List<Group> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(group1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(group2.getID())).count());
  }


  @Test
  void testUpdate() {
    Group job = getGroup1();

    dao.save(job);

    job.setName("111-updated");

    dao.update(job);

    Group found = dao.findById(job.getID()).get();
    assertEquals(job.getName(), found.getName());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Groups`");
    assertEquals(0, count());
  }

  private Group getGroup1() {
    Group group = new Group();
    group.setID("ID-1");
    group.setEnabled(true);
    group.setName("111");
    return group;
  }

  private Group getGroup2() {
    Group group = new Group();
    group.setID("ID-2");
    group.setEnabled(false);
    group.setName("222");
    return group;
  }

  long count() {
    return dao.count();
  }
}
