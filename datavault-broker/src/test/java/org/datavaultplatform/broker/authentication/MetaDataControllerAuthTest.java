package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.MetadataController;
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
public class MetaDataControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @MockBean
  MetadataController controller;

  /*
    @GetMapping("/metadata/datasets/{datasetid}")
    public Dataset getDataset(@RequestHeader(value = "X-UserID", required = true) String userID,
                              @PathVariable("datasetid") String datasetID) {
    */
  @Test
  void testGetDataset() {

    when(controller.getDataset(USER_ID_1, "dataset-id-1")).thenReturn(AuthTestData.DATASET_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/metadata/datasets/dataset-id-1"),
        AuthTestData.DATASET_1,
        HttpStatus.OK, false);

    verify(controller).getDataset(USER_ID_1, "dataset-id-1");
  }

  /*
    @GetMapping(value = "/metadata/datasets")
    public List<Dataset> getDatasets(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetDatasets() {

    when(controller.getDatasets(USER_ID_1)).thenReturn(
        Arrays.asList(AuthTestData.DATASET_1, AuthTestData.DATASET_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/metadata/datasets"),
        Arrays.asList(AuthTestData.DATASET_1, AuthTestData.DATASET_2),
        HttpStatus.OK, false);

    verify(controller).getDatasets(USER_ID_1);
  }

}
