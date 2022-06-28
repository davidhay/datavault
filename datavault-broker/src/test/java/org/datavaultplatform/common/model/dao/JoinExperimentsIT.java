package org.datavaultplatform.common.model.dao;

import static org.datavaultplatform.broker.test.TestUtils.NOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseReuseDatabaseTest;
import org.datavaultplatform.common.model.Deposit;
import org.datavaultplatform.common.model.Deposit.Status;
import org.datavaultplatform.common.model.Deposit_;
import org.datavaultplatform.common.model.Group;
import org.datavaultplatform.common.model.Group_;
import org.datavaultplatform.common.model.Vault;
import org.datavaultplatform.common.model.Vault_;
import org.datavaultplatform.common.model.dao.old.DepositOldDAO;
import org.datavaultplatform.common.model.dao.old.GroupOldDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = DataVaultBrokerApp.class)
@AddTestProperties
@Slf4j
@TestPropertySource(properties = {
    "broker.email.enabled=false",
    "broker.controllers.enabled=false",
    "broker.rabbit.enabled=false",
    "broker.scheduled.enabled=false"
})
public class JoinExperimentsIT extends BaseReuseDatabaseTest {

  @PersistenceContext
  EntityManager em;

  @Autowired
  DepositOldDAO dao;


  @Autowired
  VaultDAO vaultDAO;

  @Autowired
  GroupOldDAO groupDAO;

  //@Autowired
  //DepositChunkDAO depositChunkDAO;

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

  @Test
  @Transactional
  void testJoin1(){

    String schoolId1 = "lfcs-id-1";
    String schoolId2 = "lfcs-id-2";

    Group group1 = new Group();
    group1.setID(schoolId1);
    group1.setName("LFCS-ONE");
    group1.setEnabled(true);
    groupDAO.save(group1);

    Group group2 = new Group();
    group2.setID(schoolId2);
    group2.setName("LFCS-TWO");
    group2.setEnabled(true);
    groupDAO.save(group2);

    groupDAO.flush();


    Vault vault1 = new Vault();
    vault1.setContact("James Bond");
    vault1.setGroup(group1);
    vault1.setName("vault-one");
    vault1.setReviewDate(NOW);
    vaultDAO.save(vault1);

    Vault vault2 = new Vault();
    vault2.setContact("James Bond");
    vault2.setGroup(group2);
    vault2.setName("vault-two");
    vault2.setReviewDate(NOW);
    vaultDAO.save(vault2);

    vaultDAO.flush();

    Deposit depositReview1 = getDeposit1();
    depositReview1.setVault(vault1);
    depositReview1.setName("name1");

    Deposit depositReview2 = getDeposit2();
    depositReview2.setVault(vault1);
    depositReview2.setName("name12");

    Deposit depositReview3 = getDeposit3();
    depositReview3.setVault(vault2);
    depositReview3.setName("name3");

    Deposit depositReview4 = getDeposit4();
    depositReview4.setName("name34");

    dao.save(depositReview1);
    dao.save(depositReview2);
    dao.save(depositReview3);
    dao.save(depositReview4);

    dao.flush();

    assertEquals(4, dao.count());


    String sql = "SELECT deposit "
        + "FROM org.datavaultplatform.common.model.Deposit deposit " +
        "INNER JOIN deposit.vault as vault "
        + "INNER JOIN vault.group as group "
        + "WHERE group.id = :groupId ";


    TypedQuery<Deposit> query = em.createQuery(sql, Deposit.class);
    query.setParameter("groupId", schoolId1);

    List<Deposit> result1 = query.setParameter("groupId", schoolId1).getResultList();
    log.info("result1 {}", result1);

    List<Deposit> result2 = query.setParameter("groupId", schoolId2).getResultList();
    log.info("result2 {}", result2);

    //LEARNING HOW JOINS WORK
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Deposit> cq = cb.createQuery(Deposit.class).distinct(true);
    Root<Deposit> root = cq.from(Deposit.class);

    Function<Root<Deposit>,Predicate> predFn = _root -> {
      Join<Deposit, Vault> join1a = _root.join(Deposit_.vault);
      Join<Vault, Group> join2a = join1a.join(Vault_.group);
      Predicate pred = join2a.get(Group_.id).in(Arrays.asList("x", "y"));
      return pred;
    };

    //ITEMS
    {
      CriteriaQuery<Deposit> query1 = cq.select(root).where(predFn.apply(root));
      List<Deposit> deposits1 = em.createQuery(query1).getResultList();
      log.info("deposits1 {}", deposits1);
    }
    //COUNT
    CriteriaQuery<Long> queryCount = cb.createQuery(Long.class);
    Root<Deposit> root2 = queryCount.from(Deposit.class);
    queryCount.select(cb.countDistinct(root2)).where(predFn.apply(root2));
    Long deposits1Count = em.createQuery(queryCount).getSingleResult();
    log.info("deposits1Count {}", deposits1Count);
  }


}
