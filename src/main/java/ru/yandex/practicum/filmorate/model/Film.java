package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.adapter.DurationSerializer;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Validated
public class Film {
    private long id;
    @NotNull(message = "Логин не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не может быть больше 200 символов")
    private String description;
    private LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Duration duration;


    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
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
