#!/bin/bash

java -version

export PROJECT_ROOT=$(cd ../../;pwd)
cd $PROJECT_ROOT

./mvnw clean integration-test -Dskip.unit.tests -pl datavault-webapp
echo '**********************************************************************'
echo -e "There is a problem getting ng-tests to work with integration tests.\nTherefore all webapp tests are classed as unit tests"
echo '**********************************************************************'
