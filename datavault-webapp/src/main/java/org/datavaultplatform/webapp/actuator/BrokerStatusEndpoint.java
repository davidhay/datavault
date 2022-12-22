package org.datavaultplatform.webapp.actuator;

import org.datavaultplatform.webapp.services.RestService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@Endpoint(id="brokerstatus")
public class BrokerStatusEndpoint {

  private RestService restService;

  public BrokerStatusEndpoint(RestService restService) {
      this.restService = restService;
  }

  @ReadOperation
  public BrokerStatus brokerStatus() {
    return new BrokerStatus(restService.brokerStatus());
  }

}