FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17

WORKDIR /app

COPY --from=build /app/target/*.war app.war

EXPOSE 10000

CMD ["java", "-Dserver.port=10000", "-jar", "app.war"]
