Rate Limited Notifications

This application provides an API endpoint for sending notifications while enforcing rate limits based on the notification type. It is built using Spring Boot and utilizes Redis for rate limiting.
Installation

To run this application locally, follow these steps:

    Clone the repository:

    bash

git clone https://github.com/your-username/rate-limited-notifications.git

Navigate to the project directory:

bash

cd rate-limited-notifications

Build the project:

bash

mvn clean package

Run the application:

bash

    java -jar target/rate-limited-notifications.jar

Usage

Once the application is running, you can send notifications by making a POST request to the /sendNotification endpoint with a JSON payload containing the recipient and notification type.

Example request:

http

POST /sendNotification
Content-Type: application/json

{
"recipient": "example@example.com",
"notificationType": "Status"
}

Rate Limits

Notifications are subject to the following rate limits based on the notification type:

    Status: Not more than 2 per minute.
    News: Not more than 1 per day.
    Marketing: Not more than 3 per hour.

If the rate limit is exceeded for a particular notification type, an error message will be returned.
Dependencies

This application utilizes the following dependencies:

    Spring Boot
    Redis
    Maven

Configuration

The rate limits and expiration times for each notification type can be configured in the NotificationController class.
License

This project is licensed under the MIT License - see the LICENSE file for details.