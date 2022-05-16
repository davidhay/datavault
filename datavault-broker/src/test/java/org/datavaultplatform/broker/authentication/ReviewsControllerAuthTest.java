package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.ReviewsController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
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
public class ReviewsControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @MockBean
  ReviewsController controller;

  /*
    @GetMapping("/vaultreviews/depositreviews/{depositReviewId}")
    public DepositReview getDepositReview(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                 @PathVariable("depositReviewId") String depositReviewId) {
   */
  @Test
  void testGetDepositReview() {

    when(controller.getDepositReview(USER_ID_1, "deposit-review-123")).thenReturn(
        AuthTestData.DEPOSIT_REVIEW_1);

    checkWorksWhenAuthenticatedFailsOtherwise(
        get("/vaultreviews/depositreviews/deposit-review-123"),
        AuthTestData.DEPOSIT_REVIEW_1,
        HttpStatus.OK, false);

    verify(controller).getDepositReview(USER_ID_1, "deposit-review-123");
  }

  /*
    @GetMapping("/vaultreviews/{vaultReviewId}/depositreviews")
    public List<DepositReview> getDepositReviews(@RequestHeader(value = "X-UserID", required = true) String userID,
                                             @PathVariable("vaultReviewId") String vaultReviewId) {
   */
  @Test
  void testGetDepositReviews() {

    when(controller.getDepositReviews(USER_ID_1, "vault-review-1234")).thenReturn(
        Arrays.asList(AuthTestData.DEPOSIT_REVIEW_1, AuthTestData.DEPOSIT_REVIEW_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaultreviews/vault-review-1234/depositreviews"),
        Arrays.asList(AuthTestData.DEPOSIT_REVIEW_1, AuthTestData.DEPOSIT_REVIEW_2),
        HttpStatus.OK, false);

    verify(controller).getDepositReviews(USER_ID_1, "vault-review-1234");
  }

  /*
    @GetMapping( "/vaults/vaultreviews/{vaultReviewId}")
    public VaultReview getVaultReview(@RequestHeader(value = "X-UserID", required = true) String userID,
                                @PathVariable("vaultReviewId") String vaultReviewId) {
   */
  @Test
  void testGetVaultReview() {

    when(controller.getVaultReview(USER_ID_1, "vault-review-12345")).thenReturn(
        AuthTestData.VAULT_REVIEW_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/vaultreviews/vault-review-12345"),
        AuthTestData.VAULT_REVIEW_1,
        HttpStatus.OK, false);

    verify(controller).getVaultReview(USER_ID_1, "vault-review-12345");
  }

  /*
    @GetMapping("/vaults/{vaultid}/vaultreviews")
    public List<ReviewInfo> getVaultReviews(@RequestHeader(value = "X-UserID", required = true) String userID,
                                             @PathVariable("vaultid") String vaultID) throws Exception {
   */
  @Test
  void testGetVaultReviews() throws Exception {
    when(controller.getVaultReviews(USER_ID_1, "vault-id-123")).thenReturn(
        Arrays.asList(AuthTestData.REVIEW_INFO_1, AuthTestData.REVIEW_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/vault-id-123/vaultreviews"),
        Arrays.asList(AuthTestData.REVIEW_INFO_1, AuthTestData.REVIEW_INFO_2),
        HttpStatus.OK, false);

    verify(controller).getVaultReviews(USER_ID_1, "vault-id-123");
  }

}
