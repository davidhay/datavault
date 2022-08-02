package org.datavaultplatform.common.docker;

import org.testcontainers.utility.DockerImageName;
import static org.testcontainers.utility.DockerImageName.parse;

public abstract class DockerImage {

  //bitnami/openldap:2.6.3 was latest on 2/Aug/2022
  public static DockerImageName LDAP_IMAGE = parse("bitnami/openldap:2.6.3");


  // mysql:5.7.39 was latest on 2/Aug/2022
  public static DockerImageName MYSQL_IMAGE = parse("mysql:5.7.39");


  // mailhog/mailhog:v1.0.1 was latest on 2/Aug/2022
  public static DockerImageName MAIL_IMAGE = parse("mailhog/mailhog:v1.0.1");


  // nginx:1:23:1 was 'latest' on 2/Aug/2022
  // ( this image has 'scp' and runs a configurable ssh daemon )
  public static DockerImageName NGINX_IMAGE = parse("nginx:1:23:1");


  // rabbitmq:3.10.6-management-alpine was 'latest' on 2/Aug/2022
  public static final String RABBIT_IMAGE_NAME = "rabbitmq:3.10.6-management-alpine";

  // this image has 'openssl'
  // version-8.8_p1-r1 was 'latest' on 2/Aug/2022
  public static String OPEN_SSH_8pt8_IMAGE_NAME = "linuxserver/openssh-server:version-8.8_p1-r1";
  public static DockerImageName OPEN_SSH_IMAGE = parse(OPEN_SSH_8pt8_IMAGE_NAME);

  public static String OPEN_SSH_8pt3_IMAGE_NAME = "linuxserver/openssh-server:version-8.3_p1-r1";
  public static String OPEN_SSH_8pt4_IMAGE_NAME = "linuxserver/openssh-server:version-8.4_p1-r3";

  public static String OPEN_SSH_8pt6_IMAGE_NAME = "linuxserver/openssh-server:version-8.6_p1-r3";
  public static String OPEN_SSH_8pt7_IMAGE_NAME = "linuxserver/openssh-server:version-8.7_p1-r3";

}
