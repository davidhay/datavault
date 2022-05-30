package org.datavaultplatform.broker.test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.datavaultplatform.broker.initialise.InitialiseDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@Slf4j
@DirtiesContext
public abstract class BaseReuseDatabaseTest  {

  // This container is once per class - not once per method. Methods can 'dirty' the database.
  static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7").withReuse(true);
  @Autowired
  InitialiseDatabase initialiseDatabase;

  @Autowired
  protected JdbcTemplate template;

  @BeforeAll
  public static void beforeAll() {
    mysql.start();
  }

  @BeforeEach
  void cleanDB() {

    boolean allEmpty;

    List<String> tableNames = getTableNames();

    int i = 0;
    do {
      i++;
      tableNames.forEach(this::emptyTable);
      allEmpty = allEmpty(tableNames);
    } while (i < tableNames.size() && !allEmpty);
    log.info("after [{}] iterations, allEmpty={}", i, allEmpty);

    initialiseDatabase.initialiseDatabase();

    boolean postInitAllEmpty = allEmpty(tableNames);

    assertFalse(postInitAllEmpty);

  }

  List<String> getTableNames() {
    String SQL_2 = "show tables";
    List<String> tableNames = template.query(SQL_2,
        (rs, rowNum) -> rs.getString(1));
    return tableNames.stream().filter(name ->  !name.equalsIgnoreCase("hibernate_sequence")).collect(Collectors.toList());
  }

  void emptyTable(String tableName) {
    try {
      template.execute(String.format("delete from %s", tableName));
    } catch (DataAccessException ex) {
      log.error("oops " + ex.getMessage());
    }
  }

  public boolean allEmpty(List<String> tableNames) {
    for (String tableName : tableNames) {
      long count = template.queryForObject("select count(*) from " + tableName, Long.class);
      if (count > 0) {
        return false;
      }
    }
    return true;
  }

  @DynamicPropertySource
  static void setupProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.username", mysql::getUsername);
    registry.add("spring.datasource.password", mysql::getPassword);
    registry.add("spring.datasource.url", mysql::getJdbcUrl);
  }

}
