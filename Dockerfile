FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build.gradle gradlew settings.gradle ./
COPY gradle/ gradle/

RUN ./gradlew wrapper

COPY src/ src

RUN ./gradlew clean build

ENTRYPOINT ["java","-jar","build/libs/sleep-0.0.1-SNAPSHOT.jar"]
