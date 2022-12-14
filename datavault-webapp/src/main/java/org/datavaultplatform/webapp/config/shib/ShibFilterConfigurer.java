package org.datavaultplatform.webapp.config.shib;

import lombok.SneakyThrows;
import org.datavaultplatform.webapp.authentication.shib.ShibAuthenticationFilter;
import org.datavaultplatform.webapp.authentication.shib.ShibWebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

@Component
public class ShibFilterConfigurer extends AbstractHttpConfigurer<ShibFilterConfigurer, HttpSecurity> {

  @Value("${shibboleth.principal}")
  String principalRequestHeader;

  @Autowired
  ShibWebAuthenticationDetailsSource authDetailsSource;

  private ShibAuthenticationFilter shibAuthFilter(AuthenticationManager authenticationManager) {
    ShibAuthenticationFilter filter = new ShibAuthenticationFilter();
    filter.setPrincipalRequestHeader(principalRequestHeader);
    filter.setExceptionIfHeaderMissing(true);
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationDetailsSource(authDetailsSource);
    return filter;
  }

  @Override
  @SneakyThrows
  public void configure(HttpSecurity http) {
    final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
    ShibAuthenticationFilter shibFilter = shibAuthFilter(authenticationManager);
    http.addFilterAt(shibFilter, AbstractPreAuthenticatedProcessingFilter.class);
  }

}
