package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film newFilm);

    Optional<Film> getById(Long id);

    List<Film> getList();

    void likeFilm(Long filmId, Long userId);

    void unLikeFilm(Long filmId, Long userId);

    Set<Long> getLikesFilm(Long filmId);

    List<Film> getTopFilms();
}
