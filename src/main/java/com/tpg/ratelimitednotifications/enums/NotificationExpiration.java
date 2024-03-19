package com.tpg.ratelimitednotifications.enums;

public enum NotificationExpiration {
    STATUS(60),
    NEWS(86400),
    MARKETING(3600),
    UNKNOWN(0);

    private final long expirationInSeconds;

    NotificationExpiration(long expirationInSeconds) {
        this.expirationInSeconds = expirationInSeconds;
    }

    public long getExpirationInSeconds() {
        return expirationInSeconds;
    }

    public static long getExpiration(String notificationType) {
        for (NotificationExpiration type : NotificationExpiration.values()) {
            if (type.name().equalsIgnoreCase(notificationType)) {
                return type.expirationInSeconds;
            }
        }
        return UNKNOWN.expirationInSeconds;
    }
}

