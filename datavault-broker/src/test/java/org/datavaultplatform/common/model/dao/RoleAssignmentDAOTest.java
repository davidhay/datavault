package org.datavaultplatform.common.model.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.RoleAssignment;
import org.datavaultplatform.common.model.dao.RoleAssignmentDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
@Disabled
public class RoleAssignmentDAOTest extends BaseReuseDatabaseTest {

  @Autowired
  RoleAssignmentDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    RoleAssignment review1 = getRoleAssignment1();

    RoleAssignment review2 = getRoleAssignment2();

    dao.save(review1);
    assertNotNull(review1.getId());
    assertEquals(1, count());

    dao.save(review2);
    assertNotNull(review2.getId());
    assertEquals(2, count());

    RoleAssignment foundById1 = dao.findById(review1.getId()).get();
    assertEquals(review1.getId(), foundById1.getId());

    RoleAssignment foundById2 = dao.findById(review2.getId()).get();
    assertEquals(review2.getId(), foundById2.getId());
  }

  @Test
  void testList() {
    Assertions.assertThrows(UnsupportedOperationException.class, () -> dao.list());
  }

  @Test
  void testUpdate() {
    RoleAssignment arc1 = getRoleAssignment1();

    dao.save(arc1);

    arc1.setUserId("updated-user-id");

    dao.update(arc1);

    RoleAssignment found = dao.findById(arc1.getId()).get();
    assertEquals(arc1.getUserId(), found.getUserId());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Role_assignments`");
    assertEquals(0, count());
  }

  private RoleAssignment getRoleAssignment1() {
    RoleAssignment result = new RoleAssignment();
    result.setUserId("user-id-1");
    return result;
  }

  private RoleAssignment getRoleAssignment2() {
    RoleAssignment result = new RoleAssignment();
    result.setUserId("user-id-2");
    return result;
  }

  int count() {
     return template.queryForObject(
          "select count(*) from Role_assignments", Integer.class);
  }
}
