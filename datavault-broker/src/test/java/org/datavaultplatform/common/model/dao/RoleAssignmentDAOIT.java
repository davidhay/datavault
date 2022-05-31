package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.RoleAssignment;
import org.datavaultplatform.common.model.RoleModel;
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
public class RoleAssignmentDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  RoleAssignmentDAO dao;

  @Autowired
  RoleDAO roleDAO;

  private RoleModel role1;
  private RoleModel role2;

  @Test
  void testWriteThenRead() {
    RoleAssignment roleAssignment1 = getRoleAssignment1();

    RoleAssignment roleAssignment2 = getRoleAssignment2();

    dao.save(roleAssignment1);
    assertNotNull(roleAssignment1.getId());
    assertEquals(1, count());

    dao.save(roleAssignment2);
    assertNotNull(roleAssignment2.getId());
    assertEquals(2, count());

    RoleAssignment foundById1 = dao.findById(roleAssignment1.getId()).get();
    assertEquals(roleAssignment1.getId(), foundById1.getId());

    RoleAssignment foundById2 = dao.findById(roleAssignment2.getId()).get();
    assertEquals(roleAssignment2.getId(), foundById2.getId());
  }

  @Test
  void testList() {
    RoleAssignment ra1 = getRoleAssignment1();

    RoleAssignment ra2 = getRoleAssignment2();

    dao.save(ra1);
    assertEquals(1, dao.count());

    dao.save(ra2);
    assertEquals(2, dao.count());

    List<RoleAssignment> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1,items.stream().filter(dr -> dr.getId().equals(ra1.getId())).count());
    assertEquals(1,items.stream().filter(dr -> dr.getId().equals(ra2.getId())).count());

  }

  @Test
  void testUpdate() {
    RoleAssignment roleAssignment1 = getRoleAssignment1();

    dao.save(roleAssignment1);

    roleAssignment1.setUserId("updated-user-id");

    dao.update(roleAssignment1);

    RoleAssignment found = dao.findById(roleAssignment1.getId()).get();
    assertEquals(roleAssignment1.getUserId(), found.getUserId());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Role_permissions`");
    template.execute("delete from `Role_assignments`");
    template.execute("delete from `Roles`");
    assertEquals(0, count());
  }

  private RoleAssignment getRoleAssignment1() {
    RoleAssignment result = new RoleAssignment();
    result.setUserId("user-id-1");
    result.setRole(role1);
    return result;
  }

  private RoleAssignment getRoleAssignment2() {
    RoleAssignment result = new RoleAssignment();
    result.setUserId("user-id-2");
    result.setRole(role2);
    return result;
  }

  long count() {
     return dao.count();
  }

  @BeforeEach
  void beforeEach() {
    this.role1 = roleDAO.save(RoleDAOIT.getRoleModel1());
    this.role2 = roleDAO.save(RoleDAOIT.getRoleModel2());
  }
}
