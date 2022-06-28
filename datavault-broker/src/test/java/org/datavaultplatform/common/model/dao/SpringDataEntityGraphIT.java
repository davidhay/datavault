package org.datavaultplatform.common.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.common.event.Event;
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
    "broker.initialise.enabled=false",
    "broker.scheduled.enabled=false"
})
public class SpringDataEntityGraphIT extends BaseDatabaseTest {

  @PersistenceContext
  EntityManager em;

  @Autowired
  ArchiveDAO dao01archive;

  @Autowired
  ArchiveStoreDAO dao02archiveStore;
  @Autowired
  AuditChunkStatusDAO dao03auditChunkStatus;
  @Autowired
  AuditDAO dao04audit;
  @Autowired
  BillingDAO dao05billing;

  @Autowired
  ClientDAO dao06client;
  @Autowired
  DataCreatorDAO dao07dataCreator;
  @Autowired
  DataManagerDAO dao08dataManager;
  @Autowired
  DatasetDAO dao09dataset;
  @Autowired
  DepositChunkDAO dao10depositChunk;

  @Autowired
  DepositDAO dao11deposit; ///???? - that's one of mine!!!
  @Autowired
  DepositReviewDAO dao12depositReview;
  @Autowired
  EventDAO dao13event;
  @Autowired
  FileStoreDAO dao14fileStore;

  @Autowired
  GroupDAO dao15group; //NEW
  @Autowired
  JobDAO dao16job;
  @Autowired
  PendingDataCreatorDAO dao17pendingDataCreator;

  @Autowired
  PendingVaultDAO dao18pendingVault;
  @Autowired
  PermissionDAO dao19permission;
  @Autowired
  RetentionPolicyDAO dao20retentionPolicy;
  @Autowired
  RetrieveDAO dao21retrieve;

  @Autowired
  RoleAssignmentDAO dao22roleAssignment;
  @Autowired
  RoleDAO dao23roleDao;
  @Autowired
  UserDAO dao24userDao;
  @Autowired
  VaultDAO dao25vault;
  @Autowired
  VaultReviewDAO dao26vaultReview;

  @Test
  void test01Archive() {
    em.find(Archive.class, "Archive-AAA");
    dao01archive.findById("Archive-BBBB");
    dao01archive.findAll();
    dao01archive.list();
  }

  @Test
  void test02ArchiveStore() {
    em.find(ArchiveStore.class, "ArchiveStore-AAA");
    dao02archiveStore.findById("ArchiveStore-BBBB");
    dao02archiveStore.findAll();
    dao02archiveStore.list();
  }

  @Test
  void test03AuditChunkStatus() {
    em.find(AuditChunkStatus.class, "AuditChunkStatus-AAA");
    dao03auditChunkStatus.findById("AuditChunkStatus-BBBB");
    dao03auditChunkStatus.findAll();
    dao03auditChunkStatus.list();
  }

  @Test
  void test04Audit() {
    em.find(Audit.class, "Audit-AAA");
    dao04audit.findById("Audit-BBB");
    dao04audit.findAll();
    dao04audit.list();
  }

  @Test
  void test05BillingInfo() {
    em.find(BillingInfo.class, "BillingInfo-AAA");
    dao05billing.findById("BillingInfo-BBB");
    dao05billing.findAll();
    dao05billing.list();
  }

  @Test
  void test06Client() {
    em.find(Client.class, "Client-AAA");
    dao06client.findById("Client-BBB");
    dao06client.findAll();
    dao06client.list();
  }

  @Test
  void test07DataCreator() {
    em.find(DataCreator.class, "DataCreator-AAA");
    dao07dataCreator.findById("DataCreator-BBB");
    dao07dataCreator.findAll();
    dao07dataCreator.list();
  }

  @Test
  void test08DataManager() {
    em.find(DataManager.class, "DataManager-AAA");
    dao08dataManager.findById("DataManager-BBB");
    dao08dataManager.findAll();
    dao08dataManager.list();
  }

  @Test
  void test09Dataset() {
    em.find(Dataset.class, "Dataset-AAA");
    dao09dataset.findById("Dataset-BBB");
    dao09dataset.findAll();
    dao09dataset.list();
  }

