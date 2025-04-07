FROM maven:3.8.8-eclipse-temurin-21-alpine AS build

WORKDIR /planet

COPY . .

RUN mvn -Dmaven.test.skip clean install -f pom.xml

FROM maven:3.8.8-eclipse-temurin-21-alpine AS reservation-service-build

WORKDIR /planet

COPY --from=build /planet /planet

RUN mvn -Dmaven.test.skip clean package -f /planet/reservation-service/pom.xml

CMD ls -l /planet/reservation-service/target

FROM maven:3.8.8-eclipse-temurin-21-alpine AS notification-service-build

WORKDIR /planet

COPY --from=build /planet /planet

RUN mvn -Dmaven.test.skip clean package -f /planet/notification-service/pom.xml

FROM eclipse-temurin:21-jre AS final

WORKDIR /app

COPY --from=reservation-service-build /planet/reservation-service/target/reservation-service-0.0.1-SNAPSHOT.jar /app/reservation-service.jar
COPY --from=notification-service-build /planet/notification-service/target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service.jar

CMD ["java", "-jar", "/app/reservation-service.jar"]
CMD ["java", "-jar", "/app/notification-service.jar"]
