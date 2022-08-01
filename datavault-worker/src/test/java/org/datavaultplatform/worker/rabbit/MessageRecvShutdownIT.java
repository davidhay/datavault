package org.datavaultplatform.worker.rabbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.worker.app.DataVaultWorkerInstanceApp;
import org.datavaultplatform.worker.test.AddTestProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DataVaultWorkerInstanceApp.class)
@Slf4j
@AddTestProperties
class MessageRecvShutdownIT extends BaseRabbitTCTest {

  private static final int MESSAGES_TO_SEND = 5;
  private static final int MESSAGES_TO_PROCESS = 2;

  @Autowired
  AmqpAdmin admin;

  @Autowired
  RabbitTemplate template;

  @Autowired
  @Qualifier("workerQueue")
  Queue workerQueue;

  @Autowired
  @Qualifier("brokerQueue")
  Queue brokerQueue;

  @MockBean
  MessageProcessor mProcessor;

  @MockBean
  ShutdownHandler mShutdownHandler;

  List<MessageInfo> messageInfos;

  private String shutdownMessageId = null;

  @Captor
  ArgumentCaptor<MessageInfo> argMessageInfo;

  private CountDownLatch shutdownLatch;

  @Autowired
  RabbitListenerEndpointRegistry registry;

  @Test
  @SneakyThrows
  void testSendAndRecvMessages() {
    this.shutdownLatch = new CountDownLatch(1);

    for (long i = 0; i < MESSAGES_TO_SEND; i++) {
      sendTestMessage(i);
    }
    //okay is true only after we've recvd 1 shutdown message

    boolean okay = shutdownLatch.await(300, TimeUnit.SECONDS);
    Assertions.assertEquals(MESSAGES_TO_PROCESS, messageInfos.size());
    if (!okay) {
      Assertions.fail("problem waiting for messageInfos");
    }

    // check that the shutdown message id is the one we expected
    Assertions.assertEquals(this.shutdownMessageId, argMessageInfo.getValue().getId());

    verify(mProcessor, times(MESSAGES_TO_PROCESS)).processMessage(any(MessageInfo.class));
    verify(mShutdownHandler, times(1)).handleShutdown(any(MessageInfo.class));
    verifyNoMoreInteractions(mProcessor, mShutdownHandler);

    // check that there are still 2 unprocessed messages
    Assertions.assertEquals(2,
        admin.getQueueInfo(this.workerQueue.getActualName()).getMessageCount());

  }

  String sendTestMessage(long value) {
    MessageProperties props = new MessageProperties();
    props.setMessageId(UUID.randomUUID().toString());
    String msgBody = String.valueOf(value);
    if (value == 2) {
      this.shutdownMessageId = props.getMessageId();
      msgBody = MessageInfo.SHUTDOWN;
    }
    Message msg = new Message(msgBody.getBytes(StandardCharsets.UTF_8), props);
    template.send(workerQueue.getActualName(), msg);
    return props.getMessageId();
  }

  @BeforeEach
  void setup() {
    Assertions.assertEquals("datavault", this.workerQueue.getActualName());
    Assertions.assertEquals("datavault-event", this.brokerQueue.getActualName());
    this.messageInfos = new ArrayList<>();
    admin.purgeQueue(workerQueue.getActualName());
    log.info("q[{}]purged prior to test", workerQueue.getActualName());

    //when we process a message, we record it
    doAnswer(invocation -> {
      Assertions.assertEquals(1, invocation.getArguments().length);
      MessageInfo info = invocation.getArgument(0);
      messageInfos.add(info);
      return false;
    }).when(mProcessor).processMessage(any(MessageInfo.class));

    //when we get a shutdown message, we countDown the latch
    doAnswer(invocation -> {
      Assertions.assertEquals(1, invocation.getArguments().length);
      this.shutdownLatch.countDown();
      return null;
    }).when(mShutdownHandler).handleShutdown(argMessageInfo.capture());
  }

}
