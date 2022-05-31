package org.datavaultplatform.common.model.dao;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.app.DataVaultBrokerApp;
import org.datavaultplatform.broker.test.AddTestProperties;
import org.datavaultplatform.broker.test.BaseDatabaseTest;
import org.datavaultplatform.broker.test.TestUtils;
import org.datavaultplatform.common.event.Event;
import org.datavaultplatform.common.event.deposit.Complete;
import org.datavaultplatform.common.event.deposit.ComputedChunks;
import org.datavaultplatform.common.event.deposit.ComputedEncryption;
import org.datavaultplatform.common.event.deposit.UploadComplete;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = DataVaultBrokerApp.class)
@AddTestProperties
@Slf4j
@TestPropertySource(properties = {
    "broker.email.enabled=false",
    "broker.controllers.enabled=false",
    "broker.rabbit.enabled=false",
    "broker.scheduled.enabled=false"
})
public class EventDAOIT extends BaseDatabaseTest {

  @Autowired
  EventDAO dao;

  @Test
  public void testComputedChunksChunksDigestBLOB() {
    ComputedChunks event = getComputedChunkEvent();
    dao.save(event);

    Event foundEvent = dao.findById(event.getID()).get();
    assertTrue(foundEvent instanceof ComputedChunks);
    ComputedChunks foundCCEvent = (ComputedChunks) foundEvent;

    assertEquals(event.getChunksDigest(), foundCCEvent.getChunksDigest());
  }

  @Test
  public void testCompleteArchiveIdsBLOB() {
    Complete event = getCompleteEvent();
    dao.save(event);

    Event foundEvent = dao.findById(event.getID()).get();
    assertTrue(foundEvent instanceof Complete);
    Complete foundCompletEevent = (Complete) foundEvent;

    assertEquals(event.getArchiveIds(), foundCompletEevent.getArchiveIds());
  }

  @Test
  public void testComputedEncryptionEventBLOBS() {
    ComputedEncryption event = getComputedEncryption();
    dao.save(event);

    Event foundEvent = dao.findById(event.getID()).get();
    assertTrue(foundEvent instanceof ComputedEncryption);
    ComputedEncryption foundComputedEncryptionEvent = (ComputedEncryption) foundEvent;

    assertEquals(event.getChunksDigest(), foundComputedEncryptionEvent.getChunksDigest());
    assertEquals(event.getEncChunkDigests(), foundComputedEncryptionEvent.getEncChunkDigests());
    assertEquals(event.getChunkIVs().keySet(), foundComputedEncryptionEvent.getChunkIVs().keySet());

    for(Integer key : event.getChunkIVs().keySet()){
      assertArrayEquals(event.getChunkIVs().get(key), foundComputedEncryptionEvent.getChunkIVs().get(key));
    }
  }

  @Test
  public void testUploadCompleteArchiveIdsBLOB() {
    UploadComplete event = getUploadComplete();
    dao.save(event);

    Event foundEvent = dao.findById(event.getID()).get();
    assertTrue(foundEvent instanceof UploadComplete);
    UploadComplete foundCompletEevent = (UploadComplete) foundEvent;

    assertEquals(event.getArchiveIds(), foundCompletEevent.getArchiveIds());
  }


  private ComputedChunks getComputedChunkEvent() {
    ComputedChunks event = new ComputedChunks();
    event.setChunkId("chunk-id-123");
    event.setChunksDigest(TestUtils.getRandomMapIntegerKey());
    return event;
  }

  private Complete getCompleteEvent() {
    Complete event = new Complete();
    event.setArchiveIds(TestUtils.getRandomMap());
    return event;
  }

  private ComputedEncryption getComputedEncryption() {
    ComputedEncryption event = new ComputedEncryption();

    event.setTarIV(TestUtils.getRandomList().stream().collect(Collectors.joining(",")).getBytes(
        StandardCharsets.UTF_8));
    event.setChunkIVs(TestUtils.getRandomMapIntegerKeyByteArrayValue());
    event.setEncChunkDigests(TestUtils.getRandomMapIntegerKey());
    event.setChunksDigest(TestUtils.getRandomMapIntegerKey());
    return event;
  }

  private UploadComplete getUploadComplete() {
    UploadComplete event = new UploadComplete();
    event.setArchiveIds(TestUtils.getRandomMap());
    return event;
  }
}
