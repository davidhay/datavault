package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vavr.API;
import java.util.Arrays;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.config.MockServicesConfig;
import org.datavaultplatform.broker.controllers.RolesAndPermissionsController;
import org.datavaultplatform.broker.controllers.VaultsController;
import org.datavaultplatform.broker.queue.Sender;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.common.model.PermissionModel;
import org.datavaultplatform.common.model.RoleAssignment;
import org.datavaultplatform.common.model.RoleModel;
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
public class RolesAndPermissonsControllerAuthTest extends BaseControllerAuthTest {

  @MockBean
  Sender sender;

  @MockBean
  RolesAndPermissionsController controller;

  /*
    @PostMapping("/role")
    public RoleModel createRole(@RequestBody RoleModel role) {   */
  @Test
  void testPostCreateRole() throws JsonProcessingException {
    when(controller.createRole(AuthTestData.ROLE_MODEL)).thenReturn(AuthTestData.ROLE_MODEL);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/permissions/role")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AuthTestData.ROLE_MODEL)),
        AuthTestData.ROLE_MODEL, HttpStatus.OK,
        false);

    verify(controller).createRole(AuthTestData.ROLE_MODEL);
  }
  /*
    @PostMapping("/roleAssignment")
    public RoleAssignment createRoleAssignment(@RequestHeader(value = "X-UserID", required = true) String userID,
                                               @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                               @RequestBody RoleAssignment roleAssignment) throws Exception {
   */
  @Test
  void testPostCreateRoleAssignment() throws Exception {
    when(controller.createRoleAssignment(USER_ID_1, API_KEY_1, AuthTestData.ROLE_ASSIGNMENT)).thenReturn(AuthTestData.ROLE_ASSIGNMENT);

    checkWorksWhenAuthenticatedFailsOtherwise(post("/permissions/roleAssignment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AuthTestData.ROLE_ASSIGNMENT)),
        AuthTestData.ROLE_ASSIGNMENT, HttpStatus.OK,
        false);

    verify(controller).createRoleAssignment(USER_ID_1, API_KEY_1, AuthTestData.ROLE_ASSIGNMENT);
  }
  /*
    @DeleteMapping("/role/{roleId}")
    public ResponseEntity deleteRole(@PathVariable("roleId") Long roleId) {
   */
  @Test
  void testDeleteRole(){
    when(controller.deleteRole(1234L)).thenReturn(ResponseEntity.ok().build());

    checkWorksWhenAuthenticatedFailsOtherwise(delete("/permissions/role/1234"),
        null, HttpStatus.OK,
        false);
    verify(controller).deleteRole(1234L);
  }
  /*
    @DeleteMapping("/roleAssignment/{roleAssignmentId}")
    public ResponseEntity deleteRoleAssignment(
            @RequestHeader(value = "X-UserID", required = true) String userID,
            @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
            @PathVariable("roleAssignmentId") Long roleAssignmentId)
            throws Exception {
   */
  @Test
  void testDeleteRoleAssignment() throws Exception {
    when(controller.deleteRoleAssignment(USER_ID_1, API_KEY_1, 1234L)).thenReturn(ResponseEntity.ok().build());

    checkWorksWhenAuthenticatedFailsOtherwise(delete("/permissions/roleAssignment/1234"),
        null, HttpStatus.OK,
        false);

    verify(controller).deleteRoleAssignment(USER_ID_1, API_KEY_1, 1234L);
  }

  /*
    @GetMapping("/roles/school")
    public RoleModel[] getAllSchoolRoles() {
   */
  @Test
  void testGetAllSchoolRoles() {
    when(controller.getAllSchoolRoles()).thenReturn(new RoleModel[]{AuthTestData.ROLE_MODEL});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/roles/school"),
        new RoleModel[]{ AuthTestData.ROLE_MODEL}, HttpStatus.OK,
        false);

    verify(controller).getAllSchoolRoles();
  }

  /*
    @GetMapping("/roles/vault")
    public RoleModel[] getAllVaultRoles() {
   */
  @Test
  void testGetAllVaultRoles(){
    when(controller.getAllVaultRoles()).thenReturn(new RoleModel[]{AuthTestData.ROLE_MODEL});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/roles/vault"),
        new RoleModel[]{ AuthTestData.ROLE_MODEL}, HttpStatus.OK,
        false);

    verify(controller).getAllVaultRoles();

  }

  /*
    public RoleModel[] getEditableRoles() {
   */
  @Test
  void testGetEditableRoles() {
    when(controller.getEditableRoles()).thenReturn(new RoleModel[]{AuthTestData.ROLE_MODEL});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/roles"),
        new RoleModel[]{ AuthTestData.ROLE_MODEL}, HttpStatus.OK,
        false);

    verify(controller).getEditableRoles();
  }
  /*
    @GetMapping("/role/isAdmin")
    public RoleModel getIsAdmin() {
   */
  @Test
  void testGetIsAdmin(){
    when(controller.getIsAdmin()).thenReturn(AuthTestData.ROLE_MODEL);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/role/isAdmin"),
        AuthTestData.ROLE_MODEL, HttpStatus.OK,
        false);

    verify(controller).getIsAdmin();
  }
  /*
    @GetMapping("/role/{roleId}")
    public RoleModel getRole(@PathVariable("roleId") Long id) {
   */
  @Test
  void testGetRole(){
    when(controller.getRole(1234L)).thenReturn(AuthTestData.ROLE_MODEL);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/role/1234"),
        AuthTestData.ROLE_MODEL, HttpStatus.OK,
        false);

    verify(controller).getRole(1234L);
  }
  /*
    @GetMapping("/roleAssignment/{assignmentId}")
    public RoleAssignment getRoleAssignment(@PathVariable("assignmentId") Long assignmentId) {
   */
  @Test
  void testGetRoleAssignment(){
    when(controller.getRoleAssignment(1234L)).thenReturn(AuthTestData.ROLE_ASSIGNMENT);

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/roleAssignment/1234"),
        AuthTestData.ROLE_ASSIGNMENT, HttpStatus.OK,
        false);

    verify(controller).getRoleAssignment(1234L);
  }

  /*
    @GetMapping("/roleAssignments/role/{roleId}")
    public RoleAssignment[] getRoleAssignmentsForRole(
            @PathVariable("roleId") Long roleId) {
   */
  @Test
  void testGetRoleAssignmentsForRole(){
    when(controller.getRoleAssignmentsForRole(1234L)).thenReturn(new RoleAssignment[]{AuthTestData.ROLE_ASSIGNMENT});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/roleAssignments/role/1234"),
        new RoleAssignment[]{AuthTestData.ROLE_ASSIGNMENT}, HttpStatus.OK,
        false);

    verify(controller).getRoleAssignmentsForRole(1234L);
  }

  /*
    @GetMapping("/roleAssignments/school/{schoolId}")
    public RoleAssignment[] getRoleAssignmentsForSchool(
            @PathVariable("schoolId") String schoolId) {
   */
  @Test
  void testGetRoleAssignmentForSchool(){
    when(controller.getRoleAssignmentsForSchool("school-id-1")).thenReturn(new RoleAssignment[]{AuthTestData.ROLE_ASSIGNMENT});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/roleAssignments/school/school-id-1"),
        new RoleAssignment[]{AuthTestData.ROLE_ASSIGNMENT}, HttpStatus.OK,
        false);

    verify(controller).getRoleAssignmentsForSchool("school-id-1");
  }
  /*
    @GetMapping("/roleAssignments/user/{userId}")
    public RoleAssignment[] getRoleAssignmentsForUser(
      @PathVariable("userId") String userId) {
   */
  @Test
  void testGetRoleAssignmentForUser(){
    when(controller.getRoleAssignmentsForUser("user-id-1")).thenReturn(new RoleAssignment[]{AuthTestData.ROLE_ASSIGNMENT});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/roleAssignments/user/user-id-1"),
        new RoleAssignment[]{AuthTestData.ROLE_ASSIGNMENT}, HttpStatus.OK,
        false);

    verify(controller).getRoleAssignmentsForUser("user-id-1");
  }
  /*
    @GetMapping("/roleAssignments/vault/{vaultId}")
    public RoleAssignment[] getRoleAssignmentsForVault(
   */
  @Test
  void testGetRoleAssignmentForVault(){
    when(controller.getRoleAssignmentsForVault("vault-id-1")).thenReturn(new RoleAssignment[]{AuthTestData.ROLE_ASSIGNMENT});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/roleAssignments/vault/vault-id-1"),
        new RoleAssignment[]{AuthTestData.ROLE_ASSIGNMENT}, HttpStatus.OK,
        false);

    verify(controller).getRoleAssignmentsForVault("vault-id-1");
  }
  /*
    @GetMapping("/school")
    public PermissionModel[] getSchoolPermissions() {
   */
  @Test
  void testGetSchoolPermissions(){
    when(controller.getSchoolPermissions()).thenReturn(new PermissionModel[]{AuthTestData.PERMISSION_MODEL});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/school"),
        new PermissionModel[]{AuthTestData.PERMISSION_MODEL}, HttpStatus.OK,
        false);

    verify(controller).getSchoolPermissions();
  }

  /*
    @GetMapping("/vault")
    public PermissionModel[] getVaultPermissions() {
   */
  @Test
  void testGetVaultPermissions(){
    when(controller.getVaultPermissions()).thenReturn(new PermissionModel[]{AuthTestData.PERMISSION_MODEL});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/vault"),
        new PermissionModel[]{AuthTestData.PERMISSION_MODEL}, HttpStatus.OK,
        false);

    verify(controller).getVaultPermissions();
  }

  /*
    @GetMapping("/roles/readOnly")
    public RoleModel[] getViewableRoles() {
   */
  @Test
  void testGetViewableRoles(){
    when(controller.getViewableRoles()).thenReturn(new RoleModel[]{AuthTestData.ROLE_MODEL});

    checkWorksWhenAuthenticatedFailsOtherwise(get("/permissions/roles/readOnly"),
        new RoleModel[]{AuthTestData.ROLE_MODEL}, HttpStatus.OK,
        false);

    verify(controller).getViewableRoles();
  }

  /*
    @PutMapping("/role")
    public RoleModel updateRole(@RequestBody RoleModel role) {
   */
  @Test
  void testPutUpdateRole() throws JsonProcessingException {
    when(controller.updateRole(AuthTestData.ROLE_MODEL)).thenReturn(AuthTestData.ROLE_MODEL);

    checkWorksWhenAuthenticatedFailsOtherwise(put("/permissions/role")
            .content(mapper.writeValueAsString(AuthTestData.ROLE_MODEL))
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.ROLE_MODEL, HttpStatus.OK,
        false);

    verify(controller).updateRole(AuthTestData.ROLE_MODEL);
  }
  /*
    @PutMapping("/roleAssignment")
    public RoleAssignment updateRoleAssignment(@RequestHeader(value = "X-UserID", required = true) String userID,
                                               @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                                     @RequestBody RoleAssignment roleAssignment) throws Exception {
   */
  @Test
  void testPutUpdateRoleAssignment() throws Exception {
    when(controller.updateRoleAssignment(USER_ID_1,API_KEY_1, AuthTestData.ROLE_ASSIGNMENT)).thenReturn(AuthTestData.ROLE_ASSIGNMENT);

    checkWorksWhenAuthenticatedFailsOtherwise(put("/permissions/roleAssignment")
            .content(mapper.writeValueAsString(AuthTestData.ROLE_ASSIGNMENT))
            .contentType(MediaType.APPLICATION_JSON),
        AuthTestData.ROLE_ASSIGNMENT, HttpStatus.OK,
        false);

    verify(controller).updateRoleAssignment(USER_ID_1,API_KEY_1, AuthTestData.ROLE_ASSIGNMENT);
  }

  /*

   */
}
