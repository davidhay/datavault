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
    BillingInfo billingInfo1 = getBillingInfo1(v1);

    BillingInfo billingInfo2 = getBillingInfo2(v2);

    dao.save(billingInfo1);
    assertNotNull(billingInfo1.getID());
    assertEquals(1, count());

    dao.save(billingInfo2);
    assertNotNull(billingInfo2.getID());
    assertEquals(2, count());

    BillingInfo foundById1 = dao.findById(billingInfo1.getID()).get();
    assertEquals(billingInfo1.getID(), foundById1.getID());

    BillingInfo foundById2 = dao.findById(billingInfo2.getID()).get();
    assertEquals(billingInfo2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    BillingInfo billingInfo1 = getBillingInfo1(v1);

    BillingInfo billingInfo2 = getBillingInfo2(v2);

    dao.save(billingInfo1);
    assertNotNull(billingInfo1.getID());
    assertEquals(1, count());

    dao.save(billingInfo2);
    assertNotNull(billingInfo2.getID());
    assertEquals(2, count());

    List<BillingInfo> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(billingInfo1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(billingInfo2.getID())).count());
  }


  @Test
  void testUpdate() {
    BillingInfo billingInfo = getBillingInfo1(v1);

    dao.save(billingInfo);

    billingInfo.setSpecialComments("111-updated");

    dao.update(billingInfo);

    BillingInfo found = dao.findById(billingInfo.getID()).get();
    assertEquals(billingInfo.getSpecialComments(), found.getSpecialComments());
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
    BillingInfo billingInfo = new BillingInfo();
    billingInfo.setSpecialComments("111");
    billingInfo.setBillingType(Billing_Type.BUDGET_CODE);

    billingInfo.setVault(vault);
    vault.setBillinginfo(billingInfo);
    return billingInfo;
  }

  private BillingInfo getBillingInfo2(Vault vault) {
    BillingInfo billingInfo = new BillingInfo();
    billingInfo.setSpecialComments("222");
    billingInfo.setBillingType(Billing_Type.GRANT_FUNDING);

    billingInfo.setVault(vault);
    vault.setBillinginfo(billingInfo);
    return billingInfo;
  }

  long count() {
    return dao.count();
  }
}
