package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.BillingInfo;
import org.datavaultplatform.common.model.PendingVault.Billing_Type;
import org.datavaultplatform.common.model.Vault;
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
public class BillingDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  BillingDAO dao;

  @Autowired
  VaultDAO vaultDAO;

  Vault v1;
  Vault v2;

  @Test
  void testWriteThenRead() {
    BillingInfo BillingInfo1 = getBillingInfo1(v1);

    BillingInfo BillingInfo2 = getBillingInfo2(v2);

    dao.save(BillingInfo1);
    assertNotNull(BillingInfo1.getID());
    assertEquals(1, count());

    dao.save(BillingInfo2);
    assertNotNull(BillingInfo2.getID());
    assertEquals(2, count());

    BillingInfo foundById1 = dao.findById(BillingInfo1.getID()).get();
    assertEquals(BillingInfo1.getID(), foundById1.getID());

    BillingInfo foundById2 = dao.findById(BillingInfo2.getID()).get();
    assertEquals(BillingInfo2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    BillingInfo archive1 = getBillingInfo1(v1);

    BillingInfo archive2 = getBillingInfo2(v2);

    dao.save(archive1);
    assertNotNull(archive1.getID());
    assertEquals(1, count());

    dao.save(archive2);
    assertNotNull(archive2.getID());
    assertEquals(2, count());

    List<BillingInfo> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(archive1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(archive2.getID())).count());
  }


  @Test
  void testUpdate() {
    BillingInfo BillingInfo = getBillingInfo1(v1);

    dao.save(BillingInfo);

    BillingInfo.setSpecialComments("111-updated");

    dao.update(BillingInfo);

    BillingInfo found = dao.findById(BillingInfo.getID()).get();
    assertEquals(BillingInfo.getSpecialComments(), found.getSpecialComments());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
    v1 = vaultDAO.save(VaultDAOIT.getVault1());
    v2 = vaultDAO.save(VaultDAOIT.getVault2());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `BillingInfo`");
    template.execute("delete from `Vaults`");
    assertEquals(0, count());
  }

  private BillingInfo getBillingInfo1(Vault vault) {
    BillingInfo archive = new BillingInfo();
    archive.setSpecialComments("111");
    archive.setBillingType(Billing_Type.BUDGET_CODE);

    archive.setVault(vault);
    vault.setBillinginfo(archive);
    return archive;
  }

  private BillingInfo getBillingInfo2(Vault vault) {
    BillingInfo archive = new BillingInfo();
    archive.setSpecialComments("222");
    archive.setBillingType(Billing_Type.GRANT_FUNDING);

    archive.setVault(vault);
    vault.setBillinginfo(archive);
    return archive;
  }

  long count() {
    return dao.count();
  }
}
