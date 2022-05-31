package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.common.model.RoleModel;
import org.datavaultplatform.common.model.RoleType;
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
    "broker.initialise.enabled=false",
    "broker.scheduled.enabled=false"
})
public class RoleDAOIT extends BaseDatabaseTest {

  @Autowired
  RoleDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    RoleModel RoleModel1 = getRoleModel1();

    RoleModel RoleModel2 = getRoleModel2();

    dao.save(RoleModel1);
    assertNotNull(RoleModel1.getId());
    assertEquals(1, dao.count());

    dao.save(RoleModel2);
    assertNotNull(RoleModel2.getId());
    assertEquals(2, dao.count());

    RoleModel foundById1 = dao.findById(RoleModel1.getId()).get();
    assertEquals(RoleModel1.getName(), foundById1.getName());

    RoleModel foundById2 = dao.findById(RoleModel2.getId()).get();
    assertEquals(RoleModel2.getName(), foundById2.getName());
  }

  @Test
  void testList() {
    RoleModel roleModel1 = getRoleModel1();

    RoleModel roleModel2 = getRoleModel2();

    dao.save(roleModel1);
    assertEquals(1, dao.count());

    dao.save(roleModel2);
    assertEquals(2, dao.count());

    List<RoleModel> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1,items.stream().filter(dr -> dr.getId().equals(roleModel1.getId())).count());
    assertEquals(1,items.stream().filter(dr -> dr.getId().equals(roleModel2.getId())).count());
  }


  @Test
  void testUpdate() {

    RoleModel roleModel1 = getRoleModel1();

    dao.save(roleModel1);

    roleModel1.setName("RoleModel1.UPDATED");

    RoleModel found1 = dao.findById(roleModel1.getId()).get();
    assertEquals("RoleModel1", found1.getName());

    dao.update(roleModel1);

    RoleModel found2 = dao.findById(roleModel1.getId()).get();
    assertEquals("RoleModel1.UPDATED", found2.getName());

  }

  @BeforeEach
  void setup() {
    assertEquals(0, dao.count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Roles`");
    assertEquals(0, dao.count());
  }

  public static RoleModel getRoleModel1() {
    RoleModel roleModel = new RoleModel();
    roleModel.setName("RoleModel1");
    roleModel.setStatus("STATUS1");
    roleModel.setType(RoleType.VAULT);
    return roleModel;
  }

  public static RoleModel getRoleModel2() {
    RoleModel roleModel = new RoleModel();
    roleModel.setName("RoleModel2");
    roleModel.setStatus("STATUS2");
    roleModel.setType(RoleType.SCHOOL);
    return roleModel;
  }

}
