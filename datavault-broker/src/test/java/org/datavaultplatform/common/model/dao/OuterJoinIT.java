package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
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
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.common.model.DepositPath;
import org.datavaultplatform.common.model.RoleAssignment;
import org.datavaultplatform.common.model.Vault;
import org.hibernate.Criteria;
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
public class OuterJoinIT extends BaseDatabaseTest  {

  @PersistenceContext
  EntityManager em;

  @Test
  @Transactional
  void getOuterJoins() throws  Exception{
    Session session = em.unwrap(Session.class);

    Class[] classes = getClasses("org.datavaultplatform.common.model");
    AtomicInteger counter = new AtomicInteger(1);

    assertTrue(isEntity(Vault.class));
    assertFalse(isEntity(String.class));

    ArrayList<String> rows = new ArrayList<>();
    AtomicInteger total = new AtomicInteger();
    Arrays.stream(classes).filter( this::isEntity).forEach(claz -> {
      List<String> cols = new ArrayList<>();
      cols.add(""+counter.getAndIncrement());
      cols.add(claz.getName());
      Set<String> outerJoins = getOuterJoins(session, claz);
      int size = outerJoins.size();
      total.addAndGet(size);
      cols.add(String.valueOf(size));
      outerJoins.forEach(cols::add);
      String row = cols.stream().collect(Collectors.joining(","));
      rows.add(row);
    });
    System.out.println("---------------------------");
    rows.forEach(System.out::println);
    System.out.println("===========================");
    System.out.printf("total outer joins %d%n",total.get());
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
  Set<String> getOuterJoins(Session session, Class<?> entity){
      Criteria cr = session.createCriteria(entity);
      String sql = extractSQL(cr);
      System.out.println(sql);
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

  String extractSQL(Criteria cr){
    CriteriaImpl criteriaImpl = (CriteriaImpl)cr;
    SharedSessionContractImplementor session = criteriaImpl.getSession();
    SessionFactoryImplementor factory = session.getFactory();
    CriteriaQueryTranslator translator=new CriteriaQueryTranslator(factory,criteriaImpl,criteriaImpl.getEntityOrClassName(),CriteriaQueryTranslator.ROOT_SQL_ALIAS);
    String[] implementors = factory.getImplementors( criteriaImpl.getEntityOrClassName() );

    CriteriaJoinWalker walker = new CriteriaJoinWalker((OuterJoinLoadable)factory.getEntityPersister(implementors[0]),
        translator,
        factory,
        criteriaImpl,
        criteriaImpl.getEntityOrClassName(),
        session.getLoadQueryInfluencers()   );

    String sql=walker.getSQLString();
    log.info("SQL is [{}]", sql);
    return sql;
  }

  @Test
  @Transactional
  void testGetOuterJoinsForVault(){
    Session session = em.unwrap(Session.class);
    Set<String> result = getOuterJoins(session, Vault.class);
    assertEquals(
        new TreeSet<>(Arrays.asList("Datasets","Groups","RetentionPolicies", "Users")),
        result);
  }


  @Test
  @Transactional
  void testExtractOuterJoinsForVaultSQL() {
    String sql = "select this_.id as id1_28_4_, this_.affirmed as affirmed2_28_4_, this_.contact as contact3_28_4_, this_.creationTime as creation4_28_4_, this_.dataset_id as dataset19_28_4_, this_.description as descript5_28_4_, this_.estimate as estimate6_28_4_, this_.grantEndDate as grantend7_28_4_, this_.group_id as group_i20_28_4_, this_.name as name8_28_4_, this_.notes as notes9_28_4_, this_.projectId as project10_28_4_, this_.pureLink as purelin11_28_4_, this_.retentionPolicy_id as retenti21_28_4_, this_.retentionPolicyExpiry as retenti12_28_4_, this_.retentionPolicyLastChecked as retenti13_28_4_, this_.retentionPolicyStatus as retenti14_28_4_, this_.reviewDate as reviewd15_28_4_, this_.snapshot as snapsho16_28_4_, this_.user_id as user_id22_28_4_, this_.vaultSize as vaultsi17_28_4_, this_.version as version18_28_4_, dataset2_.id as id1_8_0_, dataset2_.crisId as crisid2_8_0_, dataset2_.name as name3_8_0_, group3_.id as id1_16_1_, group3_.enabled as enabled2_16_1_, group3_.name as name3_16_1_, retentionp4_.id as id1_21_2_, retentionp4_.dataGuidanceReviewed as dataguid2_21_2_, retentionp4_.description as descript3_21_2_, retentionp4_.endDate as enddate4_21_2_, retentionp4_.engine as engine5_21_2_, retentionp4_.extendUponRetrieval as extendup6_21_2_, retentionp4_.inEffectDate as ineffect7_21_2_, retentionp4_.minDataRetentionPeriod as mindatar8_21_2_, retentionp4_.minRetentionPeriod as minreten9_21_2_, retentionp4_.name as name10_21_2_, retentionp4_.sort as sort11_21_2_, retentionp4_.url as url12_21_2_, user5_.id as id1_26_3_, user5_.email as email2_26_3_, user5_.firstname as firstnam3_26_3_, user5_.lastname as lastname4_26_3_, user5_.password as password5_26_3_, user5_.properties as properti6_26_3_ from Vaults this_ left outer join Datasets dataset2_ on this_.dataset_id=dataset2_.id left outer join Groups group3_ on this_.group_id=group3_.id left outer join RetentionPolicies retentionp4_ on this_.retentionPolicy_id=retentionp4_.id left outer join Users user5_ on this_.user_id=user5_.id\n";
    Set<String> result = extractOuterJoins(sql);
    System.out.printf("RESULTS %s%n", result);
    assertEquals(
        new TreeSet<>(Arrays.asList("Datasets","Groups","RetentionPolicies", "Users")),
        result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForDepositPath(){
    Session session = em.unwrap(Session.class);
    Set<String> result = getOuterJoins(session, DepositPath.class);
    System.out.println(result);
  }

  @Test
  @Transactional
  void testGetOuterJoinsForRoleAssignment(){
    Session session = em.unwrap(Session.class);
    Set<String> result = getOuterJoins(session, RoleAssignment.class);
    System.out.println(result);
  }

}
