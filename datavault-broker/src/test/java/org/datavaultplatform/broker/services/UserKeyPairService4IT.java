package org.datavaultplatform.broker.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.services.UserKeyPairService.KeyPairInfo;
import org.datavaultplatform.broker.test.JSchLogger;
import org.datavaultplatform.common.docker.DockerImage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * This test generates a key pair and checks that the keypair is valid by ...
 * using the private key with Jsch and public key with OPENSSH to establish ssh connection
 */
@Slf4j
public class UserKeyPairService4IT extends BaseUserKeyPairServiceTest {

  public static final String TEST_PASSPHRASE = "tenet";
  private static final String TEST_USER = "testuser";
  private GenericContainer toContainer;

  private static final String ENV_USER_NAME = "USER_NAME";
  private static final String ENV_PUBLIC_KEY = "PUBLIC_KEY";

  /**
   * Tests that the key pair is valid by
   * using keypair to perform scp between testcontainers
   */

  @Test
  @Override
  @SneakyThrows
  void testKeyPairIsValid() {
    UserKeyPairService service = new UserKeyPairService(TEST_PASSPHRASE);
    KeyPairInfo info = service.generateNewKeyPair();
    validateKeyPair(info.getPublicKey(), info.getPrivateKey().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  @SneakyThrows
  void testKeyPairIsInValid() {
    UserKeyPairService service = new UserKeyPairService(TEST_PASSPHRASE);
    KeyPairInfo info = service.generateNewKeyPair();
    byte[] badBytes = info.getPrivateKey().getBytes(StandardCharsets.UTF_8);
    //just by changing 1 byte of private key - we should get an error
    badBytes[0] = (byte)166;
    JSchException ex = assertThrows(JSchException.class, () -> validateKeyPair(info.getPublicKey(), badBytes ));
    assertTrue(ex.getMessage().startsWith("invalid privatekey"));
  }


  @SneakyThrows
  private void validateKeyPair(String publicKey, byte[] privateKeyBytes) {

    initContainers(publicKey);
    JSch.setLogger(new JSchLogger());
    JSch jSch = new JSch();
    Session session = jSch.getSession(TEST_USER, "localhost", this.toContainer.getMappedPort(2222));
    jSch.addIdentity(TEST_USER, privateKeyBytes, null, TEST_PASSPHRASE.getBytes());
    java.util.Properties properties = new java.util.Properties();
    properties.put("StrictHostKeyChecking", "no");
    session.setConfig(properties);
    try {
      session.connect();
    } catch(JSchException ex) {
      fail("ssh error",ex);
    }
    log.info("Connected!");
  }

  void initContainers(String publicKey) {
    //we put the publicKey into the TO container at startup - so ssh daemon will trust the private key later on
    toContainer = new GenericContainer(DockerImage.OPEN_SSH_8pt8_IMAGE_NAME)
        .withEnv(ENV_USER_NAME, TEST_USER)
        .withEnv(ENV_PUBLIC_KEY, publicKey) //this causes the public key to be added to /config/.ssh/authorized_keys
        .withExposedPorts(2222)
        .waitingFor(Wait.forListeningPort());


    toContainer.start();
  }

  @AfterEach
  void tearDown() {
    if(this.toContainer != null){
      this.toContainer.stop();
    }
  }
}
