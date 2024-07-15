package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private long id;
    @NotNull(message = "Логин не может быть пустым")
    @NotBlank(message = "Логин не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не может быть больше 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;


    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;

        if (releaseDate.isBefore(LocalDate.of(1895, 12, 29))) {
            throw new ValidationException("Дата фильма не может быть раньше 29 декабря 1895 года");
        }

        this.releaseDate = releaseDate;

//        if (duration.isNegative() || duration.isZero()) {
//            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
//        }

        this.duration = duration;
    }
}