  @Test
  void test10DepositChunk() {
    em.find(DepositChunk.class, "DepositChunk-AAA");
    dao10depositChunk.findById("DepositChunk-BBB");
    dao10depositChunk.findAll();
    dao10depositChunk.list();
  }

  @Test
  void test11Deposit() {
    em.find(Deposit.class, "Deposit-AAA");
    dao11deposit.findById("Deposit-BBB");
    dao11deposit.findAll();
    dao11deposit.list();
  }

  @Test
  void test12DepositReview() {
    em.find(DepositReview.class, "DepositReview-AAA");
    dao12depositReview.findById("DepositReview-BBB");
    dao12depositReview.findAll();
    dao12depositReview.list();
  }

  @Test
  void test13Event() {
    em.find(Event.class, "Event-AAA");
    dao13event.findById("Event-BBB");
    dao13event.findAll();
    dao13event.list();
  }

  @Test
  void test14FileStore() {
    em.find(FileStore.class, "FileStore-AAA");
    dao14fileStore.findById("FileStore-BBB");
    dao14fileStore.findAll();
    dao14fileStore.list();
    log.info("fin");
  }

  @Test
  void test15Group() {
    em.find(Group.class, "Group-AAA");
    dao15group.findById("Group-BBB");
    dao15group.findAll();
    dao15group.list();
  }

  @Test
  void test16Job() {
    em.find(Job.class, "Job-AAA");
    dao16job.findById("Job-BBB");
    dao16job.findAll();
    dao16job.list();
  }

  @Test
  void test17PendingDataCreator() {
    em.find(PendingDataCreator.class, "PendingDataCreator-AAA");
    dao17pendingDataCreator.findById("PendingDataCreator-BBB");
    dao17pendingDataCreator.findAll();
    dao17pendingDataCreator.list();
  }

  @Test
  void test18PendingVault() {
    em.find(PendingVault.class, "PendingVault-AAA");
    dao18pendingVault.findById("PendingVault-BBB");
    dao18pendingVault.findAll();
    dao18pendingVault.list();
  }

  @Test
  void test19PermissionModel() {
    em.find(PermissionModel.class, "PermissionModel-AAA");
    dao19permission.findById("PermissionModel-BBB");
    dao19permission.findAll();
    dao19permission.list();
  }

  @Test
  void test20RetentionPolicy() {
    em.find(RetentionPolicy.class, 1111);
    dao20retentionPolicy.findById(9999);
    dao20retentionPolicy.findAll();
    dao20retentionPolicy.list();
  }

  @Test
  void test21Retrieve() {
    em.find(Retrieve.class, "Retrieve-AAA");
    dao21retrieve.findById("Retrieve-BBBB");
    dao21retrieve.findAll();
    dao21retrieve.list();
  }

  @Test
  void test22RoleAssignment() {
    em.find(RoleAssignment.class, 1111L);
    dao22roleAssignment.findById(9999L);
    dao22roleAssignment.findAll();
    dao22roleAssignment.list();
  }

  @Test
  void test23RoleModel() {
    em.find(RoleModel.class, 2222L);
    dao23roleDao.findById(8888L);
    dao23roleDao.findAll();
    dao23roleDao.list();
  }

  @Test
  void test24User() {
    em.find(User.class, "User-AAA");
    dao24userDao.findById("User-BBB");
    dao24userDao.findAll();
    dao24userDao.list();
  }

  @Test
  void test25Vault() {
    em.find(Vault.class, "Vault-AAA");
    dao25vault.findById("Vault-BBBB");
    dao25vault.findAll();
    dao25vault.list();
  }

  @Test
  void test26VaultReview() {
    em.find(VaultReview.class, "VaultReview-AAA");
    dao26vaultReview.findById("VaultReview-BBBB");
    dao26vaultReview.findAll();
    dao26vaultReview.list();

  }

  @Test
  void testLogging() {
    log.info("This is a test@INFO");
    log.debug("This is a test@DEBUG");
    log.trace("This is a test@TRACE");
  }
}
