package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.VaultsController;
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
public class VaultsControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  VaultsController controller;

  @MockBean
  Sender sender;

  /*
      @GetMapping("/vaults/{vaultid}/dataManagers")
    public List<DataManager> getDataManagers(@RequestHeader(value = "X-UserID", required = true) String userID,
                                             @PathVariable("vaultid") String vaultID) throws Exception {
   */
  @Test
  void testGetDataManagers() throws Exception {

    when(controller.getDataManagers(USER_ID_1, "vault-id-1")).thenReturn(
        Arrays.asList(AuthTestData.DATA_MANAGER_1, AuthTestData.DATA_MANAGER_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/vault-id-1/dataManagers"),
        Arrays.asList(AuthTestData.DATA_MANAGER_1, AuthTestData.DATA_MANAGER_2), HttpStatus.OK,
        false);

    verify(controller).getDataManagers(USER_ID_1, "vault-id-1");
  }

  /*
    @GetMapping("/vaults/{vaultid}/deposits")
    public List<DepositInfo> getDeposits(@RequestHeader(value = "X-UserID", required = true) String userID,
                                         @PathVariable("vaultid") String vaultID) throws Exception {
  */
  @Test
  void testGetDeposits() throws Exception {
    when(controller.getDeposits(USER_ID_1, "vault-id-2")).thenReturn(
        Arrays.asList(AuthTestData.DEPOSIT_INFO_1, AuthTestData.DEPOSIT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/vault-id-2/deposits"),
        Arrays.asList(AuthTestData.DEPOSIT_INFO_1, AuthTestData.DEPOSIT_INFO_2), HttpStatus.OK,
        false);

    verify(controller).getDeposits(USER_ID_1, "vault-id-2");
  }

  /*
    @GetMapping("/vaults/{vaultid}/roleEvents")
    public List<EventInfo> getRoleEvents(@RequestHeader(value = "X-UserID", required = true) String userID,
                                         @PathVariable("vaultid") String vaultID) throws Exception {
   */
  @Test
  void testGetRoleEvents() throws Exception {
    when(controller.getRoleEvents(USER_ID_1, "vault-id-3")).thenReturn(
        Arrays.asList(AuthTestData.EVENT_INFO_1, AuthTestData.EVENT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/vault-id-3/roleEvents"),
        Arrays.asList(AuthTestData.EVENT_INFO_1, AuthTestData.EVENT_INFO_2), HttpStatus.OK,
        false);

    verify(controller).getRoleEvents(USER_ID_1, "vault-id-3");
  }

  /*
    @GetMapping("/vaults")
    public List<VaultInfo> getVaults(@RequestHeader(value = "X-UserID", required = true) String userID) {
 */
  @Test
  void testGetVaults() throws Exception {
    when(controller.getVaults(USER_ID_1)).thenReturn(
        Arrays.asList(AuthTestData.VAULT_INFO_1, AuthTestData.VAULT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults"),
        Arrays.asList(AuthTestData.VAULT_INFO_1, AuthTestData.VAULT_INFO_2), HttpStatus.OK,
        false);

    verify(controller).getVaults(USER_ID_1);
  }

  /*
    @GetMapping("/vaults/user")
    public List<VaultInfo> getVaultsForUser(@RequestParam(value = "userID", required = true)String userID) {
   */
  @Test
  void testGetVaultsForUser() throws Exception {
    when(controller.getVaultsForUser("user123")).thenReturn(
        Arrays.asList(AuthTestData.VAULT_INFO_1, AuthTestData.VAULT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/user").queryParam("userID", "user123"),
        Arrays.asList(AuthTestData.VAULT_INFO_1, AuthTestData.VAULT_INFO_2), HttpStatus.OK,
        false);

    verify(controller).getVaultsForUser("user123");
  }

  /*
    @GetMapping(value = "/vaults/deposits/search")
    public List<DepositInfo> searchAllDeposits(@RequestHeader(value = "X-UserID", required = true) String userID,
                                               @RequestParam(value = "query", required = false, defaultValue = "") String query,
                                               @RequestParam(value = "sort", required = false, defaultValue = "creationTime") String sort,
                                               @RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
   */
  @Test
  void testGetSearchAllDeposits() {
    when(controller.searchAllDeposits(USER_ID_1, "query1", "sort1", "order1")).thenReturn(
        Arrays.asList(AuthTestData.DEPOSIT_INFO_1, AuthTestData.DEPOSIT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/deposits/search")
            .queryParam("query", "query1")
            .queryParam("sort", "sort1")
            .queryParam("order", "order1"),
        Arrays.asList(AuthTestData.DEPOSIT_INFO_1, AuthTestData.DEPOSIT_INFO_2), HttpStatus.OK,
        false);

    verify(controller).searchAllDeposits(USER_ID_1, "query1", "sort1", "order1");
  }

  /*
      @GetMapping("/vaults/deposits/data/search")
    public DepositsData searchAllDepositsData(@RequestHeader(value = "X-UserID", required = true) String userID,
                                              @RequestParam(value = "query", required = false, defaultValue = "") String query,
                                              @RequestParam(value = "sort", required = false, defaultValue = "creationTime") String sort,
                                              @RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
   */
  @Test
  void testGetSearchAllDepositData() {
    when(controller.searchAllDepositsData(USER_ID_1, "query1", "sort1", "order1")).thenReturn(
        AuthTestData.DEPOSIT_DATA_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/deposits/data/search")
            .queryParam("query", "query1")
            .queryParam("sort", "sort1")
            .queryParam("order", "order1"),
        AuthTestData.DEPOSIT_DATA_1, HttpStatus.OK,
        false);

    verify(controller).searchAllDepositsData(USER_ID_1, "query1", "sort1", "order1");
  }

  /*
    @GetMapping("/pendingVaults/search")
    public VaultsData searchAllPendingVaults(@RequestHeader(value = "X-UserID", required = true) String userID,
                                      @RequestParam String query,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "order", required = false) String order,
                                      @RequestParam(value = "offset", required = false) String offset,
                                      @RequestParam(value = "confirmed", required = false) String confirmed,
                                      @RequestParam(value = "maxResult", required = false) String maxResult) {

   */
  @Test
  void testGetSearchAllPendingVaults() {
    when(controller.searchAllPendingVaults(USER_ID_1, "query1", "sort1", "order1", "offset1",
        "confirmed1", "maxResult1")).thenReturn(AuthTestData.VAULTS_DATA);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/pendingVaults/search")
            .queryParam("query", "query1")
            .queryParam("sort", "sort1")
            .queryParam("order", "order1")
            .queryParam("offset", "offset1")
            .queryParam("confirmed", "confirmed1")
            .queryParam("maxResult", "maxResult1"),
        AuthTestData.VAULTS_DATA, HttpStatus.OK,
        false);

    verify(controller).searchAllPendingVaults(USER_ID_1, "query1", "sort1", "order1", "offset1",
        "confirmed1", "maxResult1");
  }

  /*
    @GetMapping("/vaults/search")
    public VaultsData searchAllVaults(@RequestHeader(value = "X-UserID", required = true) String userID,
                                      @RequestParam String query,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "order", required = false) String order,
                                      @RequestParam(value = "offset", required = false) String offset,
                                      @RequestParam(value = "maxResult", required = false) String maxResult) {
   */
  @Test
  void testGetSearchAllVaults() {
    when(controller.searchAllVaults(USER_ID_1, "query1", "sort1", "order1", "offset1",
        "maxResult1")).thenReturn(AuthTestData.VAULTS_DATA);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/search")
            .queryParam("query", "query1")
            .queryParam("sort", "sort1")
            .queryParam("order", "order1")
            .queryParam("offset", "offset1")
            .queryParam("maxResult", "maxResult1"),
        AuthTestData.VAULTS_DATA, HttpStatus.OK,
        false);

    verify(controller).searchAllVaults(USER_ID_1, "query1", "sort1", "order1", "offset1",
        "maxResult1");
  }

  /*
      @GetMapping("/vaults/{vaultid}/checkretentionpolicy")
    public Vault checkVaultRetentionPolicy(@RequestHeader(value = "X-UserID", required = true) String userID,
                                           @PathVariable("vaultid") String vaultID) {

   */
  @Test
  void testGetCheckVaultRetentionPolicy() throws Exception {
    when(controller.checkVaultRetentionPolicy(USER_ID_1, "vault-id-1")).thenReturn(
        AuthTestData.VAULT_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/vault-id-1/checkretentionpolicy"),
        AuthTestData.VAULT_1, HttpStatus.OK,
        false);

    verify(controller).checkVaultRetentionPolicy(USER_ID_1, "vault-id-1");
  }

  /*
    @GetMapping("/vaults/{vaultid}/dataManager/{uun}")
    public DataManager getDataManager(@RequestHeader(value = "X-UserID", required = true) String userID,
                                      @PathVariable("vaultid") String vaultID,
                                      @PathVariable("uun") String uun) throws Exception {
   */
  @Test
  void testGetDataManager() throws Exception {
    when(controller.getDataManager(USER_ID_1, "vault-id-1", "uun-123")).thenReturn(
        AuthTestData.DATA_MANAGER_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/vault-id-1/dataManager/uun-123"),
        AuthTestData.DATA_MANAGER_1, HttpStatus.OK,
        false);

    verify(controller).getDataManager(USER_ID_1, "vault-id-1", "uun-123");
  }

  /*
    @GetMapping("/pendingVaults/{vaultid}")
    public VaultInfo getPendingVault(@RequestHeader(value = "X-UserID", required = true) String userID,
                              @PathVariable("vaultid") String vaultID) throws Exception {
   */
  @Test
  void testGetPendingVault() throws Exception {
    when(controller.getPendingVault(USER_ID_1, "vault-id-1")).thenReturn(AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/pendingVaults/vault-id-1"),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).getPendingVault(USER_ID_1, "vault-id-1");
  }

  /*
    @GetMapping("/pendingVaults/{vaultid}/record")
    public PendingVault getPendingVaultRecord(@RequestHeader(value = "X-UserID", required = true) String userID,
                                @PathVariable("vaultid") String vaultID) {
   */
  @Test
  void testGetPendingVaultRecord() {
    when(controller.getPendingVaultRecord(USER_ID_1, "vault-id-1")).thenReturn(
        AuthTestData.PENDING_VAULT_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/pendingVaults/vault-id-1/record"),
        AuthTestData.PENDING_VAULT_1, HttpStatus.OK,
        false);

    verify(controller).getPendingVaultRecord(USER_ID_1, "vault-id-1");
  }

  /*
    @GetMapping("/pendingVaults")
    public List<VaultInfo> getPendingVaults(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetPendingVaults() {
    when(controller.getPendingVaults(USER_ID_1)).thenReturn(
        Arrays.asList(AuthTestData.VAULT_INFO_1));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/pendingVaults"),
        Arrays.asList(AuthTestData.VAULT_INFO_1), HttpStatus.OK,
        false);

    verify(controller).getPendingVaults(USER_ID_1);
  }

  /*
    @GetMapping("/vaults/{vaultid}")
    public VaultInfo getVault(@RequestHeader(value = "X-UserID", required = true) String userID,
                              @PathVariable("vaultid") String vaultID) throws Exception {
   */
  @Test
  void testGetVault() throws Exception {
    when(controller.getVault(USER_ID_1, "vault-id-1")).thenReturn(AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/vault-id-1"),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).getVault(USER_ID_1, "vault-id-1");
  }

  /*
    @GetMapping("/vaults/{vaultid}/record")
    public Vault getVaultRecord(@RequestHeader(value = "X-UserID", required = true) String userID,
                                @PathVariable("vaultid") String vaultID) {
  */
  @Test
  void testGetVaultRecord() {
    when(controller.getVaultRecord(USER_ID_1, "vault-id-1")).thenReturn(AuthTestData.VAULT_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/vaults/vault-id-1/record"),
        AuthTestData.VAULT_1, HttpStatus.OK,
        false);

    verify(controller).getVaultRecord(USER_ID_1, "vault-id-1");
  }

  /*
    @PostMapping("/vaults/{vaultid}/addDataManager")
    public VaultInfo addDataManager(@RequestHeader(value = "X-UserID", required = true) String userID,
                                    @PathVariable("vaultid") String vaultID,
                                    @RequestBody() String unn) throws Exception {

   */
  @Test
  void testPostAddDataManager() throws Exception {
    when(controller.addDataManager(USER_ID_1, "vault-id-1", "uun-001")).thenReturn(
        AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(
        post("/vaults/vault-id-1/addDataManager").content("uun-001"),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).addDataManager(USER_ID_1, "vault-id-1", "uun-001");
  }

  /*
    @PostMapping(value = "/pendingVaults")
    public VaultInfo addPendingVault(@RequestHeader(value = "X-UserID", required = true) String userID,
                              @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                              @RequestBody CreateVault createVault) throws Exception {
   */
  @Test
  void testPostAddPendingVault() throws Exception {
    when(controller.addPendingVault(USER_ID_1, API_KEY_1, AuthTestData.CREATE_VAULT)).thenReturn(
        AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(
        post("/pendingVaults").content(mapper.writeValueAsString(AuthTestData.CREATE_VAULT))
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).addPendingVault(USER_ID_1, API_KEY_1, AuthTestData.CREATE_VAULT);
  }

  /*
    @PostMapping("/vaults")
    public VaultInfo addVault(@RequestHeader(value = "X-UserID", required = true) String userID,
                              @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                              @RequestBody CreateVault createVault) throws Exception {
   */
  @Test
  void testPostAddVault() throws Exception {
    when(controller.addVault(USER_ID_1, API_KEY_1, AuthTestData.CREATE_VAULT)).thenReturn(
        AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(
        post("/vaults").content(mapper.writeValueAsString(AuthTestData.CREATE_VAULT))
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).addVault(USER_ID_1, API_KEY_1, AuthTestData.CREATE_VAULT);
  }

  /*
    @DeleteMapping( "/vaults/{vaultid}/deleteDataManager/{dataManagerID}")
    public VaultInfo deleteDataManager(@RequestHeader(value = "X-UserID", required = true) String userID,
                                       @PathVariable("vaultid") String vaultID,
                                       @PathVariable("dataManagerID") String dataManagerID) throws Exception {
   */
  @Test
  void testDeleteDataManager() throws Exception {
    when(controller.deleteDataManager(USER_ID_1, "vault-id-1", "data-manager-id-1")).thenReturn(
        AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(
        delete("/vaults/vault-id-1/deleteDataManager/data-manager-id-1"),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).deleteDataManager(USER_ID_1, "vault-id-1", "data-manager-id-1");
  }

  /*
    @PostMapping("/vaults/{vaultId}/transfer")
    public ResponseEntity transferVault(@RequestHeader(value = "X-UserID", required = true) String userID,
                                        @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                        @PathVariable("vaultId") String vaultId,
                                        @RequestBody TransferVault transfer) {
   */
  @Test
  void testPostTransferVault() throws JsonProcessingException {
    when(controller.transferVault(USER_ID_1, API_KEY_1, "vault-id-1",
        AuthTestData.TRANSFER_VAULT_1)).thenReturn(
        ResponseEntity.ok().build());

    checkWorksWhenAuthenticatedFailsOtherwise(post("/vaults/vault-id-1/transfer")
            .content(mapper.writeValueAsString(AuthTestData.TRANSFER_VAULT_1))
            .contentType(MediaType.APPLICATION_JSON),
        null, HttpStatus.OK,
        false);

    verify(controller).transferVault(USER_ID_1, API_KEY_1, "vault-id-1",
        AuthTestData.TRANSFER_VAULT_1);
  }

  /*
    @PostMapping("/pendingVaults/update")
    public VaultInfo updatePendingVault(@RequestHeader(value = "X-UserID", required=true) String userID,
                                        @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                        @RequestBody CreateVault createVault) throws Exception {
   */
  @Test
  void testPostUpdatePendingVault() throws Exception {
    when(controller.updatePendingVault(USER_ID_1, API_KEY_1, AuthTestData.CREATE_VAULT)).thenReturn(
        AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/pendingVaults/update")
            .content(mapper.writeValueAsString(AuthTestData.CREATE_VAULT))
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).updatePendingVault(USER_ID_1, API_KEY_1, AuthTestData.CREATE_VAULT);

  }

  /*
    @PostMapping("/vaults/{vaultid}/updateVaultDescription")
    public VaultInfo updateVaultDescription(@RequestHeader(value = "X-UserID", required = true) String userID,
                                            @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                            @PathVariable("vaultid") String vaultID,
                                            @RequestBody() String description) throws Exception {
   */
  @Test
  void testPostUpdateVaultDescription() throws Exception {
    when(controller.updateVaultDescription(USER_ID_1, API_KEY_1, "vault-id-1",
        "description-1")).thenReturn(AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/vaults/vault-id-1/updateVaultDescription")
            .content("description-1"),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).updateVaultDescription(USER_ID_1, API_KEY_1, "vault-id-1", "description-1");
  }

  /*
    @PostMapping(value = "/vaults/{vaultid}/updateVaultName")
    public VaultInfo updateVaultName(@RequestHeader(value = "X-UserID", required = true) String userID,
                                     @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                     @PathVariable("vaultid") String vaultID,
                                     @RequestBody String name) throws Exception {   */
  @Test
  void testPostUpdateVaultName() throws Exception {
    when(controller.updateVaultName(USER_ID_1, API_KEY_1, "vault-id-1", "name-1")).thenReturn(
        AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/vaults/vault-id-1/updateVaultName")
            .content("name-1"),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).updateVaultName(USER_ID_1, API_KEY_1, "vault-id-1", "name-1");
  }

  /*
    @PostMapping("/vaults/{vaultid}/updatereviewdate")
    public VaultInfo updateVaultReviewDate(@RequestHeader(value = "X-UserID", required = true) String userID,
                                            @PathVariable("vaultid") String vaultID,
                                            @RequestBody String reviewDate) throws Exception {
   */
  @Test
  void testPostUpdateVaultReviewDate() throws Exception {
    when(controller.updateVaultReviewDate(USER_ID_1, "vault-id-1", "review-date-1")).thenReturn(
        AuthTestData.VAULT_INFO_1);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/vaults/vault-id-1/updatereviewdate")
            .content("review-date-1"),
        AuthTestData.VAULT_INFO_1, HttpStatus.OK,
        false);

    verify(controller).updateVaultReviewDate(USER_ID_1, "vault-id-1", "review-date-1");
  }
}
