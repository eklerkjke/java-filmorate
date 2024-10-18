package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film newFilm);

    void delete(Film newFilm);

    Film getById(Long id);

    List<Film> getList();
}
