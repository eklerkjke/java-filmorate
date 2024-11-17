package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
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
    private Mpa mpa;
    private Set<Genre> genres;
    public Set<Long> likes;


    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa, Set<Genre> genres, Set<Long> likes) {
        this.id = (id == null) ? 0 : id;
        this.name = name;
        this.description = description;

        if (releaseDate.isBefore(LocalDate.of(1895, 12, 29))) {
            throw new ValidationException("Дата фильма не может быть раньше 29 декабря 1895 года");
        }

        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes != null ? likes : new HashSet<>();
        this.genres = genres != null ? genres : new LinkedHashSet<>();
        this.mpa = mpa;
    }
}
