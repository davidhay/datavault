package org.datavaultplatform.broker.authentication;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.FileStoreController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.common.model.FileStore;
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
public class FileStoreControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @MockBean
  FileStoreController controller;

  @Captor
  ArgumentCaptor<String> argUserId;

  @Captor
  ArgumentCaptor<FileStore> argFileStore;

  /*
    @PostMapping("/filestores")
    public ResponseEntity<FileStore> addFileStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                  @RequestBody FileStore store) {
   */
  @Test
  void testPostAddFileStore() throws Exception {
    when(controller.addFileStore(argUserId.capture(), argFileStore.capture())).thenReturn(
        new ResponseEntity(
            AuthTestData.FILESTORE_1, HttpStatus.CREATED));

    checkWorksWhenAuthenticatedFailsOtherwise(post("/filestores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AuthTestData.FILESTORE_1)),
        AuthTestData.FILESTORE_1, HttpStatus.CREATED,
        false);

    verify(controller).addFileStore(argUserId.getValue(), argFileStore.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
    assertEquals(AuthTestData.FILESTORE_1.getLabel(), argFileStore.getValue().getLabel());
  }

  /*
    @PostMapping("/filestores/sftp")
    public ResponseEntity<FileStore> addFileStoreSFTP(@RequestHeader(value = "X-UserID", required = true) String userID,
                                      @RequestBody FileStore store) {
   */
  @Test
  void testPostAddFileStoreSFTP() throws JsonProcessingException {
    when(controller.addFileStoreSFTP(argUserId.capture(), argFileStore.capture())).thenReturn(
        new ResponseEntity(
            AuthTestData.FILESTORE_1, HttpStatus.CREATED));

    checkWorksWhenAuthenticatedFailsOtherwise(post("/filestores/sftp")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AuthTestData.FILESTORE_1)),
        AuthTestData.FILESTORE_1, HttpStatus.CREATED,
        false);

    verify(controller).addFileStoreSFTP(argUserId.getValue(), argFileStore.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
    assertEquals(AuthTestData.FILESTORE_1.getLabel(), argFileStore.getValue().getLabel());
  }

  /*
    @DeleteMapping("/filestores/{filestoreid}")
    public ResponseEntity<Void>  deleteFileStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                               @PathVariable("filestoreid") String filestoreid) {
  */
  @Test
  void testDeleteFileStore() {
    when(controller.deleteFileStore(USER_ID_1, "file-store-id-xxx")).thenReturn(
        ResponseEntity.noContent().build());

    checkWorksWhenAuthenticatedFailsOtherwise(delete("/filestores/file-store-id-xxx"),
        null, HttpStatus.NO_CONTENT,
        false);

    verify(controller).deleteFileStore(USER_ID_1, "file-store-id-xxx");
  }

  /*
    @GetMapping("/filestores/{filestoreid}")
    public ResponseEntity<FileStore> getFileStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                               @PathVariable("filestoreid") String filestoreid) {
   */
  @Test
  void testGetFileStore() {
    when(controller.getFileStore(USER_ID_1, "file-store-id-xxx")).thenReturn(
        ResponseEntity.ok(AuthTestData.FILESTORE_1));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/filestores/file-store-id-xxx"),
        AuthTestData.FILESTORE_1, HttpStatus.OK,
        false);

    verify(controller).getFileStore(USER_ID_1, "file-store-id-xxx");
  }

  /*
    @GetMapping("/filestores")
    public ResponseEntity<List<FileStore>> getFileStores(@RequestHeader(value = "X-UserID", required = true) String userID) {
 */
  @Test
  void testGetFileStores() {
    when(controller.getFileStores(USER_ID_1)).thenReturn(
        ResponseEntity.ok(Arrays.asList(AuthTestData.FILESTORE_1, AuthTestData.FILESTORE_2)));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/filestores"),
        Arrays.asList(AuthTestData.FILESTORE_1, AuthTestData.FILESTORE_2), HttpStatus.OK,
        false);

    verify(controller).getFileStores(USER_ID_1);
  }

  /*
    @GetMapping("/filestores/local")
    public ResponseEntity<List<FileStore>> getFileStoresLocal(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetFileStoresLocal() {
    when(controller.getFileStoresLocal(USER_ID_1)).thenReturn(
        ResponseEntity.ok(Arrays.asList(AuthTestData.FILESTORE_1, AuthTestData.FILESTORE_2)));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/filestores/local"),
        Arrays.asList(AuthTestData.FILESTORE_1, AuthTestData.FILESTORE_2), HttpStatus.OK,
        false);

    verify(controller).getFileStoresLocal(USER_ID_1);
  }

  /*
    @GetMapping("/filestores/sftp")
    public ResponseEntity<List<FileStore>> getFileStoresSFTP(@RequestHeader(value = "X-UserID", required = true) String userID) {
 */
  @Test
  void testGetFileStoresSFTP() {
    when(controller.getFileStoresSFTP(USER_ID_1)).thenReturn(
        ResponseEntity.ok(Arrays.asList(AuthTestData.FILESTORE_1, AuthTestData.FILESTORE_2)));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/filestores/sftp"),
        Arrays.asList(AuthTestData.FILESTORE_1, AuthTestData.FILESTORE_2), HttpStatus.OK,
        false);

    verify(controller).getFileStoresSFTP(USER_ID_1);
  }

  /*
    @GetMapping("/filestores/sftp/{filestoreid}")
    public ResponseEntity<FileStore> getFilestoreSFTP(@RequestHeader(value = "X-UserID", required = true) String userID,
                                   @PathVariable("filestoreid") String filestoreid) {
  */
  @Test
  void testGetFileStoreSFTP() {
    when(controller.getFilestoreSFTP(USER_ID_1, "file-store-id")).thenReturn(
        ResponseEntity.ok(AuthTestData.FILESTORE_1));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/filestores/sftp/file-store-id"),
        AuthTestData.FILESTORE_1, HttpStatus.OK,
        false);

    verify(controller).getFilestoreSFTP(USER_ID_1, "file-store-id");
  }
}
