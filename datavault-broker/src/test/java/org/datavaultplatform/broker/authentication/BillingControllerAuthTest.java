package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigDecimal;
import java.util.Date;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.admin.BillingController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.common.model.PendingVault.Billing_Type;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.User;
import org.datavaultplatform.common.response.BillingInformation;
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
public class BillingControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  BillingController controller;

  User user1;

  User user2;

  BillingInformation billInfo1;


  @MockBean
  Sender sender;

  @BeforeEach
  void setup() {
    billInfo1 = new BillingInformation();
    billInfo1.setAmountBilled(new BigDecimal("12.34"));
    billInfo1.setBillingType(Billing_Type.ORIG);
    billInfo1.setCreationTime(new Date());
    billInfo1.setBudgetCode(true);
    billInfo1.setId("123");
    billInfo1.setContactName("Joe Bloggs");
    billInfo1.setProjectId("project-123");
    billInfo1.setProjectSize(1234L);
    billInfo1.setProjectTitle("project-title");
    billInfo1.setReviewDate(new Date());
    billInfo1.setSchool("school of medicine");
    billInfo1.setSubUnit("ortho");
    billInfo1.setSliceID("slice1");
    billInfo1.setSpecialComments("special K");
    billInfo1.setUserName("user ABC");
    billInfo1.setVaultID("vault-id-1");
    billInfo1.setVaultName("vault-1-name");
    billInfo1.setVaultSize(9999L);

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
  void testGetAdminBilling() {
    when(controller.getVaultBillingInfo(
        USER_ID_1, "2112")).thenReturn(billInfo1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/billing/2112"), billInfo1, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_BILLING_DETAILS);

    verify(controller).getVaultBillingInfo(USER_ID_1, "2112");
  }

  @Test
  void testGetSearchAllBillingVaults() {
    when(controller.searchAllBillingVaults(
        USER_ID_1, "query1", "sort1", "order1", "offset1", "maxResult1")).thenReturn(
        AuthTestData.VAULTS_DATA);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/billing/search")
            .param("query", "query1")
            .param("sort", "sort1")
            .param("order", "order1")
            .param("offset", "offset1")
            .param("maxResult", "maxResult1")
        , AuthTestData.VAULTS_DATA, HttpStatus.OK, false,
        Permission.CAN_MANAGE_BILLING_DETAILS);

    verify(controller).searchAllBillingVaults(USER_ID_1, "query1", "sort1", "order1", "offset1",
        "maxResult1");
  }


  @Test
  void testPostAdminBilling() throws JsonProcessingException {
    when(controller.updateBillingDetails(
        USER_ID_1, "2112", billInfo1)).thenReturn(billInfo1);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/admin/billing/2112/updateBilling")
            .content(mapper.writeValueAsString(billInfo1))
            .contentType(MediaType.APPLICATION_JSON)
        , billInfo1, HttpStatus.OK, false,
        Permission.CAN_MANAGE_BILLING_DETAILS);

    verify(controller).updateBillingDetails(USER_ID_1, "2112", billInfo1);
  }
}
