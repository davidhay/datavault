package org.datavaultplatform.broker.test;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DirtiesContext
@Testcontainers
public abstract class BaseDatabaseTest {

  @Container
  // This container is once per class - not once per method. Methods can 'dirty' the database.
  static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7");

  @DynamicPropertySource
  public static void setupProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.username", mysql::getUsername);
    registry.add("spring.datasource.password", mysql::getPassword);
    registry.add("spring.datasource.url", mysql::getJdbcUrl);
  }

}
