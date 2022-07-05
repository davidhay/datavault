#!/bin/bash

java -version

#this script runs the datavault webapp in 'standalone' mode - this helps test Spring and SpringSecurity config.
#this script uses Maven with Spring Boot specific goal
export PROJECT_ROOT=$(cd ../../;pwd)
cd $PROJECT_ROOT
DATAVAULT_HOME="$PROJECT_ROOT/dv5" \
 SPRING_PROFILES_ACTIVE=local \
 DATAVAULT_HOME="$PROJECT_ROOT/dv5/broker-local" \
 ./mvnw spring-boot:run  \
 -Dspring-boot.run.jvmArguments="-Xdebug \
 -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005" \
 --projects datavault-broker

