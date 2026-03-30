FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src

# CMD ["mvn", "test", "-e", "-B"]
ENTRYPOINT ["sh", "-c", "mvn test -Dwiremock.base.url=${WIREMOCK_BASE_URL} -e -B"]
