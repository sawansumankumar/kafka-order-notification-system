FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /build

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src/ src/

RUN sed -i 's/\r$//' mvnw \
    && chmod +x mvnw \
    && ./mvnw -B package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN groupadd --gid 1000 render \
    && useradd --system --uid 10001 --gid render appuser

COPY --from=build /build/target/*.jar app.jar

USER appuser

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]