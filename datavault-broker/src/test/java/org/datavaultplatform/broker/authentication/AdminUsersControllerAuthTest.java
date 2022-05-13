package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.admin.AdminUsersController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.common.model.User;
import org.junit.jupiter.api.BeforeEach;
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
public class AdminUsersControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  AdminUsersController controller;

  User user1;

  User user2;

  @MockBean
  Sender sender;

  @BeforeEach
  void setup() {
    user1 = new User();
    user1.setLastname("last1");
    user1.setFirstname("first1");
    user1.setEmail("user.one@test.com");
    user1.setID("001");

    user2 = new User();
    user2.setLastname("last2");
    user2.setFirstname("first2");
    user2.setEmail("user.two@test.com");
    user2.setID("002");
  }

  @Test
  void testGetAdminUsers() {

    when(controller.getUsers(
        USER_ID_1, "query123")).thenReturn(Arrays.asList(user1, user2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/users/search").param("query", "query123"),
        Arrays.asList(user1, user2), HttpStatus.OK, true);

    verify(controller).getUsers(USER_ID_1, "query123");
  }

  @Test
  void testGetAdminUsersCount() {

    when(controller.getUsersCount(
        USER_ID_1)).thenReturn(2112);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/users/count"), 2112, HttpStatus.OK, true);

    verify(controller).getUsersCount(USER_ID_1);
  }

  @Test
  void testPostAdminUsers() throws JsonProcessingException {

    when(controller.addUser(
        USER_ID_1, user1)).thenReturn(user2);

    checkWorksWhenAuthenticatedFailsOtherwise(
        post("/admin/users")
            .content(mapper.writeValueAsString(user1))
            .contentType(MediaType.APPLICATION_JSON),
        user2, HttpStatus.OK, true);

    verify(controller).addUser(USER_ID_1, user1);
  }

  @Test
  void testPutAdminUsers() throws JsonProcessingException {

    when(controller.editUser(
        USER_ID_1, user1)).thenReturn(user2);

    checkWorksWhenAuthenticatedFailsOtherwise(
        put("/admin/users")
            .content(mapper.writeValueAsString(user1))
            .contentType(MediaType.APPLICATION_JSON),
        user2, HttpStatus.OK, true);

    verify(controller).editUser(USER_ID_1, user1);
  }

}
