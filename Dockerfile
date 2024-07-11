FROM gradle:jdk21-alpine AS builder

WORKDIR /usr/app/

COPY . .

RUN gradle bootJar

FROM eclipse-temurin:21-jre-alpine

COPY --from=builder /usr/app/infrastructure/build/libs/*.jar /opt/app/application.jar

CMD java -jar /opt/app/application.jar
