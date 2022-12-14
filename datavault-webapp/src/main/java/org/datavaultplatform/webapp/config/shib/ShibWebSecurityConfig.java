package org.datavaultplatform.webapp.config.shib;

import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.webapp.authentication.shib.ShibAuthenticationProvider;
import org.datavaultplatform.webapp.config.HttpSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ShibWebSecurityConfig {

  @Autowired
  SessionRegistry sessionRegistry;

  @Autowired
  ShibAuthenticationProvider shibAuthenticationProvider;

  @Autowired
  Http403ForbiddenEntryPoint http403EntryPoint;

  @Autowired
  ShibFilterConfigurer shibFilterConfigurer;

  /*
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // no form login for 'shib'

    HttpSecurityUtils.authorizeRequests(http);

    HttpSecurityUtils.sessionManagement(http, sessionRegistry);

    // 'shib' specific config
    http.addFilterAt(shibFilter(), AbstractPreAuthenticatedProcessingFilter.class);

    http.exceptionHandling(ex -> ex.authenticationEntryPoint(http403EntryPoint));
  }
   */

  @Bean
  @Order(2)
  public SecurityFilterChain filterChain(HttpSecurity http, ShibAuthenticationProvider shibAuthenticationProvider) throws Exception {

    // no form login for 'shib'
    http.authenticationProvider(shibAuthenticationProvider);

    HttpSecurityUtils.authorizeRequests(http);

    HttpSecurityUtils.sessionManagement(http, sessionRegistry);

    // 'shib' specific config
    shibFilterConfigurer.configure(http);

    http.exceptionHandling(ex -> ex.authenticationEntryPoint(http403EntryPoint));
    return http.build();
  }

  /*
 <bean id="shibFilter" class="org.datavaultplatform.webapp.authentication.ShibAuthenticationFilter">
    <property name="principalRequestHeader" value="${shibboleth.principal}"/>
    <property name="exceptionIfHeaderMissing" value="true"/>
    <property name="authenticationManager" ref="authenticationManager" />
    <property name="authenticationDetailsSource" ref="shibWebAuthenticationDetailsSource" />
 </bean>
 */


}