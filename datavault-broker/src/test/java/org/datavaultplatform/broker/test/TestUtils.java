package org.datavaultplatform.broker.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.function.ThrowingRunnable;

public abstract class TestUtils {

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
}
