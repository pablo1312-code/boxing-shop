# ─── Etapa 1: Compilación ─────────────────────────────────────────────────────
# Usamos una imagen con Maven y Java 17 para compilar el proyecto
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos primero el pom.xml para aprovechar la caché de capas de Docker:
# si el pom no cambia, Maven no re-descarga las dependencias en cada build
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Ahora copiamos el código fuente y compilamos (sin tests, ya los hemos pasado)
COPY src ./src
RUN mvn clean package -DskipTests -q

# ─── Etapa 2: Imagen de producción ────────────────────────────────────────────
# Imagen mucho más ligera: solo JRE, sin Maven ni código fuente
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos solo el .jar generado en la etapa anterior
COPY --from=build /app/target/boxing-shop-1.0.0.jar app.jar

EXPOSE 8080

# Arranca el backend
ENTRYPOINT ["java", "-jar", "app.jar"]
