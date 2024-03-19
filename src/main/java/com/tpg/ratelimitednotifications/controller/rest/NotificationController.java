package com.tpg.ratelimitednotifications.controller.rest;

import com.tpg.ratelimitednotifications.entity.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static com.tpg.ratelimitednotifications.enums.NotificationExpiration.getExpiration;
import static com.tpg.ratelimitednotifications.enums.NotificationType.getLimit;

@RestController
public class NotificationController {

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/sendNotification")
    public String sendNotification(@RequestBody NotificationRequest request) {
        String recipient = request.getRecipient();
        String notificationType = request.getNotificationType();
        int limit = getLimit(notificationType);
        if (limit <= 0) {
            return "Error: Invalid notification type";
        }
        String cacheKey = String.format("%s=%s", recipient, notificationType);
        Integer count = (Integer)redisTemplate.opsForValue().get(cacheKey);
        if (count != null && count >= limit) {
            return String.format("Error: Rate limit exceeded for %s notifications", notificationType);
        }
        redisTemplate.opsForValue().increment(cacheKey, 1);
        redisTemplate.expire(cacheKey, getExpiration(notificationType), TimeUnit.SECONDS);
        return "Notification sent successfully";
    }

}
