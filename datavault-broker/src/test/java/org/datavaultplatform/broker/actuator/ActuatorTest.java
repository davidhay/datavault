package org.datavaultplatform.broker.actuator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.services.FileStoreService;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.TestClockConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(classes = DataVaultBrokerApp.class)
@Import({TestClockConfig.class, MockServicesConfig.class})
@AddTestProperties
@Slf4j
@TestPropertySource(properties = {
    "logging.level.org.springframework.security.web=trace",
    "broker.database.enabled=false",
    "spring.security.debug=true",
    "broker.email.enabled=true",
    "broker.controllers.enabled=true",
    "broker.initialise.enabled=false",
    "broker.rabbit.enabled=false",
    "broker.scheduled.enabled=false",
    "management.endpoints.web.exposure.include=*",
    "management.health.rabbit.enabled=false",
    "spring.autoconfigure.exclude="
        + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
        + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
        + "org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration"
    })
@AutoConfigureMockMvc
public class ActuatorTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  Sender sender;

  @Autowired
  ObjectMapper mapper;

  @Autowired
  FileStoreService mFileStoreService;

  @Test
  void setup() {
    when(mFileStoreService.getFileStores()).thenReturn(Collections.emptyList());
  }

  @Test
  @SneakyThrows
  void testActuatorPublicAccess() {
    Stream.of("/actuator/info", "/actuator/health").forEach(this::checkPublic);
  }

  @Test
  @SneakyThrows
  void testActuatorUnauthorized() {
    Stream.of("/actuator", "/actuator/", "/actuator/env", "/users")
        .forEach(this::checkUnauthorized);
  }

  @Test
  @SneakyThrows
  void testActuatorAuthorized() {
    Stream.of("/actuator", "/actuator/env", "/actuator/customtime",
            "/actuator/sftpfilestores", "/actuator/localfilestores")
        .forEach(url -> checkAuthorized(url, "bactor", "bactorpass"));
  }

  @SneakyThrows
  void checkUnauthorized(String url) {
    mvc.perform(get(url)).andDo(print()).andExpect(status().isUnauthorized());
  }

  @SneakyThrows
  void checkAuthorized(String url, String username, String password) {
    mvc.perform(get(url).with(httpBasic(username, password))).andDo(print())
        .andExpect(status().isOk());
  }

  @SneakyThrows
  void checkPublic(String url) {
    mvc.perform(get(url)).andDo(print()).andExpect(status().isOk());
  }

  @Test
  void testCurrentTime() throws Exception {
    MvcResult mvcResult = mvc.perform(
            get("/actuator/customtime")
                .with(httpBasic("bactor", "bactorpass")))
        .andExpect(content().contentTypeCompatibleWith("application/vnd.spring-boot.actuator.v3+json"))
        .andExpect(jsonPath("$.current-time").exists())
        .andReturn();

    String json = mvcResult.getResponse().getContentAsString();
    Map<String,String> infoMap = mapper.createParser(json).readValueAs(Map.class);

    assertTrue(infoMap.containsKey("current-time"));
    String ct = infoMap.get("current-time");
    Assertions.assertEquals("Tue Mar 29 14:15:16 BST 2022",ct);
  }

}
