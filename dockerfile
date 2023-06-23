FROM openjdk:11
ARG JAR_FILE=build/libs/f3f-dev1-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]