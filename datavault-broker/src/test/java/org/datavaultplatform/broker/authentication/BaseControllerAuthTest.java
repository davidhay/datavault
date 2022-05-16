package org.datavaultplatform.broker.authentication;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.services.AdminService;
import org.datavaultplatform.broker.services.ClientsService;
import org.datavaultplatform.broker.services.RolesAndPermissionsService;
import org.datavaultplatform.broker.services.UsersService;
import org.datavaultplatform.common.model.Client;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.User;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@Slf4j
@AutoConfigureMockMvc
@TestPropertySource(properties = "logging.level.org.springframework.security=DEBUG")
public abstract class BaseControllerAuthTest {

  public static final String USER_ID_1 = "test-user-01";
  public static final String API_KEY_1 = "api-key-01";
  public static final String IP_ADDRESS = "1.2.3.4";
  @Autowired
  protected MockMvc mvc;
  @Autowired
  protected UsersService mUserService;

  @Autowired
  protected AdminService mAdminService;

  @Autowired
  protected RolesAndPermissionsService mRolesAndPermissionService;
  @Autowired
  protected ClientsService mClientService;
  @Mock
  User mLoginUser;
  @Autowired
  ObjectMapper mapper;

  private Client clientForIp(String ipAddress) {
    Client result = new Client();
    result.setIpAddress(ipAddress);
    return result;
  }

  private MockHttpServletRequestBuilder setupAuthentication(MockHttpServletRequestBuilder builder) {
    return builder.with(req -> {
          req.setRemoteAddr(IP_ADDRESS);
          return req;
        })
        .header("X-Client-Key", API_KEY_1)
        .header("X-UserID", USER_ID_1);
  }

  /*
    invokes the endpoint described by requestBuilder 2 times - with authentication and without.
    Without authentication should fail, with authentication should succeed with expectedResponse as json
    NOTE : you still have to mock the controller method associated with endpoint to return expectedResponse
   */


  protected final void checkWorksWhenAuthenticatedFailsOtherwise(
      MockHttpServletRequestBuilder requestBuilder, Object expectedResponse,
      HttpStatus expectedSuccessStatus, boolean isAdminUser, Permission... permissions) {

    //CHECK UNAUTHENTICATED
    checkUnauthorizedWhenNotAuthenticated(requestBuilder);

    when(mLoginUser.getID()).thenReturn(USER_ID_1);

    when(mRolesAndPermissionService.getUserPermissions(USER_ID_1)).thenReturn(
        getPermissions(permissions));
    when(mUserService.getUser(USER_ID_1)).thenReturn(mLoginUser);
    when(mAdminService.isAdminUser(mLoginUser)).thenReturn(isAdminUser);
    when(mClientService.getClientByApiKey(API_KEY_1)).thenReturn(clientForIp(IP_ADDRESS));

    //CHECK AUTHENTICATED
    checkSuccessWhenAuthenticated(requestBuilder, expectedResponse, expectedSuccessStatus);

  }

  private Set<Permission> getPermissions(Permission[] permissions) {
    return new HashSet(Arrays.asList(permissions));
  }

  @SneakyThrows
  private void checkSuccessWhenAuthenticated(MockHttpServletRequestBuilder builder,
      Object expectedSuccessResponse, HttpStatus expectedSuccessStatus) {

    ResultActions resultActions = mvc.perform(
            setupAuthentication(builder))
        .andDo(print())
        .andExpect(status().is(expectedSuccessStatus.value()));

    if (expectedSuccessResponse == null) {
      resultActions
          .andExpect(content().string(""));
    } else {
      //an expected response of type String means we should have 'text/plain' response
      if (expectedSuccessResponse instanceof String) {
        resultActions
            .andExpect(content().string(expectedSuccessResponse.toString()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
      } else {
        resultActions
            .andExpect(content().string(mapper.writeValueAsString(expectedSuccessResponse)))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
      }
    }

    verify(mClientService).getClientByApiKey(API_KEY_1);
    verify(mUserService).getUser(USER_ID_1);
    verify(mAdminService).isAdminUser(mLoginUser);
    verify(mLoginUser, atLeast(1)).getID();
    verify(mRolesAndPermissionService, atLeast(1)).getUserPermissions(USER_ID_1);
    verifyNoMoreInteractions(mClientService, mUserService, mAdminService, mLoginUser);
  }

  @SneakyThrows
  private void checkUnauthorizedWhenNotAuthenticated(MockHttpServletRequestBuilder builder) {
    mvc.perform(builder)
        .andDo(print())
        .andExpect(status().isUnauthorized());
    verify(mClientService).getClientByApiKey(null);
    verifyNoMoreInteractions(mClientService, mUserService);
  }

}
