package org.datavaultplatform.worker.rabbit;

import org.datavaultplatform.worker.rabbit.MessageInfo;

public interface MessageProcessor {

  /**
   * Consume a message from RabbitMQ
   * @param messageInfo - the message to process
   * @return true if the message should be redelivered
   */
  boolean processMessage(MessageInfo messageInfo);

}
