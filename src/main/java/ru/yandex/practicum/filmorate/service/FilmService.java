package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
        filmStorage.likeFilm(filmId, userId);
    }

    public void unLikeFilm(Long filmId, Long userId) {
        filmStorage.unLikeFilm(filmId, userId);
    }

    public List<Film> getTopFilms(Integer count) {
        count = (count == 0) ? 10 : count;
        return getList()
                .stream()
                .sorted(new TopFilmsComparator())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilm(Long filmId) {
        return filmStorage.getById(filmId).orElseThrow(() -> new NotFoundException("Фильм не найден: " + filmId));
    }

    private static class TopFilmsComparator implements Comparator<Film> {
        @Override
        public int compare(Film film1, Film film2) {
            return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
        }
    }
}