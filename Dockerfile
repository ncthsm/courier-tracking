FROM openjdk:21-jdk

WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

RUN ./gradlew build --no-daemon

COPY . /app

RUN ./gradlew bootJar

CMD ["java", "-jar", "build/libs/couriertracking-1.0.0.jar"]