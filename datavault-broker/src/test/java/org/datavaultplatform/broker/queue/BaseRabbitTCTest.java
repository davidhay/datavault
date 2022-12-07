package org.datavaultplatform.broker.queue;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.common.docker.DockerImage;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@Slf4j
public abstract class BaseRabbitTCTest extends BaseRabbitTest {

  @Container
  private static final RabbitMQContainer RABBIT = new RabbitMQContainer(DockerImage.RABBIT_IMAGE_NAME)
      .withExposedPorts(5672,15672).withStartupTimeout(Duration.ofSeconds(90L));

  @DynamicPropertySource
  static void setupProperties(DynamicPropertyRegistry registry) {
    log.info("RABBIT HTTP URL [ {} ]",RABBIT.getHttpUrl());
    registry.add("spring.rabbitmq.host", RABBIT::getHost);
    registry.add("spring.rabbitmq.port", RABBIT::getAmqpPort);
    registry.add("spring.rabbitmq.username", RABBIT::getAdminUsername);
    registry.add("spring.rabbitmq.password", RABBIT::getAdminPassword);
  }

}
