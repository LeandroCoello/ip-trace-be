FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.war meli-ip-trace-be.war
ENTRYPOINT ["java","-jar","/meli-ip-trace-be.war"]