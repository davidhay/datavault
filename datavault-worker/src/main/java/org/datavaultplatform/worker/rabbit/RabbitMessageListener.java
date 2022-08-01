package org.datavaultplatform.worker.rabbit;

import com.rabbitmq.client.Channel;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.worker.config.QueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.util.Assert;

@Slf4j
public class RabbitMessageListener {

  private final ShutdownHandler shutdownHandler;
  private final MessageProcessor processor;

  private final RabbitListenerUtils listeners;

  public RabbitMessageListener(
      MessageProcessor processor,
      ShutdownHandler shutdownHandler,
      RabbitListenerUtils listeners) {
    this.processor = processor;
    this.shutdownHandler = shutdownHandler;
    this.listeners = listeners;
  }

  @SneakyThrows
  @RabbitListener(queues = QueueConfig.WORKER_QUEUE_NAME, exclusive = true)
  void onMessage(Message message, Channel channel,
      @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

    Assert.notNull(message, () -> "The message cannot be null");
    Assert.notNull(channel, () -> "The channel cannot be null");
    Assert.notNull(deliveryTag, () -> "The deliveryTag cannot be null");

    long deliveryTag2 = message.getMessageProperties().getDeliveryTag();
    Assert.isTrue(deliveryTag == deliveryTag2, () -> "problems with tags");

    MessageInfo info = getMessageInfo(message);
    log.info("XXX MESSAGE[{}]delTag[{}]", info, deliveryTag);

    if (info.isShutdown()) {
      channel.basicAck(deliveryTag, false);

      listeners.stopAll();

      shutdownHandler.handleShutdown(info);

    } else {

      boolean requeue = processor.processMessage(info);
      if (requeue) {
        log.info("YYY NACK/REQUEUE [{}]", info);
        channel.basicNack(deliveryTag, false, true);
      } else {
        log.info("YYY ACK [{}]", info);
        channel.basicAck(deliveryTag, false);
      }
    }
  }

  private MessageInfo getMessageInfo(Message message) {
    String messageBody = getMessageBody(message);
    String id = message.getMessageProperties().getMessageId();
    boolean isRedeliver = message.getMessageProperties().isRedelivered();
    MessageInfo info = MessageInfo.builder()
        .id(id)
        .value(messageBody)
        .isRedeliver(isRedeliver)
        .build();
    return info;
  }


  private String getMessageBody(Message message) {
    if (message == null || message.getBody() == null) {
      return "";
    } else {
      return new String(message.getBody(), StandardCharsets.UTF_8);
    }
  }


}
