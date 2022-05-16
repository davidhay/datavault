package org.datavaultplatform.broker.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Arrays;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.DepositsController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.common.model.Retrieve;
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
import org.springframework.http.ResponseEntity;
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
public class DepositsControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @MockBean
  DepositsController controller;

  @Captor
  ArgumentCaptor<String> argUserId;

  @Captor
  ArgumentCaptor<String> argDepositId;

  @Captor
  ArgumentCaptor<Retrieve> argRetrieve;

  /*
    @PostMapping("/deposits")
    public ResponseEntity<DepositInfo> addDeposit(@RequestHeader(value = "X-UserID", required = true) String userID,
                                             @RequestBody CreateDeposit createDeposit) throws Exception {
   */
  @Test
  void testPostAddDeposit() throws Exception {

    when(controller.addDeposit(USER_ID_1, AuthTestData.CREATE_DEPOSIT_1)).thenReturn(
        ResponseEntity.ok(AuthTestData.DEPOSIT_INFO_1));

    checkWorksWhenAuthenticatedFailsOtherwise(post("/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AuthTestData.CREATE_DEPOSIT_1)),
        AuthTestData.DEPOSIT_INFO_1, HttpStatus.OK, false);

    verify(controller).addDeposit(USER_ID_1, AuthTestData.CREATE_DEPOSIT_1);
  }

  /*
    @GetMapping("/deposits/{depositid}")
    public DepositInfo getDeposit(@RequestHeader(value = "X-UserID", required = true) String userID,
                                  @PathVariable("depositid") String depositID) throws Exception {
   */
  @Test
  void testGetDeposit() throws Exception {
    when(controller.getDeposit(USER_ID_1, "deposit-id-1")).thenReturn(AuthTestData.DEPOSIT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/deposits/deposit-id-1"),
        AuthTestData.DEPOSIT_INFO_1, HttpStatus.OK, false);

    verify(controller).getDeposit(USER_ID_1, "deposit-id-1");
  }

  @Test
  void testGetDepositEvents() throws Exception {
    when(controller.getDepositEvents(USER_ID_1, "deposit-id-1")).thenReturn(
        Arrays.asList(AuthTestData.EVENT_INFO_1, AuthTestData.EVENT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/deposits/deposit-id-1/events"),
        Arrays.asList(AuthTestData.EVENT_INFO_1, AuthTestData.EVENT_INFO_2),
        HttpStatus.OK, false);

    verify(controller).getDepositEvents(USER_ID_1, "deposit-id-1");
  }

  /*
      @GetMapping("/deposits/{depositid}/jobs")
    public List<Job> getDepositJobs(@RequestHeader(value = "X-UserID", required = true) String userID,
                                    @PathVariable("depositid") String depositID) throws Exception {
   */
  @Test
  void testGetDepositJobs() throws Exception {
    when(controller.getDepositJobs(USER_ID_1, "deposit-id-1")).thenReturn(
        Arrays.asList(AuthTestData.JOB_1, AuthTestData.JOB_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/deposits/deposit-id-1/jobs"),
        Arrays.asList(AuthTestData.JOB_1, AuthTestData.JOB_2),
        HttpStatus.OK, false);

    verify(controller).getDepositJobs(USER_ID_1, "deposit-id-1");
  }

  /*
    @GetMapping("/deposits/{depositid}/manifest")
    public List<FileFixity> getDepositManifest(@RequestHeader(value = "X-UserID", required = true) String userID,
                                               @PathVariable("depositid") String depositID) throws Exception {
   */
  @Test
  void testGetDepositManifest() throws Exception {
    when(controller.getDepositManifest(USER_ID_1, "deposit-id-1")).thenReturn(
        Arrays.asList(AuthTestData.FILE_FIXITY_1,
            AuthTestData.FILE_FIXITY_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/deposits/deposit-id-1/manifest"),
        Arrays.asList(AuthTestData.FILE_FIXITY_1, AuthTestData.FILE_FIXITY_2),
        HttpStatus.OK, false);

    verify(controller).getDepositManifest(USER_ID_1, "deposit-id-1");
  }

  /*
    @GetMapping("/deposits/{depositid}/retrieves")
    public List<Retrieve> getDepositRetrieves(@RequestHeader(value = "X-UserID", required = true) String userID,
                                            @PathVariable("depositid") String depositID) throws Exception {
   */
  @Test
  void testGetDepositRetrieves() throws Exception {
    when(controller.getDepositRetrieves(USER_ID_1, "deposit-id-1")).thenReturn(
        Arrays.asList(AuthTestData.RETRIEVE_1,
            AuthTestData.RETRIEVE_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/deposits/deposit-id-1/retrieves"),
        Arrays.asList(AuthTestData.RETRIEVE_1, AuthTestData.RETRIEVE_2),
        HttpStatus.OK, false);

    verify(controller).getDepositRetrieves(USER_ID_1, "deposit-id-1");
  }

  /*
    @PostMapping("/deposits/{depositid}/restart")
    public Deposit restartDeposit(@RequestHeader(value = "X-UserID", required = true) String userID,
                                   @PathVariable("depositid") String depositID) throws Exception{
   */
  @Test
  void testPostRestartDeposit() throws Exception {
    when(controller.restartDeposit(USER_ID_1, "deposit-id-1")).thenReturn(AuthTestData.DEPOSIT_1);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/deposits/deposit-id-1/restart"),
        AuthTestData.DEPOSIT_1,
        HttpStatus.OK, false);

    verify(controller).restartDeposit(USER_ID_1, "deposit-id-1");
  }

  /*
      //TODO - from DavidHay - the name of this method seems wrong
    @PostMapping( "/deposits/{depositid}/retrieve")
    public Boolean retrieveDeposit(@RequestHeader(value = "X-UserID", required = true) String userID,
                                  @PathVariable("depositid") String depositID,
                                  @RequestBody Retrieve retrieve) throws Exception {
   */
  @Test
  void testPostDepositRetrieve() throws Exception {
    when(controller.retrieveDeposit(argUserId.capture(), argDepositId.capture(),
        argRetrieve.capture())).thenReturn(false);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/deposits/deposit-id-1/retrieve")
            .content(mapper.writeValueAsString(AuthTestData.RETRIEVE_1))
            .contentType(MediaType.APPLICATION_JSON),
        false,
        HttpStatus.OK, false);

    verify(controller).retrieveDeposit(argUserId.getValue(), argDepositId.getValue(),
        argRetrieve.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
    assertEquals("deposit-id-1", argDepositId.getValue());
    assertEquals(AuthTestData.RETRIEVE_1.getID(), argRetrieve.getValue().getID());
  }

}
