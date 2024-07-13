package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping()
    public Film add(@RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            log.warn("Фильм не был найден");
            throw new RuntimeException("Фильм не найден");
        }

        Film film = films.get(newFilm.getId());
        if (newFilm.getName() != null) {
            log.debug("Смена названия фильма");
            film.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null) {
            log.debug("Смена описания фильма");
            film.setDescription(newFilm.getDescription());
        }
        if (newFilm.getDuration() != null) {
            log.debug("Смена длительности фильма");
            film.setDuration(newFilm.getDuration());
        }
        if (newFilm.getReleaseDate() != null) {
            log.debug("Смена даты выхода фильма");
            film.setReleaseDate(newFilm.getReleaseDate());
        }
        log.info("Фильм обновлен");

        return film;
    }

    @GetMapping()
    public List<Film> getList() {
        return new ArrayList<>(films.values());
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
