package org.datavaultplatform.common.model.dao;

import static org.datavaultplatform.broker.test.TestUtils.NOW;
import static org.datavaultplatform.broker.test.TestUtils.ONE_WEEK_AGO;
import static org.datavaultplatform.broker.test.TestUtils.TWO_WEEKS_AGO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.broker.test.TestUtils;
import org.datavaultplatform.common.model.Group;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.User;
import org.datavaultplatform.common.model.Vault;
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
public class VaultDAOIT extends BaseReuseDatabaseTest {


  @Autowired
  VaultDAO dao;

  @Autowired
  UserDAO userDAO;

  @Autowired
  GroupDAO groupDAO;

  @Autowired
  RoleDAO roleDAO;

  @Autowired
  RoleAssignmentDAO roleAssignmentDAO;

  @Autowired
  PermissionDAO permissionDAO;

  @Test
  void testWriteThenRead() {
    Vault vault1 = getVault1();

    Vault vault2 = getVault2();

    dao.save(vault1);
    assertNotNull(vault1.getID());
    assertEquals(1, count());

    dao.save(vault2);
    assertNotNull(vault2.getID());
    assertEquals(2, count());

    Vault foundById1 = dao.findById(vault1.getID()).get();
    assertEquals(vault1.getID(), foundById1.getID());

    Vault foundById2 = dao.findById(vault2.getID()).get();
    assertEquals(vault2.getID(), foundById2.getID());
  }

  @Test
  void testListIsSortedByCreationTimeAscending() {
    Vault vault1 = getVault1();

    Vault vault2 = getVault2();

    Vault vault3 = getVault3();

    dao.save(vault1);
    assertEquals(1, count());

    dao.save(vault2);
    assertEquals(2, count());

    dao.save(vault3);
    assertEquals(3, count());

    List<Vault> items = dao.list();
    assertEquals(3, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(vault1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(vault2.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(vault3.getID())).count());

    // The Vaults should be ordered by Ascending Creation Time
    assertEquals(
        Arrays.asList(
            vault3.getID(),
            vault1.getID(),
            vault2.getID()),
        items.stream().map(Vault::getID).collect(Collectors.toList()));
  }


  @Test
  void testUpdate() {
    Vault vault = getVault1();

    dao.save(vault);

    vault.setName("updated-name");

    dao.update(vault);

    Vault found = dao.findById(vault.getID()).get();
    assertEquals(vault.getName(), found.getName());
  }

  @Test
  void testVaultSnapshotBLOB(){

    Vault vault = getVaultWithSnapshot();
    dao.save(vault);

    Vault found = dao.findById(vault.getID()).get();

    assertEquals(vault.getSnapshot(), found.getSnapshot());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Vaults`");
    assertEquals(0, count());
  }

  @Test
  void testGetTotalNumberOfVaults() {

    Vault v1 = getVault1();
    Vault v2 = getVault2();
    Vault v3 = getVault3();

    String schoolId = "lfcs-id";

    Group group = new Group();
    group.setID(schoolId);
    group.setName("LFCS");
    group.setEnabled(true);
    groupDAO.save(group);

    v1.setGroup(group);
    v2.setGroup(group);
    v3.setGroup(group);

    dao.save(v1);
    dao.save(v2);
    dao.save(v3);

    assertEquals(3, dao.count());
    createTestUser("denied1", schoolId);
    assertEquals(0, dao.getTotalNumberOfVaults("denied1"));

    createTestUser("allowed", schoolId, Permission.CAN_MANAGE_VAULTS );
    assertEquals(3, dao.getTotalNumberOfVaults("allowed"));

    createTestUser("denied2", schoolId, Permission.CAN_MANAGE_DEPOSITS );
    assertEquals(0, dao.getTotalNumberOfVaults("denied2"));
  }

  private User createTestUser(String userId, String schoolId, Permission... permissions){
    return TestUtils.createUserWithPermissions(userDAO, permissionDAO, roleDAO, roleAssignmentDAO,  userId, schoolId, permissions);
  }

  static  Vault getVault1() {
    Vault result = new Vault();
    result.setContact("contact-1");
    result.setName("vault-1");
    result.setReviewDate(NOW);
    result.setCreationTime(ONE_WEEK_AGO);
    return result;
  }

  static Vault getVault2() {
    Vault result = new Vault();
    result.setContact("contact-2");
    result.setName("vault-2");
    result.setReviewDate(NOW);
    result.setCreationTime(NOW);
    return result;
  }

  static Vault getVault3() {
    Vault result = new Vault();
    result.setContact("contact-2");
    result.setName("vault-2");
    result.setReviewDate(NOW);
    result.setCreationTime(TWO_WEEKS_AGO);
    return result;
  }

  static Vault getVaultWithSnapshot() {
    Vault result = new Vault();
    result.setContact("contact-3");
    result.setName("vault-3");
    result.setReviewDate(NOW);
    result.setCreationTime(NOW);
    result.setSnapshot(String.join(",", TestUtils.getRandomList()));
    return result;
  }

  long count() {
     return dao.count();
  }
}
