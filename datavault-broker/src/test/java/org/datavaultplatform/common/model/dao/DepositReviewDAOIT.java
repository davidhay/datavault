package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.DepositReview;
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
public class DepositReviewDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  DepositReviewDAO dao;

  @Autowired
  JdbcTemplate template;

  Date now = new Date();

  @Test
  void testWriteThenRead() {
    DepositReview dr1 = getDR1();

    DepositReview dr2 = getDR2();

    dao.save(dr1);
    assertNotNull(dr1.getId());
    assertEquals(1, dao.count());

    dao.save(dr2);
    assertNotNull(dr2.getId());
    assertEquals(2, dao.count());

    DepositReview foundById1 = dao.findById(dr1.getId()).get();
    assertEquals(dr1.getComment(), foundById1.getComment());

    DepositReview foundById2 = dao.findById(dr2.getId()).get();
    assertEquals(dr2.getComment(), foundById2.getComment());
  }

  @Test
  void testList() {
    DepositReview dr1 = new DepositReview();
    dr1.setComment("dr1-comment");
    dr1.setCreationTime(now);

    DepositReview dr2 = new DepositReview();
    dr2.setComment("dr2-comment");
    dr2.setCreationTime(now);

    dao.save(dr1);
    assertNotNull(dr1.getId());
    assertEquals(1, dao.count());

    dao.save(dr2);
    assertNotNull(dr2.getId());
    assertEquals(2, dao.count());

    List<DepositReview> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1,items.stream().filter(dr -> dr.getId().equals(dr1.getId())).count());
    assertEquals(1,items.stream().filter(dr -> dr.getId().equals(dr2.getId())).count());
  }

  @Test
  void testSearch() {

    DepositReview dr1 = getDR1();
    dao.save(dr1);
    assertNotNull(dr1.getId());
    assertEquals(1, dao.count());

    DepositReview dr2 = getDR2();
    dao.save(dr2);
    assertNotNull(dr2.getId());
    assertEquals(2, dao.count());

    {
      String search1 = dr1.getId().split("-")[0];
      List<DepositReview> items1 = dao.search(search1);
      assertEquals(1, items1.size());
      assertEquals(1, items1.stream().filter(dr -> dr.getId().equals(dr1.getId())).count());
    }
    {
      String search2 = dr2.getId().split("-")[0];
      List<DepositReview> items2 = dao.search(search2);
      assertEquals(1, items2.size());
      assertEquals(1, items2.stream().filter(dr -> dr.getId().equals(dr2.getId())).count());
    }
  }

  @Test
  void testUpdate() {

    DepositReview dr1 = getDR1();

    dao.save(dr1);

    dr1.setComment("dr1-comment-updated");

    DepositReview found1 = dao.findById(dr1.getId()).get();
    assertEquals("dr1-comment", found1.getComment());

    dao.update(dr1);

    DepositReview found2 = dao.findById(dr1.getId()).get();
    assertEquals("dr1-comment-updated", found2.getComment());

  }

  @BeforeEach
  void setup() {
    assertEquals(0, dao.count());
  }

  @AfterEach
  void tidyUp() {
    template.execute("delete from `DepositReviews`");
    assertEquals(0, dao.count());
  }

  DepositReview getDR1() {
    DepositReview result = new DepositReview();
    result.setComment("dr1-comment");
    result.setCreationTime(now);
    return result;
  }

  DepositReview getDR2(){
    DepositReview result = new DepositReview();
    result.setComment("dr2-comment");
    result.setCreationTime(now);
    return result;
  }
}
