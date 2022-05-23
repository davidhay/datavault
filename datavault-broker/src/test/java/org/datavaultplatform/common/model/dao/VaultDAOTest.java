package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.Vault;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    "broker.scheduled.enabled=false"
})
public class VaultDAOTest extends BaseReuseDatabaseTest {

  Date now = new Date();

  @Autowired
  VaultDAO dao;

  @Autowired
  JdbcTemplate template;

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

    Vault foundById1 = dao.findById(review1.getID());
    assertEquals(review1.getID(), foundById1.getID());

    Vault foundById2 = dao.findById(review2.getID());
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

    Vault found = dao.findById(arc1.getID());
    assertEquals(arc1.getName(), found.getName());
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

  private Vault getVault1() {
    Vault result = new Vault();
    result.setContact("contact-1");
    result.setName("vault-1");
    result.setReviewDate(now);
    result.setCreationTime(now);
    return result;
  }

  private Vault getVault2() {
    Vault result = new Vault();
    result.setContact("contact-2");
    result.setName("vault-2");
    result.setReviewDate(now);
    result.setCreationTime(now);
    return result;
  }

  int count() {
     return template.queryForObject(
          "select count(*) from Vaults", Integer.class);
  }
}
