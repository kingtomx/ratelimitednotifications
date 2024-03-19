# Ratelimitednotifications Service

This is a sample service that uses Spring Boot and Redis to send rate-limited notifications.

## Run the Service

### Build the Docker Container

To build the Docker container, make sure you have Docker installed on your system and follow these steps:

1. Clone the repository:

    ```bash
    git clone https://github.com/your-user/ratelimitednotifications.git
    ```

2. Navigate to the project directory:

    ```bash
    cd ratelimitednotifications
    ```

3. Build the Docker container by running the following command:

    ```bash
    docker build -t ratelimitednotifications .
    ```

### Run the Docker Container

Once the Docker container has been built, you can run it with the following command:

```bash
docker run -d -p 8080:8080 -p 6379:6379 ratelimitednotifications
```

This will start the container in the background, exposing ports 8080 and 6379 on your local machine.

## Make a Call to the Service

To make a call to the service using cURL, you can run the following command in your terminal:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"recipient": "user@example.com", "notificationType": "Status"}' http://localhost:8080/sendNotification
```

This will send a POST request to the `/sendNotification` endpoint of the service with a JSON object containing recipient information and notification type.

You will receive a response indicating whether the notification was successfully sent or if the rate limit for the notification type was exceeded.

In terms of horizontal scalability, to ensure this app can be running several instances in parallel, we could provide a separated Redis container using a docker compose (for example) but to make it easy to test I decided to maintain everything inside the same container. 