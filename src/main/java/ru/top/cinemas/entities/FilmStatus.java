package ru.top.cinemas.entities;

public enum FilmStatus {
    SOON("Cкоро"),   // Скоро в прокате
    NOW("Сейчас"),       // Сейчас в прокате
    ARCHIVED("Архив");   // В архиве

    private final String displayName;

    FilmStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}