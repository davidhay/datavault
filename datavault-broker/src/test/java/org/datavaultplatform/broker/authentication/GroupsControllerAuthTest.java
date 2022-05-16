package org.datavaultplatform.broker.authentication;

import static org.datavaultplatform.broker.authentication.AuthTestData.GROUP_1;
import static org.datavaultplatform.broker.authentication.AuthTestData.GROUP_2;
import static org.datavaultplatform.broker.authentication.AuthTestData.VAULT_INFO_1;
import static org.datavaultplatform.broker.authentication.AuthTestData.VAULT_INFO_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.GroupsController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.common.model.Group;
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
public class GroupsControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  GroupsController controller;

  @MockBean
  Sender sender;

  @Captor
  ArgumentCaptor<Group> argGroup;

  @Captor
  ArgumentCaptor<String> argUserId;

  /*
    @PostMapping("/groups")
    public Group addGroup(@RequestHeader(value = "X-UserID", required = true) String userID,
                          @RequestBody Group group) throws Exception {
   */
  @Test
  void testPostAddGroup() throws Exception {
    when(controller.addGroup(argUserId.capture(), argGroup.capture())).thenReturn(
        AuthTestData.GROUP_1);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/groups")
            .content(mapper.writeValueAsString(AuthTestData.GROUP_1))
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.GROUP_1, HttpStatus.OK,
        false);

    verify(controller).addGroup(argUserId.getValue(), argGroup.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
    assertEquals(AuthTestData.GROUP_1.getName(), argGroup.getValue().getName());
  }

  /*
    @PutMapping("/groups/{groupid}/users/{owneruserid}")
    public @ResponseBody void addGroupOwner(@RequestHeader(value = "X-UserID", required = true) String userID,
                                            @PathVariable("groupid") String groupId,
                                            @PathVariable("owneruserid") String ownerUserId) throws Exception {
   */
  @Test
  void testPutAddGroupOwner() throws Exception {
    doNothing().when(controller).addGroupOwner(USER_ID_1, "group-id-one", "owner-user-id-xxx");

    checkWorksWhenAuthenticatedFailsOtherwise(put("/groups/group-id-one/users/owner-user-id-xxx"),
        null, HttpStatus.OK,
        false);

    verify(controller).addGroupOwner(USER_ID_1, "group-id-one", "owner-user-id-xxx");
  }

  /*
    @DeleteMapping("/groups/{groupid}")
    public boolean deleteGroup(@RequestHeader(value = "X-UserID", required = true) String userID,
                               @PathVariable("groupid") String groupID) {
   */
  @Test
  void testDeleteGroup() {
    when(controller.deleteGroup(USER_ID_1, "group-id-xxx")).thenReturn(true);

    checkWorksWhenAuthenticatedFailsOtherwise(delete("/groups/group-id-xxx"),
        true, HttpStatus.OK,
        false);

    verify(controller).deleteGroup(USER_ID_1, "group-id-xxx");
  }

  /*
    @PutMapping("/groups/{groupid}/disable")
    public @ResponseBody void disableGroup(@RequestHeader(value = "X-UserID", required = true) String userID,
                                           @PathVariable("groupid") String groupId) throws Exception {
   */
  @Test
  void testPutDisableGroup() throws Exception {
    doNothing().when(controller).disableGroup(USER_ID_1, "group-id-yyy");

    checkWorksWhenAuthenticatedFailsOtherwise(put("/groups/group-id-yyy/disable"),
        null, HttpStatus.OK,
        false);

    verify(controller).disableGroup(USER_ID_1, "group-id-yyy");
  }

  /*
    @PutMapping("/groups/{groupid}/enable")
    public @ResponseBody void enableGroup(@RequestHeader(value = "X-UserID", required = true) String userID,
                                          @PathVariable("groupid") String groupId) throws Exception { */
  @Test
  void testPutEnableGroup() throws Exception {
    doNothing().when(controller).enableGroup(USER_ID_1, "group-id-zzz");

    checkWorksWhenAuthenticatedFailsOtherwise(put("/groups/group-id-zzz/enable"),
        null, HttpStatus.OK,
        false);

    verify(controller).enableGroup(USER_ID_1, "group-id-zzz");
  }

  /*
    @GetMapping("/groups/{groupid}")
    public Group getGroup(@RequestHeader(value = "X-UserID", required = true) String userID,
                          @PathVariable("groupid") @ApiPathParam(name = "Group ID", description = "The Group ID to retrieve") String queryGroupID) {
  */
  @Test
  void testGetGroup() throws Exception {
    when(controller.getGroup(USER_ID_1, "group-id-zzz")).thenReturn(AuthTestData.GROUP_1);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/groups/group-id-zzz"),
        AuthTestData.GROUP_1,
        HttpStatus.OK, false);

    verify(controller).getGroup(USER_ID_1, "group-id-zzz");
  }

  /*
    @GetMapping("/groups/{groupid}/count")
    public int getGroupVaultCount(@RequestHeader(value = "X-UserID", required = true) String userID,
                                  @PathVariable("groupid") @ApiPathParam(name = "Group ID", description = "The Group ID to retrieve") String groupID) {   */
  @Test
  void testGetGroupVaultsCount() {
    when(controller.getGroupVaultCount(USER_ID_1, "group-id-abcde")).thenReturn(123);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/groups/group-id-abcde/count"),
        123,
        HttpStatus.OK, false);

    verify(controller).getGroupVaultCount(USER_ID_1, "group-id-abcde");
  }

  /*
    @GetMapping("/groups/{groupid}/vaults")
    public List<VaultInfo> getGroupVaults(@RequestHeader(value = "X-UserID", required = true) String userID,
                                          @PathVariable("groupid") String groupID) {
   */
  @Test
  void testGetGroupVaults() {
    when(controller.getGroupVaults(USER_ID_1, "group-id-abc")).thenReturn(
        Arrays.asList(VAULT_INFO_1, VAULT_INFO_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/groups/group-id-abc/vaults"),
        Arrays.asList(VAULT_INFO_1, VAULT_INFO_2),
        HttpStatus.OK, false);

    verify(controller).getGroupVaults(USER_ID_1, "group-id-abc");
  }

  /*
    @GetMapping("/groups")
    public List<Group> getGroups(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetGroups() {
    when(controller.getGroups(USER_ID_1)).thenReturn(Arrays.asList(GROUP_1, GROUP_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/groups"),
        Arrays.asList(GROUP_1, GROUP_2),
        HttpStatus.OK, false);

    verify(controller).getGroups(USER_ID_1);
  }

  /*
    @GetMapping("/groups/byScopedPermissions")
    public List<Group> getGroupsByScopedPermissions(@RequestHeader(value = "X-UserID", required = true) String userId) {
   */
  @Test
  void testGetGroupsByScopedPermissions() {
    when(controller.getGroupsByScopedPermissions(USER_ID_1)).thenReturn(
        Arrays.asList(GROUP_1, GROUP_2));

    checkWorksWhenAuthenticatedFailsOtherwise(get("/groups/byScopedPermissions"),
        Arrays.asList(GROUP_1, GROUP_2),
        HttpStatus.OK, false);

    verify(controller).getGroupsByScopedPermissions(USER_ID_1);
  }

  /*
    @GetMapping("/groups/count")
    public int getGroupsCount(@RequestHeader(value = "X-UserID", required = true) String userID) {
   */
  @Test
  void testGetGroupCount() {
    when(controller.getGroupsCount(USER_ID_1)).thenReturn(1234);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/groups/count"),
        1234,
        HttpStatus.OK, false);

    verify(controller).getGroupsCount(USER_ID_1);
  }


  /*
    @DeleteMapping("/groups/{groupid}/users/{owneruserid}")
    public @ResponseBody void removeGroupOwner(@RequestHeader(value = "X-UserID", required = true) String userID,
                                               @PathVariable("groupid") String groupId,
                                               @PathVariable("owneruserid") String ownerUserId) throws Exception {
   */
  @Test
  void testDeleteRemoveGroupOwner() throws Exception {
    doNothing().when(controller).removeGroupOwner(USER_ID_1, "group1", "owner1");

    checkWorksWhenAuthenticatedFailsOtherwise(delete("/groups/group1/users/owner1"),
        null,
        HttpStatus.OK, false);

    verify(controller).removeGroupOwner(USER_ID_1, "group1", "owner1");
  }

  /*
    @PostMapping("/groups/update")
    public ResponseEntity<Group> updateGroup(@RequestHeader(value = "X-UserID", required = true) String userID,
                                          @RequestBody Group group) throws Exception {
   */
  @Test
  void testPostUpdateGroup() throws Exception {
    when(controller.updateGroup(argUserId.capture(), argGroup.capture())).thenReturn(
        ResponseEntity.ok(
            GROUP_1));

    checkWorksWhenAuthenticatedFailsOtherwise(post("/groups/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(GROUP_1)),
        GROUP_1,
        HttpStatus.OK, false);

    verify(controller).updateGroup(argUserId.getValue(), argGroup.getValue());
    assertEquals(USER_ID_1, argUserId.getValue());
    assertEquals(GROUP_1.getID(), argGroup.getValue().getID());
  }
}