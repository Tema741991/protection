package ru.top.cinemas.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.top.cinemas.entities.FilmStatus;
import ru.top.cinemas.entities.Genre;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
@Data
public class FilmDTO {
    private Long id;

    @NotBlank(message = "Название фильма обязательно")
    @Size(min = 1, max = 255, message = "Название фильма должно быть от 1 до 255 символов")
    private String title;

    @NotNull(message = "Год выпуска обязателен")
    @Min(value = 1900, message = "Год выпуска не может быть раньше 1895")
    @Max(value = 2100, message = "Год выпуска не может быть позже 2100")
    private int year;

    @NotBlank(message = "Описание фильма обязательно")
    @Size(min = 10, max = 2000, message = "Описание должно быть от 10 до 2000 символов")
    private String description;

    @NotNull(message = "Рейтинг обязателен")
    @DecimalMin(value = "0.0", message = "Рейтинг не может быть меньше 0")
    @DecimalMax(value = "10.0", message = "Рейтинг не может быть больше 10")
    @Digits(integer = 2, fraction = 1, message = "Рейтинг должен содержать максимум одну цифру после запятой")
    private BigDecimal rating;


    @NotNull(message = "Продолжительность обязательна")
    @Min(value = 1, message = "Фильм должен длиться минимум 1 минуту")
    @Max(value = 600, message = "Фильм не может длиться более 600 минут (10 часов)")
    private int duration;

    private String posterPath;

    @NotEmpty(message = "Укажите хотя бы один жанр")
    private Set<Long> genreIds;

    private List<Genre> genres;

    @NotNull(message = "Возрастной рейтинг обязателен")
    private Long ageRatingId;

    private String ageRatingCode;
    private FilmStatus status;
}
