package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.common.model.Archive;
import org.datavaultplatform.common.model.ArchiveStore;
import org.datavaultplatform.common.model.Audit;
import org.datavaultplatform.common.model.AuditChunkStatus;
import org.datavaultplatform.common.model.BillingInfo;
import org.datavaultplatform.common.model.Client;
import org.datavaultplatform.common.model.DataCreator;
import org.datavaultplatform.common.model.DataManager;
import org.datavaultplatform.common.model.Dataset;
import org.datavaultplatform.common.model.Deposit;
import org.datavaultplatform.common.model.DepositChunk;
import org.datavaultplatform.common.model.DepositPath;
import org.datavaultplatform.common.model.DepositReview;
import org.datavaultplatform.common.model.FileStore;
import org.datavaultplatform.common.model.Group;
import org.datavaultplatform.common.model.Job;
import org.datavaultplatform.common.model.PendingDataCreator;
import org.datavaultplatform.common.model.PendingVault;
import org.datavaultplatform.common.model.PermissionModel;
import org.datavaultplatform.common.model.RetentionPolicy;
import org.datavaultplatform.common.model.Retrieve;
import org.datavaultplatform.common.model.RoleAssignment;
import org.datavaultplatform.common.model.RoleModel;
import org.datavaultplatform.common.model.User;
import org.datavaultplatform.common.model.Vault;
import org.datavaultplatform.common.model.VaultReview;
import org.datavaultplatform.common.util.DaoUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.junit.jupiter.api.Test;
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
public class OuterJoinJPAIT extends BaseDatabaseTest  {

  @PersistenceContext
  EntityManager em;

  @Test
  @Transactional
  void getAllOuterJoins() throws  Exception{

    Class[] classes = getClasses("org.datavaultplatform.common.model");
    AtomicInteger counter = new AtomicInteger(0);

    assertTrue(isEntity(Vault.class));
    assertFalse(isEntity(String.class));

    ArrayList<String> rows = new ArrayList<>();
    AtomicInteger total = new AtomicInteger();
    Arrays.stream(classes).filter( this::isEntity).forEach(claz -> {
      List<String> cols = new ArrayList<>();
      int curCount = counter.incrementAndGet();
      cols.add(""+curCount);
      cols.add(claz.getName());
      Set<String> outerJoins = getOuterJoins(claz);
      /*
      int size = outerJoins.size();
      total.addAndGet(size);
      cols.add(String.valueOf(size));
      outerJoins.forEach(cols::add);
      String row = cols.stream().collect(Collectors.joining(","));
      rows.add(row);
       */
    });
    /*
    System.out.println("---------------------------");
    rows.forEach(System.out::println);
    System.out.println("===========================");
    System.out.printf("total outer joins %d%n",total.get());
     */
    //System.out.printf("total outer joins %d%n",total.get());
    System.out.printf("counter [%d]%n", counter.get());
    System.out.println("FIN");
  }

  private boolean isEntity(Class<?> claz){
    return claz.isAnnotationPresent(Entity.class);
  }

  private static Class[] getClasses(String packageName)
      throws ClassNotFoundException, IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;
    String path = packageName.replace('.', '/');
    Enumeration resources = classLoader.getResources(path);
    List<File> dirs = new ArrayList<>();
    while (resources.hasMoreElements()) {
      URL resource = (URL) resources.nextElement();
      dirs.add(new File(resource.getFile()));
    }
    ArrayList<Class> classes = new ArrayList<>();
    for (File directory : dirs) {
      classes.addAll(findClasses(directory, packageName));
    }
    return classes.toArray(new Class[classes.size()]);
  }

  /**
   * Recursive method used to find all classes in a given directory and subdirs.
   *
   * @param directory   The base directory
   * @param packageName The package name for classes found inside the base directory
   * @return The classes
   * @throws ClassNotFoundException
   */
  private static List findClasses(File directory, String packageName) throws ClassNotFoundException {
    List classes = new ArrayList();
    if (!directory.exists()) {
      return classes;
    }
    File[] files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        assert !file.getName().contains(".");
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
      }
    }
    return classes;
  }
  <E> Set<String>  getOuterJoins(Class<E> entity){
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<E> query = cb.createQuery(entity);
      query.select(query.from(entity));
      TypedQuery<E> typedQuery = DaoUtils.addEntityGraph(em,entity,em.createQuery(query));
      Query hibQuery = (Query) typedQuery;

      String sql = hibQuery.getQueryString();
      List<E> result = typedQuery.getResultList();
      return extractOuterJoins(sql);
  }

  private Set<String> extractOuterJoins(String sql){
    String regex = "\\bleft outer join\\s+([a-zA-Z]+)\\s+";

    Pattern pattern = Pattern.compile(regex);
    Set<String> result = new TreeSet();
    //Matching the compiled pattern in the String
    Matcher matcher = pattern.matcher(sql);
    while (matcher.find()) {
      result.add(matcher.group(1));
    }
    return result;
  }



  @Test
  @Transactional
  void testGetOuterJoinsForDepositPath(){
    Set<String> result = getOuterJoins(DepositPath.class);
    System.out.println(result);
  }
  @Test
  @Transactional
  void testGetOuterJoinsForPendingDataCreator(){
    Set<String> result = getOuterJoins(PendingDataCreator.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsFor03ArchiveStore(){
    Set<String> result = getOuterJoins(ArchiveStore.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsFor04RoleAssignment(){
    Set<String> result = getOuterJoins(RoleAssignment.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsFor05DataManager(){
    Set<String> result = getOuterJoins(DataManager.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsFor06PendingVault(){
    Set<String> result = getOuterJoins(PendingVault.class);
    System.out.println(result);
  }


  @Test
  @Transactional
  void testGetOuterJoinsFor07DataCreator(){
    Set<String> result = getOuterJoins(DataCreator.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsFor08BillingInfo() {
    Set<String> result = getOuterJoins(BillingInfo.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsFor09Archive() {
    Set<String> result = getOuterJoins(Archive.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForPermissionModel10() {
    Set<String> result = getOuterJoins(PermissionModel.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForVault11(){
    Set<String> result = getOuterJoins(Vault.class);
  }
  @Test
  @Transactional
  void testGetOuterJoinsForDeposit12(){
    Set<String> result = getOuterJoins(Deposit.class);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForUser13() {
    Set<String> result = getOuterJoins(User.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForDepositChunk14(){
    Set<String> result = getOuterJoins(DepositChunk.class);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForAuditChunkStatus15(){
    Set<String> result = getOuterJoins(AuditChunkStatus.class);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForDepositReview16(){
    Set<String> result = getOuterJoins(DepositReview.class);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForClient17() {
    Set<String> result = getOuterJoins(Client.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForRetrieve18(){
    Set<String> result = getOuterJoins(Retrieve.class);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForDataset19() {
    Set<String> result = getOuterJoins(Dataset.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForRetentionPolicy20() {
    Set<String> result = getOuterJoins(RetentionPolicy.class);
    System.out.println(result);
  }
  @Test
  @Transactional
  void testGetOuterJoinsForVaultReview21() {
    Set<String> result = getOuterJoins(VaultReview.class);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForGroup22() {
    Set<String> result = getOuterJoins(Group.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForFileStore23() {
    Set<String> result = getOuterJoins(FileStore.class);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForAudit24() {
    Set<String> result = getOuterJoins(Audit.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForJob25() {
    Set<String> result = getOuterJoins(Job.class);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForRoleModel26() {
    Set<String> result = getOuterJoins(RoleModel.class);
  }
}
