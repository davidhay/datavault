package org.datavaultplatform.webapp.actuator;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrokerStatus {

  private final Map<String, Object> values = new HashMap<>();

  public BrokerStatus(String status){
    values.put("status", status);
  }

  @JsonAnyGetter
  public Map<String, Object> getBrokerStatus() {
    return this.values;
  }
}
