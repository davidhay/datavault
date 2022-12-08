package org.datavaultplatform.broker.config;

import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

@Component
public class RestFilterConfigurer extends AbstractHttpConfigurer<RestFilterConfigurer, HttpSecurity> {


  @Override
  @SneakyThrows
  public void configure(HttpSecurity http) {
    final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
    http.addFilterAt(SecurityConfig.restFilter(authenticationManager), AbstractPreAuthenticatedProcessingFilter.class);
  }

}
