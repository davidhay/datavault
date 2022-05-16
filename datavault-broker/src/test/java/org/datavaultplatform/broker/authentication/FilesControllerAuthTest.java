package org.datavaultplatform.broker.authentication;


import static org.datavaultplatform.broker.authentication.AuthTestData.FILE_INFO_1;
import static org.datavaultplatform.broker.authentication.AuthTestData.FILE_INFO_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.FilesController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
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
public class FilesControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  FilesController controller;

  @MockBean
  Sender sender;

  @Captor
  ArgumentCaptor<String> argUserId;
  @Captor
  ArgumentCaptor<String> argStorageId;

  @Captor
  ArgumentCaptor<String> argFilename;

  @Captor
  ArgumentCaptor<String> argFileUploadHandle;

  @Captor
  ArgumentCaptor<HttpServletRequest> argRequest;

  /*
    @GetMapping("/checkdepositsize")
    public DepositSize checkDepositSize(@RequestHeader(value = "X-UserID", required = true) String userID,
                              HttpServletRequest request) throws Exception {
   */
  @Test
  void testGetCheckDepositSize() throws Exception {

    when(controller.checkDepositSize(argUserId.capture(), argRequest.capture())).thenReturn(
        AuthTestData.DEPOSIT_SIZE);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/checkdepositsize"), AuthTestData.DEPOSIT_SIZE,
        HttpStatus.OK, false);

    verify(controller).checkDepositSize(argUserId.getValue(), argRequest.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
  }


  /*
    @GetMapping("/files/{storageid}/**")
    public List<FileInfo> getFilesListing(@RequestHeader(value = "X-UserID", required = true) String userID,
                                          HttpServletRequest request,
                                          @PathVariable("storageid") String storageID) throws Exception {
   */
  @Test
  void testGetFilesListing() throws Exception {

    when(controller.getFilesListing(argUserId.capture(), argRequest.capture(),
        argStorageId.capture())).thenReturn(
        Arrays.asList(AuthTestData.FILE_INFO_1, AuthTestData.FILE_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/files/storage-id-xxx"),
        Arrays.asList(AuthTestData.FILE_INFO_1, AuthTestData.FILE_INFO_2),
        HttpStatus.OK, false);

    verify(controller).getFilesListing(argUserId.getValue(), argRequest.getValue(),
        argStorageId.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
    assertEquals("storage-id-xxx", argStorageId.getValue());
  }

  /*
    @GetMapping("/filesize/{storageid}/**")
    public String getFilesize(@RequestHeader(value = "X-UserID", required = true) String userID,
                                      HttpServletRequest request,
                                      @PathVariable("storageid") String storageID) throws Exception {
   */
  @Test
  void testGetFileSize() throws Exception {

    when(controller.getFilesize(argUserId.capture(), argRequest.capture(),
        argStorageId.capture())).thenReturn("file-size");

    checkWorksWhenAuthenticatedFailsOtherwise(get("/filesize/storage-id-xxx"),
        "file-size",
        HttpStatus.OK, false);

    verify(controller).getFilesize(argUserId.getValue(), argRequest.getValue(),
        argStorageId.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
    assertEquals("storage-id-xxx", argStorageId.getValue());
  }

  /*
    @GetMapping("/files")
    public List<FileInfo> getStorageListing(@RequestHeader(value = "X-UserID", required = true) String userID,
                                            HttpServletRequest request) {
   */
  @Test
  void testGetStorageListing() throws Exception {

    when(controller.getStorageListing(argUserId.capture(), argRequest.capture())).thenReturn(
        Arrays.asList(FILE_INFO_1, FILE_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/files"),
        Arrays.asList(FILE_INFO_1, FILE_INFO_2),
        HttpStatus.OK, false);

    verify(controller).getStorageListing(argUserId.getValue(), argRequest.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
  }

  /*
    @PostMapping(value="/upload/{fileUploadHandle}/{filename:.+}")
    public String postFileChunk(@RequestHeader(value = "X-UserID", required = true) String userID,
                                HttpServletRequest request,
                                @PathVariable("fileUploadHandle") String fileUploadHandle,
                                @PathVariable("filename") String filename) throws Exception {
   */
  @Test
  void testPostFileChunk() throws Exception {

    when(controller.postFileChunk(argUserId.capture(), argRequest.capture(),
        argFileUploadHandle.capture(), argFilename.capture()))
        .thenReturn("some-result-string");

    checkWorksWhenAuthenticatedFailsOtherwise(post("/upload/file-handle-1/some-filename"),
        "some-result-string",
        HttpStatus.OK, false);

    verify(controller).postFileChunk(argUserId.capture(), argRequest.capture(),
        argFileUploadHandle.capture(), argFilename.capture());
  }


}
