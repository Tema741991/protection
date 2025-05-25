package ru.top.cinemas.entities;

public enum BookingStatus {

        RESERVED("Забронировано"),
        SOLD("Продано"),
        CANCELLED("Отменено"),
        RETURNED("Возвращено");
    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
