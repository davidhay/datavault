package org.datavaultplatform.worker.config;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class QueueConfig {

  public final static String WORKER_QUEUE_NAME = "${queue.name}";
  public final static String BROKER_QUEUE_NAME = "${queue.events}";

  @Value(QueueConfig.WORKER_QUEUE_NAME)
  String workerQueueName;

  @Value(QueueConfig.BROKER_QUEUE_NAME)
  String brokerQueueName;


  @Bean
  @ConditionalOnProperty(value="worker.define.queue.worker", havingValue = "true",  matchIfMissing = false)
  public Queue workerQueue() {
    Map<String, Object> args = new HashMap<>();
    args.put("x-max-priority", 2);
    return new Queue(workerQueueName, true, false, false, args);
  }

  @Bean
  @ConditionalOnProperty(value="worker.define.queue.broker", havingValue = "true",  matchIfMissing = false)
  public Queue brokerQueue() {
    return new Queue(brokerQueueName, true, false, false);
  }
}
