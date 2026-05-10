FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build

# copy maven wrapper and project files to use local wrapper if present
COPY pom.xml ./
COPY src src
# install maven then build (avoid depending on a local .mvn wrapper directory)
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