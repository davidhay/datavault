We can override the snap.version in the maven poms.
This ensures we can't be picking up OLD datavault-common-0.0.1-SHAPSHOT.jar from local maven repo.

./mvnw clean install -Dskip.unit.tests -Dskip.integration.tests

$ tar tvf datavault-webapp/target/datavault-webapp.jar | grep datavault-common
-rw-r--r--  0 0      0      494756 20 Jan 18:19 BOOT-INF/lib/datavault-common-0.0.1-SNAPSHOT.jar

./mvnw -Dsnap.version=0.0.23fe2224-SNAPSHOT clean install -Dskip.unit.tests -Dskip.integration.tests

$ tar tvf datavault-webapp/target/datavault-webapp.jar | grep datavault-common
-rw-r--r--  0 0      0      494772 20 Jan 18:43 BOOT-INF/lib/datavault-common-0.0.23fe2224-SNAPSHOT.jar