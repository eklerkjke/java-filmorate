package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> getById(Long id);

    List<Genre> getList();

    List<Genre> getGenresByFilmId(Long filmId);
}
