package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.UsersController;
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
public class UsersControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @MockBean
  UsersController controller;

  /*
    @GetMapping( "/users")
    public List<User> getUsers(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetUsers() {
    when(controller.getUsers(USER_ID_1)).thenReturn(
        Arrays.asList(AuthTestData.USER_1, AuthTestData.USER_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/users"),
        Arrays.asList(AuthTestData.USER_1, AuthTestData.USER_2), HttpStatus.OK,
        false);

    verify(controller).getUsers(USER_ID_1);
  }

  /*
    @GetMapping("/users/{userid}")
    public User getUser(@PathVariable("userid") String queryUserID) {
   */
  @Test
  void testGetUser() {
    when(controller.getUser("query-user-id")).thenReturn(AuthTestData.USER_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/users/query-user-id"),
        AuthTestData.USER_1, HttpStatus.OK,
        false);

    verify(controller).getUser("query-user-id");
  }

  /*
    @PostMapping("/auth/users/exists")
    public Boolean exists(@RequestBody ValidateUser validateUser) {
   */
  @Test
  void testPostUserExists() throws JsonProcessingException {
    when(controller.exists(AuthTestData.VALIDATE_USER)).thenReturn(true);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/auth/users/exists")
            .content(mapper.writeValueAsString(AuthTestData.VALIDATE_USER))
            .contentType(MediaType.APPLICATION_JSON),
        true, HttpStatus.OK,
        false);

    verify(controller).exists(AuthTestData.VALIDATE_USER);
  }

  /*
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {  */
  @Test
  void testPostAddUser() throws JsonProcessingException {
    when(controller.addUser(AuthTestData.USER_1)).thenReturn(AuthTestData.USER_2);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/users")
            .content(mapper.writeValueAsString(AuthTestData.USER_1))
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.USER_2, HttpStatus.OK,
        false);

    verify(controller).addUser(AuthTestData.USER_1);
  }

  /*
    @PostMapping("/auth/users/isadmin")
    public Boolean isAdmin(@RequestBody ValidateUser validateUser) {
   */
  @Test
  void testPostIsAdmin() throws JsonProcessingException {

    when(controller.isAdmin(AuthTestData.VALIDATE_USER)).thenReturn(true);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/auth/users/isadmin")
            .content(mapper.writeValueAsString(AuthTestData.VALIDATE_USER))
            .contentType(MediaType.APPLICATION_JSON),
        true, HttpStatus.OK,
        false);

    verify(controller).isAdmin(AuthTestData.VALIDATE_USER);
  }

  /*
    @PostMapping("/auth/users/isvalid")
    public Boolean validateUser(@RequestBody ValidateUser validateUser) {
   */
  @Test
  void testPostValidateUser() throws JsonProcessingException {
    when(controller.validateUser(AuthTestData.VALIDATE_USER)).thenReturn(true);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/auth/users/isvalid")
            .content(mapper.writeValueAsString(AuthTestData.VALIDATE_USER))
            .contentType(MediaType.APPLICATION_JSON),
        true, HttpStatus.OK,
        false);

    verify(controller).validateUser(AuthTestData.VALIDATE_USER);
  }

}
