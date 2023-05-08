FROM gradle:jdk17 as builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
ENTRYPOINT java -jar app.jar