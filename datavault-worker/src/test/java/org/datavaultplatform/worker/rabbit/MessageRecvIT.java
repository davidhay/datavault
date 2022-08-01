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
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = DataVaultWorkerInstanceApp.class)
@Slf4j
@AddTestProperties
class MessageRecvIT extends BaseRabbitTCTest {

  private static final int MESSAGES_TO_SEND = 5;
  private static final int MESSAGES_TO_RECV = MESSAGES_TO_SEND;

  @Autowired
  AmqpAdmin admin;

  @Autowired
  RabbitTemplate template;

  @Autowired
  @Qualifier("workerQueue")
  Queue workerQueue;

  @MockBean
  MessageProcessor mProcessor;

  @MockBean
  ShutdownHandler mShutdownHandler;

  List<MessageInfo> messageInfos;

  private CountDownLatch latch;

  @Test
  @SneakyThrows
  void testSendAndRecvMessages() {
    Set<String> messageIds = new LinkedHashSet<>();
    this.latch = new CountDownLatch(MESSAGES_TO_RECV);
    for (long i = 0; i < MESSAGES_TO_SEND; i++) {
      messageIds.add(sendTestMessage(i));
    }
    //okay is true only after we've recvd 10 messages
    boolean okay = latch.await(3, TimeUnit.SECONDS);
    for (int i = 0; i < messageInfos.size(); i++) {
      log.info("[{}]{}", i, messageInfos.get(i));
    }
    if (!okay) {
      Assertions.fail("problem waiting for messageInfos");
    }

    verify(mProcessor, times(MESSAGES_TO_RECV)).processMessage(any(MessageInfo.class));
    verifyNoMoreInteractions(mProcessor, mShutdownHandler);

    int i=0;
    for(String messageId:messageIds){
      assertEquals(messageId, this.messageInfos.get(i++).getId());
    }
  }

  String sendTestMessage(long value) {
    MessageProperties props = new MessageProperties();
    props.setMessageId(UUID.randomUUID().toString());
    Message msg = new Message(String.valueOf(value).getBytes(StandardCharsets.UTF_8), props);
    template.send(workerQueue.getActualName(), msg);
    return props.getMessageId();
  }

  @BeforeEach
  void setup() {
    this.messageInfos = new ArrayList<>();
    admin.purgeQueue(workerQueue.getActualName());
    log.info("q[{}]purged prior to test", workerQueue.getActualName());

    doAnswer(invocation -> {
      Assertions.assertEquals(1, invocation.getArguments().length);
      MessageInfo info = invocation.getArgument(0);
      messageInfos.add(info);
      this.latch.countDown();
      return false;
    }).when(mProcessor).processMessage(any(MessageInfo.class));
  }

}
