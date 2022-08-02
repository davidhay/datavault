package org.datavaultplatform.worker.app;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.common.crypto.Encryption;
import org.datavaultplatform.worker.config.ActuatorConfig;
import org.datavaultplatform.worker.config.EncryptionConfig;
import org.datavaultplatform.worker.config.EventSenderConfig;
import org.datavaultplatform.worker.config.PropertiesConfig;
import org.datavaultplatform.worker.config.QueueConfig;
import org.datavaultplatform.worker.config.RabbitConfig;
import org.datavaultplatform.worker.config.ReceiverConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

@SpringBootApplication
@Import({
    PropertiesConfig.class,
    ActuatorConfig.class,
    QueueConfig.class,
    EventSenderConfig.class,
    ReceiverConfig.class,
    RabbitConfig.class,
    EncryptionConfig.class
})
@Slf4j
public class DataVaultWorkerInstanceApp implements CommandLineRunner {

  @Autowired
  Environment env;

  @Value("${spring.application.name}")
  String applicationName;

  @Value("${validate.encryption.config:false}")
  boolean validateEncryptionConfig;

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


  @Override
  public void run(String... args) {
    log.info("java.version [{}]", env.getProperty("java.version"));
    log.info("java.vendor [{}]", env.getProperty("java.vendor"));

    log.info("os.arch [{}]", env.getProperty("os.arch"));
    log.info("os.name [{}]", env.getProperty("os.name"));

    log.info("git.commit.id.abbrev [{}]", env.getProperty("git.commit.id.abbrev", "-1"));

    log.info("spring.security.debug [{}]", env.getProperty("spring.security.debug","false"));
    log.info("spring-boot.version [{}]", SpringBootVersion.getVersion());
    log.info("active.profiles {}", (Object) env.getActiveProfiles());

    log.info("validate.encryption.config [{}]", validateEncryptionConfig);

    if (validateEncryptionConfig) {
      validateEncryptionConfig(null);
      validateEncryptionConfig(Encryption.getVaultPrivateKeyEncryptionKeyName());
      validateEncryptionConfig(Encryption.getVaultDataEncryptionKeyName());
    } else {
      log.info("Encryption Config NOT CHECKED");
    }
  }


  private void validateEncryptionConfig(String keyName) throws IllegalStateException {
    String randomSecret = UUID.randomUUID().toString();
    String encryptedThenDecrypted = encryptThenDecrypt(randomSecret, keyName);
    Assert.isTrue(randomSecret.equals(encryptedThenDecrypted),
        () -> String.format("Problem  with the setup of Encryption using keyName[%s]", keyName));
    log.info("Encryption Config is Valid");
  }

  private String encryptThenDecrypt(String plainText, String keyName) {
    try {
      byte[] iv = Encryption.generateIV();
      // null secretKey => the (Vault)PrivateKeyEncryptionKeyName (for SSH KEYS - NOT DATA)
      SecretKey secretKey = keyName == null ? null : Encryption.getSecretKeyFromKeyStore(keyName);
      byte[] encrypted = Encryption.encryptSecret(plainText, secretKey, iv);
      byte[] decrypted = Encryption.decryptSecret(encrypted, iv, secretKey);
      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception ex) {
      throw new IllegalStateException("Encryption Config is NOT VALID", ex);
    }
  }
}