FROM openjdk:8-jre-slim

WORKDIR /app

COPY app.jar /app
COPY application.properties /app

ENV JAVA_OPTS=""
# ENV SPRING_CONFIG_LOCATION = "file:/app/application.properties"

RUN test -f "application.properties" && \ export SPRING_CONFIG_LOCATION = "file:/app/application.properties" || true

# java -jar app.jar 이 명령어를 실행한다는 뜻
ENTRYPOINT [ "java", "-jar", "app.jar" ]