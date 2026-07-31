# Etapa 1: Compilar la aplicación usando Maven oficial
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /code
COPY . /code
RUN mvn clean package

# Etapa 2: Imagen de ejecución ligera
FROM eclipse-temurin:17-jre-alpine    
WORKDIR /work/
COPY --from=build /code/target/quarkus-app/lib/ /work/lib/
COPY --from=build /code/target/quarkus-app/*.jar /work/
COPY --from=build /code/target/quarkus-app/app/ /work/app/
COPY --from=build /code/target/quarkus-app/quarkus/ /work/quarkus/

EXPOSE 8080
CMD ["java", "-Dquarkus.http.host=0.0.0.0", "-jar", "/work/quarkus-run.jar"]
