# ─────────────────────────────────────────
# Etapa 1: Build (compila el proyecto)
# ─────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia primero el pom.xml para aprovechar la caché de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el código fuente y compila
COPY src ./src
RUN mvn clean package -DskipTests

# ─────────────────────────────────────────
# Etapa 2: Runtime (solo ejecuta el .jar)
# ─────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crea carpeta para archivos subidos
RUN mkdir -p uploads

# Copia el .jar generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]