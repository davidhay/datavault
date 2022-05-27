package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.VaultReview;
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
public class VaultReviewDAOIT extends BaseReuseDatabaseTest {

  Date now = new Date();

  @Autowired
  VaultReviewDAO dao;

  @Autowired
  JdbcTemplate template;

  @Test
  void testWriteThenRead() {
    VaultReview review1 = getVaultReview1();

    VaultReview review2 = getVaultReview2();

    dao.save(review1);
    assertNotNull(review1.getId());
    assertEquals(1, count());

    dao.save(review2);
    assertNotNull(review2.getId());
    assertEquals(2, count());

    VaultReview foundById1 = dao.findById(review1.getId()).get();
    assertEquals(review1.getId(), foundById1.getId());

    VaultReview foundById2 = dao.findById(review2.getId()).get();
    assertEquals(review2.getId(), foundById2.getId());
  }

  @Test
  void testList() {
    VaultReview arc1 = getVaultReview1();

    VaultReview arc2 = getVaultReview2();

    dao.save(arc1);
    assertEquals(1, count());

    dao.save(arc2);
    assertEquals(2, count());

    List<VaultReview> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(arc1.getId())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getId().equals(arc2.getId())).count());
  }


  @Test
  void testUpdate() {
    VaultReview arc1 = getVaultReview1();

    dao.save(arc1);

    arc1.setComment("updated-review-one");

    dao.update(arc1);

    VaultReview found = dao.findById(arc1.getId()).get();
    assertEquals(arc1.getComment(), found.getComment());
  }

  @BeforeEach
  void setup() {
    assertEquals(0, count());
  }

  @AfterEach
  void cleanup() {
    template.execute("delete from `VaultReviews`");
    assertEquals(0, count());
  }

  private VaultReview getVaultReview1() {
    VaultReview result = new VaultReview();
    result.setComment("review-1");
    result.setCreationTime(now);
    return result;
  }

  private VaultReview getVaultReview2() {
    VaultReview result = new VaultReview();
    result.setComment("review-2");
    result.setCreationTime(now);
    return result;
  }

  long count() {
    return dao.count();
  }

}
