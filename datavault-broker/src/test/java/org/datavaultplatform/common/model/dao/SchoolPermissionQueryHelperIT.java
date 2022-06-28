package org.datavaultplatform.common.model.dao;


import static org.datavaultplatform.broker.test.TestUtils.NOW;
import static org.datavaultplatform.broker.test.TestUtils.ONE_WEEK_AGO;
import static org.datavaultplatform.broker.test.TestUtils.TWO_WEEKS_AGO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.common.model.Dataset;
import org.datavaultplatform.common.model.Deposit;
import org.datavaultplatform.common.model.Group;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.Vault;
import org.datavaultplatform.common.model.dao.old.VaultOldDAO;
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
public class SchoolPermissionQueryHelperIT extends BaseDatabaseTest {

  Vault v1;
  Vault v2;
  Vault v3;
  //uses QueryHelper
  @Autowired
  VaultDAO vaultDAO;

  @Autowired
  GroupDAO groupDAO;

  @Autowired
  DatasetDAO datasetDAO;

  //uses session.createCriteroa
  @Autowired
  VaultOldDAO oldVaultDAO;

  @Test
  void testQueryHelperVSoldCriteria() {
    log.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    List<Vault> vaultsByNameAsc = vaultDAO.search("allowed", "name-", "name", "asc", "1","2");
    log.info("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");

    //String SQL_V1 = SQLAppender.SQL_TL.get();
    assertEquals(3, vaultsByNameAsc.size());
    assertEquals(v3, vaultsByNameAsc.get(0));
    assertEquals(v2, vaultsByNameAsc.get(1));
    assertEquals(v1, vaultsByNameAsc.get(2));

    //String oldSQL = SQLAppender.SQL_TL.get();
    //assertEquals(2, oldVaultDAO.search("allowed", null, "crisID", "asc", "1", "2").size());
    //String oldSQL = SQLAppender.SQL_TL.get();
    //SQLAppender.SQL_TL.set(null);

    //assertEquals(2, vaultDAO   .search("allowed", null, "crisID", "asc", "1", "2").size());
    //String newSQL = SQLAppender.SQL_TL.get();
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

  @BeforeEach
  void setup() {
    Dataset ds1 = new Dataset();
    ds1.setID("dataset-1");
    ds1.setName("dataset-1");
    ds1.setCrisId("crisId1");
    ds1.setVisible(true);

    Dataset ds2 = new Dataset();
    ds2.setID("dataset-2");
    ds2.setName("dataset-2");
    ds2.setCrisId("crisId2");
    ds2.setVisible(true);

    datasetDAO.save(ds1);
    datasetDAO.save(ds2);

    v1 = getVault1();
    v1.setDataset(ds1);
    v2 = getVault2();
    v2.setDataset(ds2);
    v3 = getVault3();
    v3.setDataset(ds1);

    String schoolId = "lfcs-id";

    Group group = new Group();
    group.setID(schoolId);
    group.setName("LFCS");
    group.setEnabled(true);
    groupDAO.save(group);

    v1.setGroup(group);
    v2.setGroup(group);
    v3.setGroup(group);

    v1.setDescription("desc-for-1");
    v2.setDescription("desc-for-12");
    v3.setDescription("desc-for-123");

    v1.setName("name-Z");
    v2.setName("name-Y");
    v3.setName("name-X");

    vaultDAO.save(v1);
    vaultDAO.save(v2);
    vaultDAO.save(v3);

    assertEquals(3, vaultDAO.count());
    assertEquals(3, oldVaultDAO.count());

    createTestUser("denied1", schoolId);
    //    List<Vault> list(String userId, String sort, String order, String offset, String maxResult);
    //assertTrue(vaultDAO.search("denied1", null, "description", "asc", null,null).isEmpty());

    createTestUser("denied2", schoolId, Permission.CAN_MANAGE_DEPOSITS);
    //assertTrue(vaultDAO.search("denied2", null, "description", "asc", null,null).isEmpty());

    createTestUser("allowed", schoolId, Permission.CAN_MANAGE_VAULTS);
  }

  @Test
  void testSpecial() {
    String v1id = v1.getID();
    assertEquals(3, vaultDAO.count());
    log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    vaultDAO.special();
    log.info("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
    vaultDAO.findAll();
    log.info("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
    vaultDAO.findById(v1id);
    log.info("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
  }


}
