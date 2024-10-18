package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getList() {
        return new ArrayList<>(filmStorage.getList());
    }

    public void likeFilm(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NoSuchElementException("Фильм не найден: " + filmId);
        }

        User user = userService.getById(userId);
        if (user == null) {
            throw new NoSuchElementException("Пользователь не найден: " + userId);
        }

        film.getLikes().add(userId);
    }

    public void unLikeFilm(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NoSuchElementException("Фильм не найден: " + filmId);
        }

        User user = userService.getById(userId);
        if (user == null) {
            throw new NoSuchElementException("Пользователь не найден: " + userId);
        }

        film.getLikes().remove(userId);
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> films = new ArrayList<>(getList());
        films.sort(new TopFilmsComparator());
        count = (count == 0) ? 10 : count;
        return films.stream().limit(count).collect(Collectors.toList());
    }

    private static class TopFilmsComparator implements Comparator<Film> {
        @Override
        public int compare(Film film1, Film film2) {
            return Integer.compare(film1.getLikes().size(), film2.getLikes().size());
        }
    }
}