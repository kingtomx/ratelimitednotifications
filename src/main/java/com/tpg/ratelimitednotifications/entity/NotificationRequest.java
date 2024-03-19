package com.tpg.ratelimitednotifications.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class NotificationRequest {
    private String recipient;
    private String notificationType;

}
