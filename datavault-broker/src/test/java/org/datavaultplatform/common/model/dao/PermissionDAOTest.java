package org.datavaultplatform.common.model.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.PermissionModel;
import org.datavaultplatform.common.model.PermissionModel.PermissionType;
import org.datavaultplatform.common.model.dao.PermissionDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
@Import(MockServicesConfig.class)
public class PermissionDAOTest extends BaseDatabaseTest {

  @Autowired
  PermissionDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    PermissionModel permissionModel1 = getPermissionModel1();

    PermissionModel permissionModel2 = getPermissionModel2();

    dao.save(permissionModel1);
    assertNotNull(permissionModel1.getId());
    assertEquals(1, count());

    dao.save(permissionModel2);
    assertNotNull(permissionModel2.getId());
    assertEquals(2, count());

    PermissionModel foundById1 = dao.findById(permissionModel1.getId()).get();
    assertEquals(permissionModel1.getId(), foundById1.getId());

    PermissionModel foundById2 = dao.findById(permissionModel2.getId()).get();
    assertEquals(permissionModel2.getId(), foundById2.getId());
  }

  @Test
  void testList() {
    PermissionModel audit1 = getPermissionModel1();

    PermissionModel audit2 = getPermissionModel2();

    dao.save(audit1);
    assertNotNull(audit1.getId());
    assertEquals(1, count());

    dao.save(audit2);
    assertNotNull(audit2.getId());
    assertEquals(2, count());

    List<PermissionModel> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(audit1.getId())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(audit2.getId())).count());
  }


  @Test
  void testUpdate() {
    PermissionModel audit1 = getPermissionModel1();

    dao.save(audit1);

    audit1.setLabel("111-updated");

    dao.update(audit1);

    PermissionModel found = dao.findById(audit1.getId()).get();
    assertEquals(audit1.getLabel(), found.getLabel());
  }


  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Permissions`");
    assertEquals(0, count());
  }

  private PermissionModel getPermissionModel1() {
    PermissionModel result = new PermissionModel();
    result.setId("CLIENT-1");
    result.setLabel("LABEL-1");
    result.setPermission(Permission.CAN_MANAGE_VAULTS);
    result.setType(PermissionType.VAULT);
    return result;
  }

  private PermissionModel getPermissionModel2() {
    PermissionModel result = new PermissionModel();
    result.setId("CLIENT-2");
    result.setLabel("LABEL-2");
    result.setPermission(Permission.CAN_MANAGE_DEPOSITS);
    result.setType(PermissionType.SCHOOL);
    return result;
  }

  long count() {
    return dao.count();
  }
}
