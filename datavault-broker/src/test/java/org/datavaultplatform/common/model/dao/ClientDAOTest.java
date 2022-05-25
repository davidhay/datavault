package org.datavaultplatform.common.model.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.Client;
import org.datavaultplatform.common.model.dao.ClientDAO;
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
public class ClientDAOTest extends BaseReuseDatabaseTest {

  @Autowired
  ClientDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    Client Client1 = getClient1();

    Client Client2 = getClient2();

    dao.save(Client1);
    assertNotNull(Client1.getId());
    assertEquals(1, count());

    dao.save(Client2);
    assertNotNull(Client2.getId());
    assertEquals(2, count());

    Client foundById1 = dao.findById(Client1.getId()).get();
    assertEquals(Client1.getId(), foundById1.getId());

    Client foundById2 = dao.findById(Client2.getId()).get();
    assertEquals(Client2.getId(), foundById2.getId());
  }

  @Test
  void testList() {
    Client audit1 = getClient1();

    Client audit2 = getClient2();

    dao.save(audit1);
    assertNotNull(audit1.getId());
    assertEquals(1, count());

    dao.save(audit2);
    assertNotNull(audit2.getId());
    assertEquals(2, count());

    List<Client> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(audit1.getId())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(audit2.getId())).count());
  }


  @Test
  void testUpdate() {
    Client audit1 = getClient1();

    dao.save(audit1);

    audit1.setName("111-updated");

    dao.update(audit1);

    Client found = dao.findById(audit1.getId()).get();
    assertEquals(audit1.getName(), found.getName());
  }

  @Test
  void testFindByApiKey() {

    Client client1 = getClient1();

    Client client2 = getClient2();

    dao.save(client1);
    assertNotNull(client1.getId());
    assertEquals(1, count());

    dao.save(client2);
    assertNotNull(client2.getId());
    assertEquals(2, count());

    Client apiKeyOne = dao.findByApiKey(client1.getApiKey());
    assertEquals(client1.getId(), apiKeyOne.getId());
    Client apiKeyTwo = dao.findByApiKey(client2.getApiKey());
    assertEquals(client2.getId(), apiKeyTwo.getId());

    assertNull(dao.findByApiKey("XXX"));
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `Clients`");
    assertEquals(0, count());
  }

  private Client getClient1() {
    Client result = new Client();
    result.setId("CLIENT-1");
    result.setName("111");
    result.setApiKey("ONE");
    return result;
  }

  private Client getClient2() {
    Client result = new Client();
    result.setId("CLIENT-2");
    result.setName("222");
    result.setApiKey("TWO");
    return result;
  }

  long count() {
    return dao.count();
  }
}
