FROM openjdk:8-alpine

COPY target/uberjar/merry.jar /merry/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/merry/app.jar"]
