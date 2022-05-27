package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.common.model.ArchiveStore;
import org.datavaultplatform.common.model.dao.ArchiveStoreDAO;
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
public class ArchiveStoreDAOTest extends BaseDatabaseTest {

  @Autowired
  ArchiveStoreDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    ArchiveStore as1 = getArchiveStore1();

    ArchiveStore as2 = getArchiveStore2();

    dao.save(as1);
    assertNotNull(as1.getID());
    assertEquals(1, count());

    dao.save(as2);
    assertNotNull(as2.getID());
    assertEquals(2, count());

    ArchiveStore foundById1 = dao.findById(as1.getID()).get();
    assertEquals(as1.getID(), foundById1.getID());

    ArchiveStore foundById2 = dao.findById(as2.getID()).get();
    assertEquals(as2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    ArchiveStore ArchiveStore1 = getArchiveStore1();

    ArchiveStore ArchiveStore2 = getArchiveStore2();

    dao.save(ArchiveStore1);
    assertNotNull(ArchiveStore1.getID());
    assertEquals(1, count());

    dao.save(ArchiveStore2);
    assertNotNull(ArchiveStore2.getID());
    assertEquals(2, count());

    List<ArchiveStore> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(ArchiveStore1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(ArchiveStore2.getID())).count());
  }


  @Test
  void testUpdate() {
    ArchiveStore archiveStore = getArchiveStore1();

    dao.save(archiveStore);

    archiveStore.setLabel("111-updated");

    dao.update(archiveStore);

    ArchiveStore found = dao.findById(archiveStore.getID()).get();
    assertEquals(archiveStore.getLabel(), found.getLabel());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `ArchiveStores`");
    assertEquals(0, count());
  }

  private ArchiveStore getArchiveStore1() {
    ArchiveStore archiveStore = new ArchiveStore();
    archiveStore.setLabel("111");
    archiveStore.setRetrieveEnabled(false);
    return archiveStore;
  }

  private ArchiveStore getArchiveStore2() {
    ArchiveStore archiveStore = new ArchiveStore();
    archiveStore.setLabel("222");
    archiveStore.setRetrieveEnabled(true);
    return archiveStore;
  }

  long count() {
    return dao.count();
  }

  @Test
  void testFindForRetrieval(){

    ArchiveStore as1 = getArchiveStore1();

    ArchiveStore as2 = getArchiveStore2();

    dao.save(as1);
    assertNotNull(as1.getID());
    assertEquals(1, count());

    dao.save(as2);
    assertNotNull(as2.getID());
    assertEquals(2, count());

    ArchiveStore result = dao.findForRetrieval();
    assertEquals(as2.getID(), result.getID());
  }
}
