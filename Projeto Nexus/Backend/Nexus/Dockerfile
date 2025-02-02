# Use a imagem oficial do Maven que inclui o OpenJDK
FROM maven:latest

# Crie um diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo pom.xml com as dependencias
COPY pom.xml .

# Execute a construção com o Maven
RUN mvn clean install

# Copie o arquivo JAR construído para o diretório de trabalho
COPY target/Nexus-1.0-jar-with-dependencies.jar .

# Comando para iniciar o aplicativo Java
CMD ["java", "-jar", "Nexus-1.0-jar-with-dependencies.jar"]