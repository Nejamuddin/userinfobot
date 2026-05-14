FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package

FROM eclipse-temurin:17
COPY --from=build /target/*.jar app.jar
CMD ["java","-jar","app.jar"]
