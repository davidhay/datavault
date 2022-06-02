package org.datavaultplatform.common.model.dao;

import static org.datavaultplatform.broker.test.TestUtils.NOW;
import static org.datavaultplatform.broker.test.TestUtils.ONE_YEAR_AGO;
import static org.datavaultplatform.broker.test.TestUtils.THREE_YEARS_AGO;
import static org.datavaultplatform.broker.test.TestUtils.TWO_YEARS_AGO;
import static org.datavaultplatform.broker.test.TestUtils.datePlus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.Deposit;
import org.datavaultplatform.common.model.Deposit.Status;
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
public class DepositDAOIT extends BaseReuseDatabaseTest {

  @Autowired
  DepositDAO dao;

  //@Autowired
  //DepositChunkDAO depositChunkDAO;

  @Test
  void testWriteThenRead() {
    Deposit depositReview1 = getDeposit1();

    Deposit depositReview2 = getDeposit2();

    dao.save(depositReview1);
    assertNotNull(depositReview1.getID());
    assertEquals(1, dao.count());

    dao.save(depositReview2);
    assertNotNull(depositReview2.getID());
    assertEquals(2, dao.count());

    Deposit foundById1 = dao.findById(depositReview1.getID()).get();
    assertEquals(depositReview1.getName(), foundById1.getName());

    Deposit foundById2 = dao.findById(depositReview2.getID()).get();
    assertEquals(depositReview2.getName(), foundById2.getName());
  }

