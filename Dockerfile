# syntax=docker/dockerfile:1
FROM --platform=linux/amd64 eclipse-temurin:17-jdk-jammy as base
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
COPY ./data ./data
RUN ./mvnw clean install

FROM base as test
RUN ["./mvnw", "test"]

FROM base as dev
CMD ["./mvnw", "spring-boot:run"]
#CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=dev"]

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
EXPOSE 8080
COPY --from=base /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/opt/app/*.jar" ]