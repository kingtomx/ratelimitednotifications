package com.tpg.ratelimitednotifications.enums;

public enum NotificationType {
    STATUS(2),
    NEWS(1),
    MARKETING(3),
    UNKNOWN(-1);

    private final int limit;

    NotificationType(int limit) {
        this.limit = limit;
    }

    public static int getLimit(String notificationType) {
        for (NotificationType type : NotificationType.values()) {
            if (type.name().equalsIgnoreCase(notificationType)) {
                return type.limit;
            }
        }
        return UNKNOWN.limit;
    }
}