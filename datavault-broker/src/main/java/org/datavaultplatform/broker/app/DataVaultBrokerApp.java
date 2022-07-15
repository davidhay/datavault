package org.datavaultplatform.broker.app;

import static org.datavaultplatform.broker.scheduled.ScheduledUtils.*;

import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.config.ControllerConfig;
import org.datavaultplatform.broker.config.DatabaseConfig;
import org.datavaultplatform.broker.config.EmailConfig;
import org.datavaultplatform.broker.config.EmailLocalConfig;
import org.datavaultplatform.broker.config.InitialiseConfig;
import org.datavaultplatform.broker.config.LdapConfig;
import org.datavaultplatform.broker.config.PropertiesConfig;
import org.datavaultplatform.broker.config.RabbitConfig;
import org.datavaultplatform.broker.config.ScheduleConfig;
import org.datavaultplatform.broker.config.SecurityActuatorConfig;
import org.datavaultplatform.broker.config.SecurityConfig;
import org.datavaultplatform.broker.config.ServiceConfig;
import org.datavaultplatform.broker.scheduled.ScheduledUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Import({
    PropertiesConfig.class,
    ScheduleConfig.class, InitialiseConfig.class,
    SecurityActuatorConfig.class, SecurityConfig.class, ControllerConfig.class,
    ServiceConfig.class,  DatabaseConfig.class,
    LdapConfig.class, EmailConfig.class, EmailLocalConfig.class, RabbitConfig.class
})
@Slf4j
//@EnableJSONDoc
public class DataVaultBrokerApp implements CommandLineRunner {
  @Autowired
  Environment env;

  public static void main(String[] args) {
    SpringApplication.run(DataVaultBrokerApp.class, args);
  }

  @Override
  public void run(String... args) {
    log.info("java.version [{}]",env.getProperty("java.version"));
    log.info("java.vendor [{}]",env.getProperty("java.vendor"));
    log.info("os.arch [{}]",env.getProperty("os.arch"));
    log.info("os.name [{}]",env.getProperty("os.name"));
    log.info("spring.security.debug [{}]",env.getProperty("spring.security.debug"));
    log.info("spring-boot.version [{}]", SpringBootVersion.getVersion());
    log.info("active.profiles {}", (Object) env.getActiveProfiles());
    log.info("git.commit.id.abbrev [{}]", env.getProperty("git.commit.id.abbrev","-1"));

    Stream.of(
        SCHEDULE_1_AUDIT_DEPOSIT_NAME,
        SCHEDULE_2_ENCRYPTION_CHECK_NAME,
        SCHEDULE_3_DELETE_NAME,
        SCHEDULE_4_REVIEW_NAME,
        SCHEDULE_5_RETENTION_CHECK_NAME).forEach(schedule -> log.info("Schedule[{}]value[{}]",schedule, env.getProperty(schedule)));
  }


}
