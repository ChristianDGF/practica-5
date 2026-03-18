# ETAPA 1: Construcción (Build)
# Utilizamos una imagen con Gradle y Java 21 (la versión que tienes en tu build.gradle)
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copiamos los archivos de configuración de Gradle primero (para aprovechar el caché de Docker)
COPY build.gradle settings.gradle ./
COPY src ./src

# Compilamos la aplicación omitiendo las pruebas para que sea más rápido
RUN gradle clean build -x test

# ETAPA 2: Ejecución (Run)
# Utilizamos una imagen ligera de Java 21 para correr la app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el .jar generado en la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Exponemos un puerto por defecto (aunque Docker Compose lo inyectará)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]