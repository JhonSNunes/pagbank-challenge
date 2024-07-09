FROM gradle:jdk21-alpine AS builder

WORKDIR /usr/app/

COPY . .

RUN gradle bootJar

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
RUN adduser spring root

ENV DOCKERIZE_VERSION v0.7.0

RUN apk update --no-cache \
    && apk add --no-cache wget openssl \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apk del wget

COPY --from=builder /usr/app/infrastructure/build/libs/*.jar /opt/app/application.jar

ENV DB_URL=pagbank_challenge_mysql:3306

USER root

CMD dockerize -wait tcp://$DB_URL -timeout 30s java -jar /opt/app/application.jar
