package org.datavaultplatform.broker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@ConditionalOnExpression("${broker.security.enabled:true}")
@Configuration
@Slf4j
public class SecurityActuatorConfig {

  @Value("${broker.actuator.username:bactor}")
  private String username;

  @Value("${broker.actuator.password:bactorpass}")
  private String password;

  @Bean
  public UserDetailsService actuatorUsers() {
    UserDetails user = User.builder()
        .username(username).password("{noop}" + password).roles("ACTUATOR").build();

    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public DaoAuthenticationProvider actuatorAuthenticationProvider(@Qualifier("actuatorUsers") UserDetailsService uds) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(uds);
    return provider;
  }

  @Bean
  @Order(1)
  public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http,
      @Qualifier("actuatorAuthenticationProvider") AuthenticationProvider authenticationProvider) throws Exception {

    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.httpBasic();

    http.authenticationProvider(authenticationProvider);

    http.securityMatcher("/actuator/**")
        .authorizeHttpRequests()
        .requestMatchers("/actuator/info", "/actuator/health").permitAll()
        .anyRequest().fullyAuthenticated();

    return http.build();
  }
}
