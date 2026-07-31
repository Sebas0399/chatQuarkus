# Etapa 1: Compilar la aplicación usando Maven con Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /code

# Forzamos la copia del archivo pom y la carpeta src
COPY pom.xml /code/
COPY src /code/src

RUN mvn clean package -DskipTests

# Etapa 2: Imagen de ejecución ligera con Java 21
FROM eclipse-temurin:21-jre-alpine    
WORKDIR /work/
COPY --from=build /code/target/quarkus-app/lib/ /work/lib/
COPY --from=build /code/target/quarkus-app/*.jar /work/
COPY --from=build /code/target/quarkus-app/app/ /work/app/
COPY --from=build /code/target/quarkus-app/quarkus/ /work/quarkus/

EXPOSE 8080
CMD ["java", "-Dquarkus.http.host=0.0.0.0", "-jar", "/work/quarkus-run.jar"]
