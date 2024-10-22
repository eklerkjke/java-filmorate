package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        return filmStorage.getList();
    }

    public void likeFilm(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        if (isUserExist(userId)) {
            film.getLikes().add(userId);
        }
    }

    public void unLikeFilm(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        if (isUserExist(userId)) {
            film.getLikes().remove(userId);
        }
    }

    public List<Film> getTopFilms(Integer count) {
        count = (count == 0) ? 10 : count;
        return getList()
                .stream()
                .sorted(new TopFilmsComparator())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Film getFilm(Long filmId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм не найден: " + filmId);
        }

        return film;
    }

    private Boolean isUserExist(Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден: " + userId);
        }

        return true;
    }

    private static class TopFilmsComparator implements Comparator<Film> {
        @Override
        public int compare(Film film1, Film film2) {
            return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
        }
    }
}