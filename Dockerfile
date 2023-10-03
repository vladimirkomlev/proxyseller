#build stage
FROM gradle:latest AS BUILD
WORKDIR /src
COPY . .
RUN gradle build

#package stage
FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
