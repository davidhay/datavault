package org.datavaultplatform.common.model.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.RetentionPolicy;
import org.datavaultplatform.common.model.dao.RetentionPolicyDAO;
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
public class RetentionPolicyDAOTest extends BaseReuseDatabaseTest {

  @Autowired
  RetentionPolicyDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    RetentionPolicy retentionPolicy1 = getRetentionPolicy1();

    RetentionPolicy retentionPolicy2 = getRetentionPolicy2();

    dao.save(retentionPolicy1);
    assertNotNull(retentionPolicy1.getID());
    assertEquals(1, count());

    dao.save(retentionPolicy2);
    assertNotNull(retentionPolicy2.getID());
    assertEquals(2, count());

    RetentionPolicy foundById1 = dao.findById(retentionPolicy1.getID()).get();
    assertEquals(retentionPolicy1.getID(), foundById1.getID());

    RetentionPolicy foundById2 = dao.findById(retentionPolicy2.getID()).get();
    assertEquals(retentionPolicy2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    RetentionPolicy archive1 = getRetentionPolicy1();

    RetentionPolicy archive2 = getRetentionPolicy2();

    dao.save(archive1);
    assertNotNull(archive1.getID());
    assertEquals(1, count());

    dao.save(archive2);
    assertNotNull(archive2.getID());
    assertEquals(2, count());

    List<RetentionPolicy> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID() == archive1.getID()).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID() == archive2.getID()).count());
  }


  @Test
  void testUpdate() {
    RetentionPolicy retentionPolicy = getRetentionPolicy1();

    dao.save(retentionPolicy);

    retentionPolicy.setName("111-updated");

    dao.update(retentionPolicy);

    RetentionPolicy found = dao.findById(retentionPolicy.getID()).get();
    assertEquals(retentionPolicy.getName(), found.getName());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `RetentionPolicies`");
    assertEquals(0, count());
  }

  private RetentionPolicy getRetentionPolicy1() {
    RetentionPolicy ret = new RetentionPolicy();
    ret.setEngine("engine-1");
    ret.setName("111");
    return ret;
  }

  private RetentionPolicy getRetentionPolicy2() {
    RetentionPolicy ret = new RetentionPolicy();
    ret.setName("222");
    ret.setEngine("engine-2");
    return ret;
  }

  long count() {
    return dao.count();
  }
}
