package org.datavaultplatform.webapp.config.database;

import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.webapp.authentication.AuthenticationSuccess;
import org.datavaultplatform.webapp.authentication.database.DatabaseAuthenticationProvider;
import org.datavaultplatform.webapp.config.HttpSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class DatabaseWebSecurityConfig {

  @Autowired
  SessionRegistry sessionRegistry;

  @Autowired
  AuthenticationSuccess authenticationSuccess;

  @Autowired
  DatabaseAuthenticationProvider authenticationProvider;

  @Bean
  @Order(2)
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    HttpSecurityUtils.formLogin(http, authenticationSuccess);

    HttpSecurityUtils.authorizeRequests(http);

    HttpSecurityUtils.sessionManagement(http, sessionRegistry);

    http.authenticationProvider(authenticationProvider);

    return http.build();
  }
}