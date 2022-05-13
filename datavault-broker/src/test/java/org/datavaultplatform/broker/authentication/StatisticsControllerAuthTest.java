package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.StatisticsController;
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
public class StatisticsControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @MockBean
  StatisticsController controller;

  /*
    @GetMapping(value = "/statistics/depositcount")
    public int getDepositsCount(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetDepositsCount(){
    when(controller.getDepositsCount(USER_ID_1)).thenReturn(1234);


    checkWorksWhenAuthenticatedFailsOtherwise(get("/statistics/depositcount"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getDepositsCount(USER_ID_1);
  }
  /*
    @GetMapping("/vaults/depositinprogress")
    public List<Deposit> getDepositsInProgress(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetDepositsInProgress(){
    when(controller.getDepositsInProgress(USER_ID_1)).thenReturn(Arrays.asList(AuthTestData.DEPOSIT_1,  AuthTestData.DEPOSIT_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/depositinprogress"),
        Arrays.asList(AuthTestData.DEPOSIT_1,  AuthTestData.DEPOSIT_2), HttpStatus.OK,
        false);

    verify(controller).getDepositsInProgress(USER_ID_1);
  }

  /*
    @GetMapping("/vaults/depositqueuecount")
    public int getDepositsQueueCount(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetDepositsQueueCount(){
    when(controller.getDepositsQueueCount(USER_ID_1)).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/depositqueuecount"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getDepositsQueueCount(USER_ID_1);
  }
  /*
    @GetMapping("/statistics/depositinprogresscount")
    public int getDepositsInProgressCount(@RequestHeader(value = "X-UserID", required = true) String userID) {
    */
  @Test
  void testGetDepositsInProgressCount() {
    when(controller.getDepositsInProgressCount(USER_ID_1)).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/statistics/depositinprogresscount"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getDepositsInProgressCount(USER_ID_1);
  }

  /*
    @GetMapping("/statistics/eventcount")
    public int getEventCount(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetEventCount() {
    when(controller.getEventCount(USER_ID_1)).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/statistics/eventcount"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getEventCount(USER_ID_1);
  }
  /*
    @GetMapping(value = "/vaults/retentionpolicycount/{status}")
    public int getPolicyStatusCount(@RequestHeader(value = "X-UserID", required = true) String userID,
                                    @PathVariable("status") int status) {
   */
  @Test
  void testGetPolicyStatusCount(){

    when(controller.getPolicyStatusCount(USER_ID_1,111)).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/retentionpolicycount/111"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getPolicyStatusCount(USER_ID_1,111);
  }

  /*
    @GetMapping("/statistics/retrievecount")
   */
  @Test
  void testGetRetrievesCount() {
    when(controller.getRetrievesCount(USER_ID_1)).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/statistics/retrievecount"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getRetrievesCount(USER_ID_1);
  }
  /*
    @GetMapping("/vaults/retrieveinprogress")
    public List<Retrieve> getRetrievesInProgress(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetRetrievesInProgress() {
    when(controller.getRetrievesInProgress(USER_ID_1)).thenReturn(Arrays.asList(AuthTestData.RETRIEVE_1,AuthTestData.RETRIEVE_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/retrieveinprogress"),
        Arrays.asList(AuthTestData.RETRIEVE_1,AuthTestData.RETRIEVE_2), HttpStatus.OK,
        false);

    verify(controller).getRetrievesInProgress(USER_ID_1);
  }
  /*
    @GetMapping("/statistics/retrieveinprogresscount")
    public int getRetrievesInProgressCount(@RequestHeader(value = "X-UserID", required = true) String userID) {
  */
  @Test
  void testGetRetrievesInProgressCount() {
    when(controller.getRetrievesInProgressCount(USER_ID_1)).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/statistics/retrieveinprogresscount"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getRetrievesInProgressCount(USER_ID_1);
  }
  /*
    @GetMapping("/vaults/retrievequeuecount")
    public int getRetrievesQueuedCount(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetRetrievesQueuedCount() {
    when(controller.getRetrievesQueuedCount(USER_ID_1)).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/retrievequeuecount"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getRetrievesQueuedCount(USER_ID_1);
  }
  /*
    @GetMapping("/statistics/pendingVaultsTotal")
    public int getTotalNumberOfPendingVaults() {
   */
  @Test
  void testGetTotalNumberOfPendingVaults() {
    when(controller.getTotalNumberOfPendingVaults()).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/statistics/pendingVaultsTotal"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getTotalNumberOfPendingVaults();
  }
  /*
    @GetMapping("/statistics/count")
    public int getVaultsCount(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testVaultsCount() {
    when(controller.getVaultsCount(USER_ID_1)).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/statistics/count"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getVaultsCount(USER_ID_1);
  }
  /*
    @GetMapping("/statistics/size")
    public Long getVaultsSize(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testVaultsSize() {
    when(controller.getVaultsSize(USER_ID_1)).thenReturn(1234L);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/statistics/size"),
        1234, HttpStatus.OK,
        false);

    verify(controller).getVaultsSize(USER_ID_1);
  }

  /*
getVaultsSize
   */

}
