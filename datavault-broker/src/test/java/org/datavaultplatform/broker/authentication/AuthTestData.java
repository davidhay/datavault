package org.datavaultplatform.broker.authentication;

import java.util.Arrays;
import org.datavaultplatform.common.model.ArchiveStore;
import org.datavaultplatform.common.model.DataManager;
import org.datavaultplatform.common.model.Deposit;
import org.datavaultplatform.common.model.DepositReview;
import org.datavaultplatform.common.model.PendingVault;
import org.datavaultplatform.common.model.PermissionModel;
import org.datavaultplatform.common.model.Retrieve;
import org.datavaultplatform.common.model.RoleAssignment;
import org.datavaultplatform.common.model.RoleModel;
import org.datavaultplatform.common.model.User;
import org.datavaultplatform.common.model.Vault;
import org.datavaultplatform.common.model.VaultReview;
import org.datavaultplatform.common.request.CreateVault;
import org.datavaultplatform.common.request.TransferVault;
import org.datavaultplatform.common.request.ValidateUser;
import org.datavaultplatform.common.response.AuditInfo;
import org.datavaultplatform.common.response.DepositInfo;
import org.datavaultplatform.common.response.DepositsData;
import org.datavaultplatform.common.response.EventInfo;
import org.datavaultplatform.common.response.ReviewInfo;
import org.datavaultplatform.common.response.VaultInfo;
import org.datavaultplatform.common.response.VaultsData;
import org.testcontainers.shaded.com.google.common.util.concurrent.RateLimiter;

public class AuthTestData {

  public static final VaultsData VAULTS_DATA;
  public static final ReviewInfo REVIEW_INFO;

  public static final VaultReview VAULT_REVIEW;

  public static final DepositReview DEPOSIT_REVIEW;

  public static final ArchiveStore ARCHIVE_STORE_1;
  public static final ArchiveStore ARCHIVE_STORE_2;
  public static final AuditInfo AUDIT_INFO_1;
  public static final AuditInfo AUDIT_INFO_2;
  public static final DepositInfo DEPOSIT_INFO_1;
  public static final DepositInfo DEPOSIT_INFO_2;
  public static final DepositsData DEPOSIT_DATA_1;
  public static final EventInfo EVENT_INFO_1;
  public static final EventInfo EVENT_INFO_2;
  public static final Retrieve RETRIEVE_1;
  public static final Retrieve RETRIEVE_2;
  public static final DataManager DATA_MANAGER_1;
  public static final DataManager DATA_MANAGER_2;
  public static final VaultInfo VAULT_INFO_1;
  public static final VaultInfo VAULT_INFO_2;

  public static final Vault VAULT_1;
  public static final PendingVault PENDING_VAULT_1;
  public static final CreateVault CREATE_VAULT;
  public static final TransferVault TRANSFER_VAULT_1;

  public static final RoleModel ROLE_MODEL;
  public static final RoleAssignment ROLE_ASSIGNMENT;
  public static final PermissionModel PERMISSION_MODEL;
  public static final Deposit DEPOSIT_1;
  public static final Deposit DEPOSIT_2;
  public static final User USER_1;
  public static final User USER_2;
  public static final ValidateUser VALIDATE_USER;

  static {
    VaultInfo vaultInfo1 = new VaultInfo();
    vaultInfo1.setID("vaultInfo1");

    VaultInfo vaultInfo2 = new VaultInfo();
    vaultInfo2.setID("vaultInfo2");

    VAULTS_DATA = new VaultsData();
    VAULTS_DATA.setData(Arrays.asList(vaultInfo1, vaultInfo2));
    VAULTS_DATA.setRecordsTotal(2);
    VAULTS_DATA.setRecordsFiltered(123);

    REVIEW_INFO = new ReviewInfo();
    REVIEW_INFO.setVaultReviewId("2112");
    REVIEW_INFO.setDepositIds(Arrays.asList("d1", "d2"));
    REVIEW_INFO.setDepositReviewIds(Arrays.asList("dr1", "dr2"));

    VAULT_REVIEW = new VaultReview();
    VAULT_REVIEW.setId("vault-review-1");
    VAULT_REVIEW.setComment("this is a comment");

    DEPOSIT_REVIEW = new DepositReview();
    DEPOSIT_REVIEW.setId("deposit-review-1");
    DEPOSIT_REVIEW.setComment("this is a comment");

    ARCHIVE_STORE_1 = new ArchiveStore();
    ARCHIVE_STORE_1.setLabel("ARCHIVE STORE 1");

    ARCHIVE_STORE_2 = new ArchiveStore();
    ARCHIVE_STORE_2.setLabel("ARCHIVE STORE 2");

    AUDIT_INFO_1 = new AuditInfo();
    AUDIT_INFO_1.setId("audit-info-1");

    AUDIT_INFO_2 = new AuditInfo();
    AUDIT_INFO_2.setId("audit-info-2");

    DEPOSIT_INFO_1 = new DepositInfo();
    DEPOSIT_INFO_1.setID("deposit-info-1");

    DEPOSIT_INFO_2 = new DepositInfo();
    DEPOSIT_INFO_2.setID("deposit-info-2");

    DEPOSIT_DATA_1 = new DepositsData();
    DEPOSIT_DATA_1.setData(Arrays.asList(DEPOSIT_INFO_1, DEPOSIT_INFO_2));
    DEPOSIT_DATA_1.setRecordsTotal(1234);
    DEPOSIT_DATA_1.setRecordsFiltered(123);

    EVENT_INFO_1 = new EventInfo();
    EVENT_INFO_1.setId("event-info-1");

    EVENT_INFO_2 = new EventInfo();
    EVENT_INFO_2.setId("event-info-2");

    RETRIEVE_1 = new Retrieve();
    RETRIEVE_1.setNote("retrieve 1");

    RETRIEVE_2 = new Retrieve();
    RETRIEVE_2.setNote("retrieve 2");

    DATA_MANAGER_1 = new DataManager();
    DATA_MANAGER_1.setUUN("data-manager-1");

    DATA_MANAGER_2 = new DataManager();
    DATA_MANAGER_2.setUUN("data-manager-2");

    VAULT_INFO_1 = new VaultInfo();
    VAULT_INFO_1.setID("vault-info-1");

    VAULT_INFO_2 = new VaultInfo();
    VAULT_INFO_2.setID("vault-info-2");

    VAULT_1 = new Vault();
    VAULT_1.setName("VAULT_ONE");

    PENDING_VAULT_1 = new PendingVault();
    PENDING_VAULT_1.setId("vault-id-1");

    CREATE_VAULT = new CreateVault();
    CREATE_VAULT.setName("create-vault-1");

    TRANSFER_VAULT_1 = new TransferVault();
    TRANSFER_VAULT_1.setReason("transfer-vault-1");

    ROLE_MODEL = new RoleModel();
    ROLE_MODEL.setId(1111L);
    ROLE_MODEL.setName("role-model-one");

    ROLE_ASSIGNMENT = new RoleAssignment();
    ROLE_ASSIGNMENT.setId(1234L);

    PERMISSION_MODEL = new PermissionModel();
    PERMISSION_MODEL.setId("permission-model-1");

    DEPOSIT_1 = new Deposit();
    DEPOSIT_1.setName("deposit-one");

    DEPOSIT_2 = new Deposit();
    DEPOSIT_2.setName("deposit-two");


    USER_1 = new User();
    USER_1.setID("user-one");

    USER_2 = new User();
    USER_2.setID("user-two");

    VALIDATE_USER = new ValidateUser();
    VALIDATE_USER.setUserid("validate-user-id-one");

  }
}
