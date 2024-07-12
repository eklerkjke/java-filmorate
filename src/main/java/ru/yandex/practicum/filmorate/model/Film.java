package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;


    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        this.name = name;
        if (description.length() > 200) {
            throw new ValidationException("Описание не может быть более 200 символов");
        }

        this.description = description;

        if (releaseDate.isBefore(LocalDate.of(1895, 12, 29))) {
            throw new ValidationException("Дата фильма не может быть раньше 29 декабря 1895 года");
        }

        this.releaseDate = releaseDate;

        if (duration.isNegative() || duration.isZero()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        this.duration = duration;
    }
}
