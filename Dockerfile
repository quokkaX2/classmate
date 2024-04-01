FROM openjdk:11

WORKDIR /classmate

COPY classmate-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
