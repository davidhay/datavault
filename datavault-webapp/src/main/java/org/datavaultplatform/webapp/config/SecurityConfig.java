package org.datavaultplatform.webapp.config;

import org.datavaultplatform.webapp.authentication.AuthenticationSuccess;
import org.datavaultplatform.webapp.services.EvaluatorService;
import org.datavaultplatform.webapp.security.ScopedPermissionEvaluator;
import org.datavaultplatform.webapp.services.NotifyLoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

  @Value("${spring.security.debug:false}")
  boolean securityDebug;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.debug(securityDebug);
  }

  @Bean
  public AuthenticationSuccess authenticationSuccess(NotifyLoginService service){
    return new AuthenticationSuccess(service);
  }

  @Bean
  public PermissionEvaluator permissionEvaluator(EvaluatorService evaluatorService) {
    return new ScopedPermissionEvaluator(evaluatorService);
  }

}
