package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.RetentionPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    "broker.initialise.enabled=false",
    "broker.scheduled.enabled=false"
})
@Disabled
public class RetentionPolicyDAOTest extends BaseReuseDatabaseTest {

  @Autowired
  RetentionPolicyDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    RetentionPolicy retPol1 = getRetentionPolicy1();

    RetentionPolicy retPol2 = getRetentionPolicy2();

    dao.save(retPol1);
    assertNotNull(retPol1.getID());
    assertEquals(1, count());

    dao.save(retPol2);
    assertNotNull(retPol2.getID());
    assertEquals(2, count());

    RetentionPolicy foundById1 = dao.findById(retPol1.getID()).get();
    assertEquals(retPol1.getID(), foundById1.getID());

    RetentionPolicy foundById2 = dao.findById(retPol2.getID()).get();
    assertEquals(retPol2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    RetentionPolicy rp1 = getRetentionPolicy1();

    RetentionPolicy rp2 = getRetentionPolicy2();

    dao.save(rp1);
    assertEquals(1, count());

    dao.save(rp2);
    assertEquals(2, count());

    List<RetentionPolicy> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(rp -> rp.getID() == rp1.getID()).count());
    assertEquals(1, items.stream().filter(rp -> rp.getID() == rp2.getID()).count());
  }

  @Test
  void testUpdate() {
    RetentionPolicy rp1 = getRetentionPolicy1();

    dao.save(rp1);

    rp1.setName("updated-name");

    dao.update(rp1);

    RetentionPolicy found = dao.findById(rp1.getID()).get();
    assertEquals(rp1.getName(), found.getName());
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
    RetentionPolicy result = new RetentionPolicy();
    result.setEngine("engine-1");
    result.setName("ret-pol-1");
    return result;
  }

  private RetentionPolicy getRetentionPolicy2() {
    RetentionPolicy result = new RetentionPolicy();
    result.setName("ret-pol-2");
    result.setEngine("engine-2");
    return result;
  }

  long count() {
    return template.queryForObject(
          "select count(*) from RetentionPolicies", Long.class);
  }
}
