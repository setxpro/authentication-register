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

# Define variáveis de ambiente
ENV URI_CONNECTION=from_compose
ENV DB_USER=from_compose
ENV DB_PASSWORD=from_compose
ENV SECRET_TOKEN=from_compose
ENV URL_VALIDATION=from_compose
ENV URL_NOTIFICATION=from_compose
ENV SERVICE_DISCOVERY=from_compose

COPY --from=build /app/target/oauth-register-0.0.1.jar oauth-register.jar

EXPOSE 7009

ENTRYPOINT [ "java", "-jar", "oauth-register.jar" ]