package org.datavaultplatform.broker.authentication;

import static org.datavaultplatform.broker.authentication.AuthTestData.ARCHIVE_STORE_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import java.util.List;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.admin.AdminArchiveStoreController;
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
public class AdminArchiveStoreControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @MockBean
  AdminArchiveStoreController controller;
  @Captor
  ArgumentCaptor<ArchiveStore> argArchiveStore;
  @Captor
  ArgumentCaptor<String> argUserId;

  /*
      @GetMapping(value = "/admin/archivestores")
    public ResponseEntity<List<ArchiveStore>> getArchiveStores(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetArchiveStores() {

    ResponseEntity<List<ArchiveStore>> result = ResponseEntity.ok(
        Arrays.asList(ARCHIVE_STORE_1, AuthTestData.ARCHIVE_STORE_2));

    when(controller.getArchiveStores(USER_ID_1)).thenReturn(result);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/archivestores"), Arrays.asList(
            ARCHIVE_STORE_1, AuthTestData.ARCHIVE_STORE_2), HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_ARCHIVE_STORES);

    verify(controller).getArchiveStores(USER_ID_1);
  }

  /*
      @GetMapping("/admin/archivestores/{archivestoreid}")
    public ResponseEntity<ArchiveStore> getArchiveStore(@RequestHeader(value = "X-UserID", required = true) String userID, @PathVariable("archivestoreid") String archivestoreid) {
   */
  @Test
  void testGetArchiveStore() {

    ResponseEntity<ArchiveStore> result = ResponseEntity.ok(ARCHIVE_STORE_1);
    when(controller.getArchiveStore(USER_ID_1, "2112")).thenReturn(result);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/admin/archivestores/2112"), ARCHIVE_STORE_1,
        HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_ARCHIVE_STORES);

    verify(controller).getArchiveStore(USER_ID_1, "2112");
  }

  /*
      @GetMapping("/admin/archivestores")
    public ResponseEntity<ArchiveStore> addArchiveStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                        @RequestBody ArchiveStore store) {

   */
  @Test
  void testPostArchiveStores() throws JsonProcessingException {
    ResponseEntity<ArchiveStore> result = ResponseEntity.ok(AuthTestData.ARCHIVE_STORE_2);
    when(controller.addArchiveStore(argUserId.capture(), argArchiveStore.capture())).thenReturn(
        result);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/admin/archivestores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(ARCHIVE_STORE_1)),
        AuthTestData.ARCHIVE_STORE_2, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_ARCHIVE_STORES);

    verify(controller).addArchiveStore(argUserId.getValue(), argArchiveStore.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
    assertEquals(ARCHIVE_STORE_1.getID(), argArchiveStore.getValue().getID());
  }

  /*
      @GetMapping("/admin/archivestores")
    public ResponseEntity<ArchiveStore> addArchiveStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                        @RequestBody ArchiveStore store) {

   */
  @Test
  void testPutArchiveStores() throws JsonProcessingException {
    ResponseEntity<ArchiveStore> result = ResponseEntity.ok(AuthTestData.ARCHIVE_STORE_2);
    when(controller.editArchiveStore(argUserId.capture(), argArchiveStore.capture())).thenReturn(
        result);

    checkWorksWhenAuthenticatedFailsOtherwise(put("/admin/archivestores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(ARCHIVE_STORE_1)),
        AuthTestData.ARCHIVE_STORE_2, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_ARCHIVE_STORES);

    verify(controller).editArchiveStore(argUserId.getValue(), argArchiveStore.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
    assertEquals(ARCHIVE_STORE_1.getID(), argArchiveStore.getValue().getID());
  }

  /*
      @DeleteMapping("/admin/archivestores/{archivestoreid}")
    public ResponseEntity<Void>  deleteArchiveStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                      @PathVariable("archivestoreid") String archivestoreid) {
   */
  @Test
  void testDeleteArchiveStore() throws JsonProcessingException {
    when(controller.deleteArchiveStore(USER_ID_1, "2112")).thenReturn(
        ResponseEntity.ok().body(null));

    checkWorksWhenAuthenticatedFailsOtherwise(delete("/admin/archivestores/2112"),
        null, HttpStatus.OK,
        false,
        Permission.CAN_MANAGE_ARCHIVE_STORES);

    verify(controller).deleteArchiveStore(USER_ID_1, "2112");
  }
}
