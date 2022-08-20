FROM openjdk:18.0-jdk

RUN mkdir -p /usr/src/app
COPY build/libs/BotApp.jar /usr/src/app
WORKDIR /usr/src/app
EXPOSE 5005
EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "BotApp.jar"]