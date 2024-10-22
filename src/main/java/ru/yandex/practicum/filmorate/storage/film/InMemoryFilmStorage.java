package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film add(Film film) {
        if (film.getId() > 0 && films.containsKey(film.getId())) {
            throw new BadRequestException("Нельзя доабвить фильм с таким ID: " + film.getId());
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            log.warn("Фильм не был найден");
            throw new NotFoundException("Фильм не найден");
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

        return newFilm;
    }

    @Override
    public void delete(Film newFilm) {
        films.remove(newFilm.getId());
        log.info("Фильм удален");
    }

    @Override
    public Film getById(Long id) {
        return films.get(id);
    }

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
