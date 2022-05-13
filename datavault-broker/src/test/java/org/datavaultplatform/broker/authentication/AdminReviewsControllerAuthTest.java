package org.datavaultplatform.broker.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.admin.AdminReviewsController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.common.model.DepositReview;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.VaultReview;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = DataVaultBrokerApp.class)
@AddTestProperties
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class})
@TestPropertySource(properties = {
    "broker.controllers.enabled=true",
    "broker.services.enabled=false",
    "broker.scheduled.enabled=false",
    "broker.initialise.enabled=false",
    "broker.rabbit.enabled=false",
    "broker.database.enabled=false"})
@Import(MockServicesConfig.class) //spring security relies on services
public class AdminReviewsControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  AdminReviewsController controller;

  @MockBean
  Sender sender;

  @Captor
  ArgumentCaptor<VaultReview> argVaultReview;

  @Captor
  ArgumentCaptor<DepositReview> argDepositReview;

  @Captor
  ArgumentCaptor<String> argClientKey;

  @Captor
  ArgumentCaptor<String> argUserId;

  @Test
  void testGetCurrentReview() throws Exception {
    when(controller.getCurrentReview(
        USER_ID_1, "2112")).thenReturn(AuthTestData.REVIEW_INFO);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/vaults/2112/vaultreviews/current"),
        AuthTestData.REVIEW_INFO, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_VAULTS);

    verify(controller).getCurrentReview(USER_ID_1, "2112");
  }

  @Test
  void testGetVaultsForReview() throws Exception {
    when(controller.getVaultsForReview(
        USER_ID_1)).thenReturn(AuthTestData.VAULTS_DATA);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/vaultsForReview"),
        AuthTestData.VAULTS_DATA, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_VAULTS);

    verify(controller).getVaultsForReview(USER_ID_1);
  }

  /*
      @PostMapping("/admin/vaults/vaultreviews/current")
    public ReviewInfo createCurrentReview(@RequestHeader(value = "X-UserID", required = true) String userID,
                                       @RequestBody String vaultID) throws Exception {

   */
  @Test
  void testPostCreateCurrentReview() throws Exception {
    when(controller.createCurrentReview(
        USER_ID_1, "vaultID1")).thenReturn(AuthTestData.REVIEW_INFO);

    checkWorksWhenAuthenticatedFailsOtherwise(
        post("/admin/vaults/vaultreviews/current")
            .content("vaultID1")
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.REVIEW_INFO, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_VAULTS);

    verify(controller).createCurrentReview(USER_ID_1, "vaultID1");
  }

  /*
      @PutMapping("/admin/vaults/vaultreviews")
    public VaultReview editVaultReview(@RequestHeader(value = "X-UserID", required = true) String userID,
                                       @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                       @RequestBody VaultReview vaultReview) {
   */
  @Test
  void testPutEditCurrentReview() throws Exception {

    //we have to use Captor for VaultReview cos it doesn't have decent equals method
    //and as it's an @Entity class.
    when(controller.editVaultReview(
        argUserId.capture(),
        argClientKey.capture(),
        argVaultReview.capture())).thenReturn(AuthTestData.VAULT_REVIEW);

    checkWorksWhenAuthenticatedFailsOtherwise(
        put("/admin/vaults/vaultreviews")
            .content(mapper.writeValueAsString(AuthTestData.VAULT_REVIEW))
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.VAULT_REVIEW, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_VAULTS);

    verify(controller).editVaultReview(USER_ID_1, API_KEY_1, argVaultReview.getValue());
    assertEquals(AuthTestData.VAULT_REVIEW.getId(), argVaultReview.getValue().getId());
  }

  /*
    @PutMapping("/admin/vaultreviews/depositreviews")
    public DepositReview editDepositReview(@RequestHeader(value = "X-UserID", required = true) String userID,
                                       @RequestBody DepositReview depositReview) {
   */
  @Test
  void testPutEditDepositReview() throws Exception {

    //we have to use Captor for DepositReview cos it doesn't have decent equals method
    //and as it's an @Entity class.
    when(controller.editDepositReview(
        argUserId.capture(),
        argDepositReview.capture())).thenReturn(AuthTestData.DEPOSIT_REVIEW);

    checkWorksWhenAuthenticatedFailsOtherwise(
        put("/admin/vaultreviews/depositreviews")
            .content(mapper.writeValueAsString(AuthTestData.DEPOSIT_REVIEW))
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.DEPOSIT_REVIEW, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_VAULTS);

    verify(controller).editDepositReview(USER_ID_1, argDepositReview.getValue());
    assertEquals(AuthTestData.DEPOSIT_REVIEW.getId(), argDepositReview.getValue().getId());
  }

}
