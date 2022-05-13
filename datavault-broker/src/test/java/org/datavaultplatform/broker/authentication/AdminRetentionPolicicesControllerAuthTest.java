package org.datavaultplatform.broker.authentication;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.admin.AdminRetentionPoliciesController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.common.request.CreateRetentionPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.util.MockUtil;
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
public class AdminRetentionPolicicesControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @Captor
  ArgumentCaptor<String> argUserId;

  @Captor
  ArgumentCaptor<String> argClientKey;

  @Captor
  ArgumentCaptor<String> argPolicyId;

  @Captor
  ArgumentCaptor<CreateRetentionPolicy> argPolicy;

  @MockBean
  AdminRetentionPoliciesController controller;

  CreateRetentionPolicy policy;

  @BeforeEach
  void checkSetup() {
    assertTrue(MockUtil.isMock(controller));
    policy = new CreateRetentionPolicy();
    policy.setDescription("desc");
    policy.setId(123);
    policy.setName("the-name");
    policy.setUrl("https://ed.ac.uk");
    policy.setMinRetentionPeriod(123);
    policy.setMinDataRetentionPeriod("min date retention period");
    policy.setEngine("V8");
    policy.setSort("bubble");
    policy.setExtendUponRetrieval(true);
    policy.setDateGuidanceReviewed("guidance reviewed date");
    policy.setEndDate("end date");
    policy.setInEffectDate("ineffect date");
  }

  @Test
  void testGetAdminRetentionPolicies() {

    when(controller.getRetentionPolicy(
        argUserId.capture(),
        argClientKey.capture(), argPolicyId.capture())).thenReturn(ResponseEntity.ok(policy));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/retentionpolicies/123"), policy, HttpStatus.OK, false);

    verify(controller).getRetentionPolicy(USER_ID_1, API_KEY_1, "123");
  }

  @Test
  @SneakyThrows
  void testPutRetentionPolicy() {

    when(controller.editRetentionPolicy(
        argUserId.capture(),
        argClientKey.capture(),
        argPolicy.capture())).thenReturn(ResponseEntity.ok(policy));

    checkWorksWhenAuthenticatedFailsOtherwise(
        put("/admin/retentionpolicies")
            .content(mapper.writeValueAsString(policy))
            .contentType(MediaType.APPLICATION_JSON), policy, HttpStatus.OK, false);

    verify(controller).editRetentionPolicy(USER_ID_1, API_KEY_1, policy);

  }

  @Test
  @SneakyThrows
  void testAddRetentionPolicy() {

    when(controller.addRetentionPolicy(
        argUserId.capture(),
        argClientKey.capture(),
        argPolicy.capture())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(policy));

    checkWorksWhenAuthenticatedFailsOtherwise(
        post("/admin/retentionpolicies")
            .content(mapper.writeValueAsString(policy))
            .contentType(MediaType.APPLICATION_JSON), policy, HttpStatus.CREATED, false);

    verify(controller).addRetentionPolicy(USER_ID_1, API_KEY_1, policy);

  }

  @Test
  void testDeleteAdminPolicy() {
    doNothing().when(controller).deleteRetentionPolicy(
        argUserId.capture(),
        argPolicyId.capture());

    checkWorksWhenAuthenticatedFailsOtherwise(delete("/admin/retentionpolicies/delete/123"), null, HttpStatus.OK, false);

    verify(controller).deleteRetentionPolicy(USER_ID_1, "123");

  }

}
