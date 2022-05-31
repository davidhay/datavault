package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.broker.test.TestUtils;
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
public class VaultDAOIT extends BaseReuseDatabaseTest {

  static Date NOW = new Date();

  @Autowired
  VaultDAO dao;

  @Test
  void testWriteThenRead() {
    Vault review1 = getVault1();

    Vault review2 = getVault2();

    dao.save(review1);
    assertNotNull(review1.getID());
    assertEquals(1, count());

    dao.save(review2);
    assertNotNull(review2.getID());
    assertEquals(2, count());

    Vault foundById1 = dao.findById(review1.getID()).get();
    assertEquals(review1.getID(), foundById1.getID());

    Vault foundById2 = dao.findById(review2.getID()).get();
    assertEquals(review2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    Vault arc1 = getVault1();

    Vault arc2 = getVault2();

    dao.save(arc1);
    assertEquals(1, count());

    dao.save(arc2);
    assertEquals(2, count());

    List<Vault> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(arc1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(arc2.getID())).count());
  }


  @Test
  void testUpdate() {
    Vault arc1 = getVault1();

    dao.save(arc1);

    arc1.setName("updated-name");

    dao.update(arc1);

    Vault found = dao.findById(arc1.getID()).get();
    assertEquals(arc1.getName(), found.getName());
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

  static  Vault getVault1() {
    Vault result = new Vault();
    result.setContact("contact-1");
    result.setName("vault-1");
    result.setReviewDate(NOW);
    result.setCreationTime(NOW);
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

  static Vault getVaultWithSnapshot() {
    Vault result = new Vault();
    result.setContact("contact-3");
    result.setName("vault-3");
    result.setReviewDate(NOW);
    result.setCreationTime(NOW);
    result.setSnapshot(TestUtils.getRandomList().stream().collect(Collectors.joining(",")));
    return result;
  }

  long count() {
     return dao.count();
  }
}
