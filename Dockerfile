FROM amazoncorretto:17-alpine3.19-jdk
RUN apk update && \
    apk add --no-cache redis
WORKDIR /app
COPY build/libs/ratelimitednotifications-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080 6379
CMD ["sh", "-c", "redis-server --bind 0.0.0.0 & java -jar app.jar"]
