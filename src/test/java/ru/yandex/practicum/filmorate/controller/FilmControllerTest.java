package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void setUp() {
        UserService userService = new UserService(new InMemoryUserStorage());
        FilmService filmService = new FilmService(new InMemoryFilmStorage());
        filmController = new FilmController(filmService);
        film1 = new Film(
                "name 1",
                "description 1",
                LocalDate.of(2000, 2, 4),
                90
        );
        film2 = new Film(
                "name 2",
                "description 2",
                LocalDate.of(20022, 7, 14),
                60
        );
    }

    @Test
    void addTest() {
        Film film = filmController.add(film1);
        film1.setId(film.getId());
        assertEquals(film, film1, "Фильмы не одинаковы");
        assertNotEquals(film, film2, "Фильмы одинаковы");
    }

    @Test
    void updateTest() {
        Film film = filmController.add(film1);
        film.setName("name 3");
        Film newFilm = filmController.update(film);

        assertEquals(film.getName(), newFilm.getName(), "Поле не обновилось");
        assertEquals(1, filmController.getList().size(), "Список изменился");
    }

    @Test
    void getListTest() {
        filmController.add(film1);
        filmController.add(film2);

        List<Film> films = filmController.getList();
        assertNotNull(films, "Список фильмов пустой");
        assertTrue(films.contains(film2), "Фильм не добавился в список");
        assertEquals(2, films.size(), "Размер списка не правильный");
    }
}