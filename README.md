# comp307-booking-backend
Backend Repository for COMP 307 Team Final Project


# Local Execution Instructions 
If running with a local configuration (e.g. using H2 for DB), define an application-local.properties file with custom configurations within the resources/ folder and run the application with the following command: 

./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=local"
