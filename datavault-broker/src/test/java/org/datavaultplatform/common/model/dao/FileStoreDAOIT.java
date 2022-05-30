package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.FileStore;
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
public class FileStoreDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  FileStoreDAO dao;

  @Test
  void testWriteThenRead() {
    FileStore fs1 = getFileStore1();

    FileStore fs2 = getFileStore2();

    dao.save(fs1);
    assertNotNull(fs1.getID());
    assertEquals(1, count());

    dao.save(fs2);
    assertNotNull(fs2.getID());
    assertEquals(2, count());

    FileStore foundById1 = dao.findById(fs1.getID()).get();
    assertEquals(fs1.getID(), foundById1.getID());

    FileStore foundById2 = dao.findById(fs2.getID()).get();
    assertEquals(fs2.getID(), foundById2.getID());
  }

  @Test
  void testList() {
    FileStore archive1 = getFileStore1();

    FileStore archive2 = getFileStore2();

    dao.save(archive1);
    assertNotNull(archive1.getID());
    assertEquals(1, count());

    dao.save(archive2);
    assertNotNull(archive2.getID());
    assertEquals(2, count());

    List<FileStore> items = dao.findAll();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(archive1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(archive2.getID())).count());
  }


  @Test
  void testUpdate() {
    FileStore archive1 = getFileStore1();

    dao.save(archive1);

    archive1.setLabel("111-updated");

    dao.update(archive1);

    FileStore found = dao.findById(archive1.getID()).get();
    assertEquals(archive1.getLabel(), found.getLabel());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `FileStores`");
    assertEquals(0, count());
  }

  private FileStore getFileStore1() {
    FileStore archive = new FileStore();
    archive.setLabel("111");
    return archive;
  }

  private FileStore getFileStore2() {
    FileStore archive = new FileStore();
    archive.setLabel("222");
    return archive;
  }

  long count() {
    return dao.count();
  }
}
