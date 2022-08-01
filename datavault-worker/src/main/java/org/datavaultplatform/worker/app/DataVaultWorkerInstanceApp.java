package org.datavaultplatform.worker.app;

import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.worker.config.ActuatorConfig;
import org.datavaultplatform.worker.config.EventSenderConfig;
import org.datavaultplatform.worker.config.PropertiesConfig;
import org.datavaultplatform.worker.config.QueueConfig;
import org.datavaultplatform.worker.config.RabbitConfig;
import org.datavaultplatform.worker.config.ReceiverConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Import({
    PropertiesConfig.class,
    ActuatorConfig.class,
    QueueConfig.class,
    EventSenderConfig.class,
    ReceiverConfig.class,
    RabbitConfig.class,
})
@Slf4j
public class DataVaultWorkerInstanceApp {

  @Value("${spring.application.name}")
  String applicationName;

  public static void main(String[] args) {

    //setup properties BEFORE spring starts
    System.setProperty("datavault-home", System.getenv("DATAVAULT_HOME"));

    SpringApplication.run(DataVaultWorkerInstanceApp.class, args);
  }

  @EventListener
  void onEvent(ApplicationStartingEvent event) {
    log.info("Worker [{}] starting", applicationName);
  }

  @EventListener
  void onEvent(ApplicationReadyEvent event) {
    log.info("Worker [{}] ready", applicationName);
  }

}