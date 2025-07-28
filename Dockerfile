# Etapa de build
FROM ubuntu:latest AS build
LABEL authors="PATRICK-ANJOS"
# Instalar o JDK 21 e Maven
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk maven

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o pom.xml e baixar as dependências do Maven
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar o restante do código-fonte
COPY src ./src

# Compilar o projeto
RUN mvn clean install -DskipTests

# Etapa de runtime
FROM openjdk:21-jdk-slim

# Definir o diretório de trabalho
WORKDIR /app

COPY --from=build /app/target/oauth-register-0.0.1.jar oauth-register.jar

EXPOSE 7009

ENTRYPOINT [ "java", "-jar", "oauth-register.jar" ]