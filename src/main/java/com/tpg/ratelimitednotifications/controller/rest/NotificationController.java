package com.tpg.ratelimitednotifications.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class NotificationController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/sendNotification")
    public String sendNotification(@RequestBody NotificationRequest request) {
        String recipient = request.getRecipient();
        String notificationType = request.getNotificationType();

        int limit = getLimit(notificationType);
        if (limit <= 0) {
            return "Error: Invalid notification type";
        }

        String cacheKey = recipient + "_" + notificationType;
        Integer count = (Integer)redisTemplate.opsForValue().get(cacheKey);
        if (count != null && count >= limit) {
            return "Error: Rate limit exceeded for " + notificationType + " notifications";
        }

        redisTemplate.opsForValue().increment(cacheKey, 1);
        redisTemplate.expire(cacheKey, getExpiration(notificationType), TimeUnit.SECONDS);

        // Code to send notification

        return "Notification sent successfully";
    }

    private int getLimit(String notificationType) {
        switch (notificationType) {
            case "Status":
                return 2; // Not more than 2 per minute
            case "News":
                return 1; // Not more than 1 per day
            case "Marketing":
                return 3; // Not more than 3 per hour
            default:
                return -1; // Invalid notification type
        }
    }

    private long getExpiration(String notificationType) {
        switch (notificationType) {
            case "Status":
                return 60; // 1 minute
            case "News":
                return 86400; // 1 day
            case "Marketing":
                return 3600; // 1 hour
            default:
                return 0;
        }
    }

    static class NotificationRequest {
        private String recipient;
        private String notificationType;

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getNotificationType() {
            return notificationType;
        }

        public void setNotificationType(String notificationType) {
            this.notificationType = notificationType;
        }
    }
}
