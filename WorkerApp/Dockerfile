FROM openjdk:18.0-jdk

RUN mkdir -p /usr/src/app
COPY build/libs/WorkerApp.jar /usr/src/app
WORKDIR /usr/src/app
EXPOSE 5006
EXPOSE 8080

CMD ["java", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "WorkerApp.jar"]
