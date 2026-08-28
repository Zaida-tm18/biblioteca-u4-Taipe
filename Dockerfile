# =====================================================================
#  Imagen de la aplicacion. Construccion multietapa.
#  Etiquetas verificadas en Docker Hub (agosto de 2026):
#    maven:3.9.16-eclipse-temurin-25-alpine
#    eclipse-temurin:25-jre-alpine
#  No se usa Maven Wrapper: IntelliJ IDEA trae Maven incorporado y el
#  laboratorio ya lo tiene instalado. Un wrapper mal versionado es una de
#  las causas mas frecuentes de "en mi maquina si compila".
# =====================================================================

# ---------- etapa 1: compilacion ----------
FROM maven:3.9.16-eclipse-temurin-25-alpine AS constructor
WORKDIR /build
# Primero solo el pom: asi la capa de dependencias se cachea y no se
# vuelve a descargar cuando cambia unicamente el codigo fuente.
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline
COPY src/ src/
RUN mvn -B clean package -DskipTests

# ---------- etapa 2: ejecucion ----------
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN addgroup -S app && adduser -S app -G app
COPY --from=constructor /build/target/*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