  @Test
  void testList() {
    Deposit deposit1 = new Deposit();
    deposit1.setName("dep1-name");
    deposit1.setHasPersonalData(false);
    deposit1.setCreationTime(NOW);

    Deposit deposit2 = new Deposit();
    deposit2.setHasPersonalData(true);
    deposit2.setName("dep2-name");
    deposit2.setCreationTime(NOW);

    dao.save(deposit1);
    assertNotNull(deposit1.getID());
    assertEquals(1, dao.count());

    dao.save(deposit2);
    assertNotNull(deposit2.getID());
    assertEquals(2, dao.count());

    List<Deposit> items = dao.list();
    assertEquals(2, items.size());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(deposit1.getID())).count());
    assertEquals(1, items.stream().filter(dr -> dr.getID().equals(deposit2.getID())).count());
  }



  @Test
  void testFindCompleted() {

    Deposit deposit1 = getDeposit1();
    Deposit deposit2 = getDeposit2();
    Deposit deposit3 = getDeposit3();
    Deposit deposit4 = getDeposit4();
    Deposit deposit5 = getDeposit5();
    Deposit deposit6 = getDeposit6();

    dao.save(deposit1);
    dao.save(deposit2);
    dao.save(deposit3);
    dao.save(deposit4);
    dao.save(deposit5);
    dao.save(deposit6);

    List<Deposit> items = dao.list();
    assertEquals(6, items.size());

    List<Deposit> completed = dao.completed();
    assertEquals(
        new HashSet(Arrays.asList(deposit1.getID(),deposit3.getID())),
        completed.stream().map(Deposit::getID).collect(Collectors.toSet()));
  }


  /*
  @Test
  void testSearch() {

    Deposit depositReview1 = getDeposit1();
    dao.save(depositReview1);
    assertNotNull(depositReview1.getID());
    assertEquals(1, dao.count());

    Deposit depositReview2 = getDeposit2();
    dao.save(depositReview2);
    assertNotNull(depositReview2.getID());
    assertEquals(2, dao.count());

    {
      String search1 = depositReview1.getID().split("-")[0];
      List<Deposit> items1 = dao.search(search1);
      assertEquals(1, items1.size());
      assertEquals(1, items1.stream().filter(dr -> dr.getID().equals(depositReview1.getID())).count());
    }
    {
      String search2 = depositReview2.getID().split("-")[0];
      List<Deposit> items2 = dao.search(search2);
      assertEquals(1, items2.size());
      assertEquals(1, items2.stream().filter(dr -> dr.getID().equals(depositReview2.getID())).count());
    }
  }
   */

  @Test
  void testUpdate() {

    Deposit depositReview = getDeposit1();

    dao.save(depositReview);

    depositReview.setName("dep1-name-updated");

    Deposit found1 = dao.findById(depositReview.getID()).get();
    assertEquals("dep1-name", found1.getName());

    dao.update(depositReview);

    Deposit found2 = dao.findById(depositReview.getID()).get();
    assertEquals("dep1-name-updated", found2.getName());

  }

  @Test
  void testInProgress(){
    Deposit deposit1 = getDeposit1();
    Deposit deposit2 = getDeposit2();
    Deposit deposit3 = getDeposit3();
    Deposit deposit4 = getDeposit4();
    Deposit deposit5 = getDeposit5();
    Deposit deposit6 = getDeposit6();

    dao.save(deposit1);
    dao.save(deposit2);
    dao.save(deposit3);
    dao.save(deposit4);
    dao.save(deposit5);
    dao.save(deposit6);

    List<Deposit> items = dao.list();
    assertEquals(6, items.size());

    List<Deposit> completed = dao.inProgress();
    assertEquals(
        Stream.of(deposit2,deposit4,deposit5,deposit6).map(Deposit::getID).collect(Collectors.toSet()),
        completed.stream().map(Deposit::getID).collect(Collectors.toSet()));
  }

  @Test
  void testGetDepositsWaitingForAudit() {
    Deposit deposit1 = getDeposit1();
    Deposit deposit2 = getDeposit2();
    Deposit deposit3 = getDeposit3();
    Deposit deposit4 = getDeposit4();
    Deposit deposit5 = getDeposit5();
    Deposit deposit6 = getDeposit6();
    Deposit deposit7 = getDeposit7AwaitAuditNOW();
    Deposit deposit8 = getDeposit8AwaitAudit1YearAgo();
    Deposit deposit9 = getDeposit9AwaitAudit2YearsAgo();
    Deposit deposit10 = getDeposit10AwaitAudit3YearsAgo();

    dao.save(deposit1);
    dao.save(deposit2);
    dao.save(deposit3);
    dao.save(deposit4);
    dao.save(deposit5);
    dao.save(deposit6);
    dao.save(deposit7);
    dao.save(deposit8);
    dao.save(deposit9);
    dao.save(deposit10);

    List<Deposit> items = dao.list();
    assertEquals(10, items.size());


    //check only 1 deposit that is <= Completed and <= 3 year ago
    List<Deposit> waitingAudit3 = dao.getDepositsWaitingForAudit(THREE_YEARS_AGO);
    checkSameDepositNames(waitingAudit3, deposit10);

    //check only 2 deposits that are <= Completed and <= 2 year ago
    List<Deposit> waitingAudit2 = dao.getDepositsWaitingForAudit(TWO_YEARS_AGO);
    checkSameDepositNames(waitingAudit2, deposit10, deposit9);

    //check only 3 deposits that are <= Completed and <= 1 year ago
    List<Deposit> waitingAudit1 = dao.getDepositsWaitingForAudit(ONE_YEAR_AGO);
    checkSameDepositNames(waitingAudit1, deposit10, deposit9, deposit8);

    //check 8 deposits that are <= Completed and <= NOW
    List<Deposit> waitingAudit0 = dao.getDepositsWaitingForAudit(NOW);
    checkSameDepositNames(waitingAudit0,  deposit10, deposit9, deposit8, deposit7, deposit6, deposit3, deposit2, deposit1);
  }

  void checkSameDepositNames(Collection<Deposit> actual, Deposit... expected){
    assertEquals(
        Arrays.stream(expected).map(Deposit::getName).sorted().collect(Collectors.toList()),
        actual.stream().map(Deposit::getName).sorted().collect(Collectors.toList()));
  }

  @BeforeEach
  void setup() {
    assertEquals(0, dao.count());
  }

  @AfterEach
  void tidyUp() {
    template.execute("delete from `Deposits`");
    assertEquals(0, dao.count());
  }


  Deposit getDeposit1() {
    Deposit result = new Deposit();
    result.setName("dep1-name");
    result.setCreationTime(NOW);
    result.setHasPersonalData(false);
    result.setStatus(Status.COMPLETE);
    return result;
  }

  Deposit getDeposit2(){
    Deposit result = new Deposit();
    result.setName("dep2-name");
    result.setCreationTime(NOW);
    result.setHasPersonalData(true);
    result.setStatus(Status.IN_PROGRESS);
    return result;
  }

  Deposit getDeposit3(){
    Deposit result = new Deposit();
    result.setName("dep3-name");
    result.setCreationTime(NOW);
    result.setHasPersonalData(true);
    result.setStatus(Status.COMPLETE);
    return result;
  }

  Deposit getDeposit4() {
    Deposit result = new Deposit();
    result.setName("dep4-name");
    result.setCreationTime(NOW);
    result.setHasPersonalData(true);
    result.setStatus(Status.DELETE_FAILED);
    return result;
  }

  Deposit getDeposit5() {
    Deposit result = new Deposit();
    result.setName("dep5-name");
    result.setCreationTime(NOW);
    result.setHasPersonalData(true);
    result.setStatus(Status.FAILED);
    return result;
  }

  Deposit getDeposit6() {
    Deposit result = new Deposit();
    result.setName("dep6-name");
    result.setCreationTime(NOW);
    result.setHasPersonalData(true);
    result.setStatus(Status.IN_PROGRESS);
    return result;
  }

  Deposit getDeposit7AwaitAuditNOW() {
    Deposit result = new Deposit();
    result.setName("dep7-name");
    result.setCreationTime(NOW);
    result.setHasPersonalData(true);
    result.setStatus(Status.COMPLETE);
    return result;
  }

  Deposit getDeposit8AwaitAudit1YearAgo() {
    Deposit result = new Deposit();
    result.setName("dep8-name");
    result.setCreationTime(ONE_YEAR_AGO);
    result.setHasPersonalData(true);
    result.setStatus(Status.COMPLETE);
    return result;
  }

  Deposit getDeposit9AwaitAudit2YearsAgo() {
    Deposit result = new Deposit();
    result.setName("dep9-name");
    result.setCreationTime(TWO_YEARS_AGO);
    result.setHasPersonalData(true);
    result.setStatus(Status.NOT_STARTED);
    return result;
  }

  Deposit getDeposit10AwaitAudit3YearsAgo() {
    Deposit result = new Deposit();
    result.setName("dep10-name");
    result.setCreationTime(THREE_YEARS_AGO);
    result.setHasPersonalData(true);
    result.setStatus(Status.IN_PROGRESS);
    return result;
  }


}
