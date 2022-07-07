#!/bin/bash

java -version

export PROJECT_ROOT=$(cd ../../;pwd)
cd $PROJECT_ROOT
DATAVAULT_HOME="$PROJECT_ROOT/dv5" \
 SPRING_PROFILES_ACTIVE=database \
 SPRING_SECURITY_DEBUG=true \
 SERVER_PORT=8888 \
 DATAVAULT_HOME="$PROJECT_ROOT/dv5/local/webapp" \
 ./mvnw spring-boot:run  \
 -Dspring-boot.run.jvmArguments="-Xdebug \
 -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5050" \
 --projects datavault-webapp

