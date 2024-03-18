package com.tpg.ratelimitednotifications.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationRequest {
    private String recipient;
    private String notificationType;

}
