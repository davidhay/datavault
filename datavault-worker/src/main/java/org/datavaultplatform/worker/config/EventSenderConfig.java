package org.datavaultplatform.worker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.datavaultplatform.worker.queue.EventSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventSenderConfig {

  @Autowired
  RabbitTemplate template;

  @Value(QueueConfig.BROKER_QUEUE_NAME)
  String brokerQueueName;

  @Value("${sender.sequence.start:0}")
  int sequenceStart;

  @Value("${spring.application.name:worker-instance}")
  String workerName;

  @Bean
  public ObjectMapper mapper() {
    return new ObjectMapper();
  }

  @Bean
  public EventSender eventSender() {
    return new EventSender(template, brokerQueueName, workerName, sequenceStart, mapper());
  }
}
