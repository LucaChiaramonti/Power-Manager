FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build

COPY pom.xml ./
COPY src src
RUN apt-get update \
	&& apt-get install -y maven \
	&& mvn -B -DskipTests package \
	&& rm -rf /var/lib/apt/lists/*

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache netcat-openbsd
COPY --from=builder /build/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]