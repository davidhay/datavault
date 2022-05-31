package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.RetentionPolicy;
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
public class RetentionPolicyDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  RetentionPolicyDAO dao;

  @Test
  void testWriteThenRead() {
    RetentionPolicy retentionPolicy1 = getRetentionPolicy1();

    RetentionPolicy retentionPolicy2 = getRetentionPolicy2();

    dao.save(retentionPolicy1);
    assertTrue(retentionPolicy1.getID() > 0);
    assertEquals(1, count());

    dao.save(retentionPolicy2);
    assertTrue(retentionPolicy2.getID() > 0);
    assertEquals(2, count());

    RetentionPolicy foundById1 = dao.findById(retentionPolicy1.getID()).get();
    assertEquals(retentionPolicy1.getID(), foundById1.getID());

    RetentionPolicy foundById2 = dao.findById(retentionPolicy2.getID()).get();
    assertEquals(retentionPolicy2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    RetentionPolicy retentionPolicy1 = getRetentionPolicy1();

    RetentionPolicy retentionPolicy2 = getRetentionPolicy2();

    dao.save(retentionPolicy1);
    assertTrue(retentionPolicy1.getID() > 0);
    assertEquals(1, count());

    dao.save(retentionPolicy2);
    assertTrue(retentionPolicy2.getID() > 0);
    assertEquals(2, count());

    List<RetentionPolicy> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID() == retentionPolicy1.getID()).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID() == retentionPolicy2.getID()).count());
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
