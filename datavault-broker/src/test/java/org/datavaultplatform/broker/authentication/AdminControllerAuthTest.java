package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.admin.AdminController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.common.model.ArchiveStore;
import org.datavaultplatform.common.model.Permission;
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
public class AdminControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @MockBean
  AdminController controller;
  @Captor
  ArgumentCaptor<ArchiveStore> argArchiveStore;
  @Captor
  ArgumentCaptor<String> argUserId;

  @Captor
  ArgumentCaptor<HttpServletRequest> argHttpRequest;

  /*
    @RequestMapping(value = "/admin/deposits/count", method = RequestMethod.GET)
    public Integer getDepositsAll(@RequestHeader(value = "X-UserID", required = true) String userID,
                                  @RequestParam(value = "query", required = false)
                                  @ApiQueryParam(name = "query",
                                          description = "Deposit query field",
                                          required = false) String query) throws Exception {
    }
   */
  @Test
  void testGetAdminDepositsCount() {

    when(controller.getDepositsAll(USER_ID_1, "query1")).thenReturn(2112);

    checkWorksWhenAuthenticatedFailsOtherwise(
        get("/admin/deposits/count").queryParam("query", "query1"), 2112, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_DEPOSITS);

    verify(controller).getDepositsAll(USER_ID_1, "query1");

  }

  @Test
  void testGetAuditsAll() throws Exception {

    when(controller.getAuditsAll(USER_ID_1)).thenReturn(
        Arrays.asList(AuthTestData.AUDIT_INFO_1, AuthTestData.AUDIT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/audits"),
        Arrays.asList(AuthTestData.AUDIT_INFO_1, AuthTestData.AUDIT_INFO_2), HttpStatus.OK,
        false);

    verify(controller).getAuditsAll(USER_ID_1);

  }

  /*
      @GetMapping("/admin/deposits")
      public List<DepositInfo> getDepositsAll(@RequestHeader(value = "X-UserID", required = true) String userID,
                                            @RequestParam(value = "query", required = false) String query,
                                            @RequestParam(value = "sort", required = false) String sort,
                                            @RequestParam(value = "order", required = false) String order,
                                            @RequestParam(value = "offset", required = false) int offset,
                                            @RequestParam(value = "maxResult", required = false) int maxResult) {
   */
  @Test
  void testGetDepositsAll() {

    when(controller.getDepositsAll(USER_ID_1, "query1", "sort1", "order1", 123, 1234)).thenReturn(
        Arrays.asList(AuthTestData.DEPOSIT_INFO_1, AuthTestData.DEPOSIT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/deposits")
            .queryParam("query", "query1")
            .queryParam("sort", "sort1")
            .queryParam("order", "order1")
            .queryParam("offset", "123")
            .queryParam("maxResult", "1234"),
        Arrays.asList(AuthTestData.DEPOSIT_INFO_1, AuthTestData.DEPOSIT_INFO_2),
        HttpStatus.OK,
        false, Permission.CAN_MANAGE_DEPOSITS);

    verify(controller).getDepositsAll(USER_ID_1, "query1", "sort1", "order1", 123, 1234);
  }

  /*
      @GetMapping(value = "/admin/deposits/count")
    public Integer getDepositsAll(@RequestHeader(value = "X-UserID", required = true) String userID,
                                  @RequestParam(value = "query", required = false) String query) {
   */
  @Test
  void testGetAllDepositsCount() {
    when(controller.getDepositsAll(USER_ID_1, "query1")).thenReturn(2112);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/deposits/count")
            .queryParam("query", "query1"),
        2112,
        HttpStatus.OK,
        false, Permission.CAN_MANAGE_DEPOSITS);

    verify(controller).getDepositsAll(USER_ID_1, "query1");

  }

  /*
      @GetMapping("/admin/deposits/data")
    public DepositsData getDepositsAllData(@RequestHeader(value = "X-UserID", required = true) String userID,
                                           @RequestParam(value = "sort", required = false) String sort
   */
  @Test
  void testGetDepositsAllData() {
    when(controller.getDepositsAllData(USER_ID_1, "sort1")).thenReturn(AuthTestData.DEPOSIT_DATA_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/deposits/data")
            .queryParam("sort", "sort1"),
        AuthTestData.DEPOSIT_DATA_1,
        HttpStatus.OK,
        false, Permission.CAN_MANAGE_DEPOSITS);

    verify(controller).getDepositsAllData(USER_ID_1, "sort1");
  }

  /*
    @GetMapping("/admin/deposits/audit")
    public String runDepositAudit(@RequestHeader(value = "X-UserID", required = true) String userID,
        HttpServletRequest request) {
   */
  @Test
  void testRunAudit() throws JsonProcessingException {

    when(controller.runDepositAudit(argUserId.capture(), argHttpRequest.capture())).thenReturn(
        "Success");

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/deposits/audit"),
        "Success",
        HttpStatus.OK,
        false, Permission.CAN_MANAGE_DEPOSITS);

    verify(controller).runDepositAudit(USER_ID_1, argHttpRequest.getValue());

  }

  /*
      @GetMapping(value = "/admin/events")
    public List<EventInfo> getEventsAll(@RequestHeader(value = "X-UserID", required = true) String userID,
                                        @RequestParam(value = "sort", required = false) String sort) {
   */
  @Test
  void testGetAdminEvents() {

    when(controller.getEventsAll(USER_ID_1, "sort1")).thenReturn(
        Arrays.asList(AuthTestData.EVENT_INFO_1, AuthTestData.EVENT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/events").queryParam("sort", "sort1"),
        Arrays.asList(AuthTestData.EVENT_INFO_1, AuthTestData.EVENT_INFO_2),
        HttpStatus.OK,
        false, Permission.CAN_VIEW_EVENTS);

    verify(controller).getEventsAll(USER_ID_1, "sort1");
  }

  /*
      @GetMapping("/admin/retrieves")
    public List<Retrieve> getRetrievesAll(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetRetrievesAll() {

    when(controller.getRetrievesAll(USER_ID_1)).thenReturn(Arrays.asList(AuthTestData.RETRIEVE_1,
        AuthTestData.RETRIEVE_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/retrieves"),
        Arrays.asList(AuthTestData.RETRIEVE_1, AuthTestData.RETRIEVE_2),
        HttpStatus.OK,
        false, Permission.CAN_VIEW_RETRIEVES);

    verify(controller).getRetrievesAll(USER_ID_1);
  }

  /*
      @GetMapping(value = "/admin/vaults")
    public VaultsData getVaultsAll(@RequestHeader(value = "X-UserID", required = true) String userID,
                                   @RequestParam(value = "sort", required = false) String sort,
                                   @RequestParam(value = "order", required = false) String order,
                                   @RequestParam(value = "offset", required = false) String offset,
                                   @RequestParam(value = "maxResult", required = false) String maxResult) {
   */
  @Test
  void testGetVaultsAll() {
    when(controller.getVaultsAll(USER_ID_1, "sort1", "order1", "123", "1234")).thenReturn(
        AuthTestData.VAULTS_DATA);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/vaults")
            .queryParam("sort", "sort1")
            .queryParam("order", "order1")
            .queryParam("offset", "123")
            .queryParam("maxResult", "1234"),
        AuthTestData.VAULTS_DATA,
        HttpStatus.OK,
        false, Permission.CAN_MANAGE_VAULTS);

    verify(controller).getVaultsAll(USER_ID_1, "sort1", "order1", "123", "1234");
  }
}
