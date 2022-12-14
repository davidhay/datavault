package org.datavaultplatform.webapp.app.authentication.shib;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import org.datavaultplatform.webapp.authentication.shib.ShibAuthenticationListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class MockitoTest {

  @Captor
  ArgumentCaptor<AuthenticationSuccessEvent> argAuthSuccessEvent;

  @Mock
  ShibAuthenticationListener mAuthListener;


  @Mock
  Authentication mAuth;

  @Test
  void testNoInvocation() {
    AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(mAuth);
    Mockito.verify(mAuthListener, times(0)).onApplicationEvent(any());
  }
  @Test
  void testOneInvocation() {
    doNothing().when(mAuthListener).onApplicationEvent(argAuthSuccessEvent.capture());

    AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(mAuth);

    mAuthListener.onApplicationEvent(event);
    //Mockito.verify(mAuthListener, times(0)).onApplicationEvent(any());
    Mockito.verify(mAuthListener).onApplicationEvent(argAuthSuccessEvent.getValue());
  }
}
