#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim as build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install -DskipTests

#
#Run stage
#
FROM adoptopenjdk/openjdk8:alpine-slim
COPY --from=build /home/app/target/parquimetro-app.jar /usr/local/lib/parquimetro-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/parquimetro-app.jar"]