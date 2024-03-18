package com.tpg.ratelimitednotifications.controller.rest;

import com.tpg.ratelimitednotifications.entity.NotificationRequest;
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

        String cacheKey = String.format("%s_%s", recipient, notificationType);
        Integer count = (Integer)redisTemplate.opsForValue().get(cacheKey);
        if (count != null && count >= limit) {
            return String.format("Error: Rate limit exceeded for %s notifications", notificationType);
        }

        redisTemplate.opsForValue().increment(cacheKey, 1);
        redisTemplate.expire(cacheKey, getExpiration(notificationType), TimeUnit.SECONDS);

        // Code to send notification

        return "Notification sent successfully";
    }

    private int getLimit(String notificationType) {
        return switch (notificationType) {
            case "Status" -> 2; // Not more than 2 per minute
            case "News" -> 1; // Not more than 1 per day
            case "Marketing" -> 3; // Not more than 3 per hour
            default -> -1; // Invalid notification type
        };
    }

    private long getExpiration(String notificationType) {
        return switch (notificationType) {
            case "Status" -> 60; // 1 minute
            case "News" -> 86400; // 1 day
            case "Marketing" -> 3600; // 1 hour
            default -> 0;
        };
    }

}
