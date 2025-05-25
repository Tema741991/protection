package ru.top.cinemas.entities;

public enum SessionStatus {

    DRAFT("Черновик"),
    PUBLISHED("Опубликован"),
    ARCHIVED("Завершен"),
    CANCELED("Отменен");//

    private final String displayName;

    SessionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
