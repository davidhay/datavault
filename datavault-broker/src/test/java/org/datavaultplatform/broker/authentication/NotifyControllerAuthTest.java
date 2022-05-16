package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.NotifyController;
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
public class NotifyControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  NotifyController controller;

  @MockBean
  Sender sender;

  /*
    @PutMapping("/notify/login")
    public String login(@RequestHeader(value = "X-UserID", required = true) String userID,
                        @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                        @RequestBody CreateClientEvent clientEvent) {
   */
  @Test
  void testPutNotifyLogin() throws Exception {
    when(controller.login(USER_ID_1, API_KEY_1, AuthTestData.CREATE_CLIENT_EVENT)).thenReturn(
        "blah1");

    checkWorksWhenAuthenticatedFailsOtherwise(put("/notify/login")
            .content(mapper.writeValueAsString(AuthTestData.CREATE_CLIENT_EVENT))
            .contentType(MediaType.APPLICATION_JSON),
        "blah1",
        HttpStatus.OK, false);

    verify(controller).login(USER_ID_1, API_KEY_1, AuthTestData.CREATE_CLIENT_EVENT);
  }

  /*
    @PutMapping("/notify/logout")
    public String logout(@RequestHeader(value = "X-UserID", required = true) String userID,
                         @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                         @RequestBody CreateClientEvent clientEvent) {
   */
  @Test
  void testPutNotifyLogout() throws JsonProcessingException {
    when(controller.logout(USER_ID_1, API_KEY_1, AuthTestData.CREATE_CLIENT_EVENT)).thenReturn(
        "blah2");

    checkWorksWhenAuthenticatedFailsOtherwise(put("/notify/logout")
            .content(mapper.writeValueAsString(AuthTestData.CREATE_CLIENT_EVENT))
            .contentType(MediaType.APPLICATION_JSON),
        "blah2",
        HttpStatus.OK, false);

    verify(controller).logout(USER_ID_1, API_KEY_1, AuthTestData.CREATE_CLIENT_EVENT);
  }
}
