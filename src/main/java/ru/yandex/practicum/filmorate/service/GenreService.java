package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage storage;

    public Genre getRatingById(long id) {
        return storage.getById(id)
                .orElseThrow(() -> new NotFoundException("Не найден жанр: " + id));
    }

    public List<Genre> getAll() {
        return storage.getList();
    }
}