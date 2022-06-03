package org.datavaultplatform.broker.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.datavaultplatform.common.model.Permission;
import org.datavaultplatform.common.model.PermissionModel;
import org.datavaultplatform.common.model.RoleAssignment;
import org.datavaultplatform.common.model.RoleModel;
import org.datavaultplatform.common.model.RoleType;
import org.datavaultplatform.common.model.User;
import org.datavaultplatform.common.model.dao.PermissionDAO;
import org.datavaultplatform.common.model.dao.RoleAssignmentDAO;
import org.datavaultplatform.common.model.dao.RoleDAO;
import org.datavaultplatform.common.model.dao.UserDAO;
import org.junit.function.ThrowingRunnable;

public abstract class TestUtils {

  public static final Date NOW = new Date();
  public static final Date ONE_WEEK_AGO = datePlus(NOW, -7, ChronoUnit.DAYS);
  public static final Date TWO_WEEKS_AGO = datePlus(NOW, -14, ChronoUnit.DAYS);

  public static final Date ONE_YEAR_AGO = datePlus(NOW, -1, ChronoUnit.YEARS);

  public static final Date TWO_YEARS_AGO = datePlus(NOW, -2, ChronoUnit.YEARS);

  public static final Date THREE_YEARS_AGO = datePlus(NOW, -3, ChronoUnit.YEARS);

  public static Date datePlus(Date date, int amt, ChronoUnit unit) {
    Instant ins = Instant.ofEpochMilli(date.getTime());
    LocalDateTime local = LocalDateTime.ofInstant(ins, ZoneId.of("UTC"));
    LocalDateTime result = local.plus(amt, unit);
    return new Date(result.toInstant(ZoneOffset.UTC).toEpochMilli());
  }

  public static <T extends Exception> void checkException(Class<T> exceptionClass, String message, ThrowingRunnable runnable) {
    T ex = assertThrows(exceptionClass, runnable);
    assertEquals(message, ex.getMessage());
  }

  public static String useNewLines(String value) {
    return value.replace("\r\n","\n").replace("\r","\n");
  }

  public static ArrayList<String> getRandomList() {
    List<String> items = Stream.generate(new Random()::nextInt).map(Object::toString)
        .limit(100).collect(Collectors.toList());
    return new ArrayList<>(items);
  }

  public static HashMap<String,String> getRandomMap(){
    return getRandomList().stream().collect(Collectors.toMap(
        Function.identity(),
        Function.identity(),
        (k1,k2)->k1,
        HashMap::new));
  }

  public static HashMap<Integer,String> getRandomMapIntegerKey() {
    return Stream.generate(new Random()::nextInt)
        .limit(100)
        .collect(Collectors.toMap(
            Function.identity(),
            (item) -> item.toString(), (
            k1,k2)->k2,
            HashMap::new));
  }

  public static HashMap<Integer,byte[]> getRandomMapIntegerKeyByteArrayValue() {
    return Stream.generate(new Random()::nextInt)
        .limit(100)
        .collect(Collectors.toMap(
            Function.identity(),
            (item) -> item.toString().getBytes(StandardCharsets.UTF_8),
            (k1,k2)->k1,
            HashMap::new));
  }

  public static User createUserWithPermissions(
      UserDAO userDAO,
      PermissionDAO permissionDAO,
      RoleDAO roleDAO,
      RoleAssignmentDAO roleAssignmentDAO,
      String userId,
      String schoolId,
      Permission... permissions) {

    //create test user with specified 'usedId'
    User user = new User();
    user.setID(userId);
    user.setFirstname("first-"+userId);
    user.setLastname("last-"+userId);
    user.setEmail("test.user@test.com");
    userDAO.save(user);

    if(permissions.length > 0) {

      // create permissions
      List<PermissionModel> pms = Arrays.stream(permissions).map(p -> {
        PermissionModel pm = new PermissionModel();
        pm.setId(p.name());
        pm.setPermission(p);
        pm.setType(p.getDefaultType());
        pm.setLabel(p.getRoleName());
        permissionDAO.save(pm);
        return pm;
      }).collect(Collectors.toList());

      // create role with associated permissions
      RoleModel role = new RoleModel();
      role.setPermissions(pms);
      role.setName("test-role");
      role.setStatus("test-status"); //does this matter ?
      role.setType(RoleType.VAULT);//does this matter ?
      roleDAO.save(role);

      // link user with role
      RoleAssignment roleAssignment = new RoleAssignment();
      roleAssignment.setUserId(userId);
      roleAssignment.setRole(role);
      roleAssignment.setSchoolId(schoolId);
      roleAssignmentDAO.save(roleAssignment);
    }
    return user;
  }
}
