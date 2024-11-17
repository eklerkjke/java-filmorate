package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {
    private final FilmController filmController;
    private final MpaController mpaController;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void setUp() {
        film1 = Film.builder()
                .name("name 1")
                .description("description 1")
                .releaseDate(LocalDate.of(2000, 2, 4))
                .duration(90)
                .mpa(
                        Mpa.builder()
                                .id(1)
                                .name("test")
                                .build()
                )
                .build();
        film2 = Film.builder()
                .name("name 2")
                .description("description 2")
                .releaseDate(LocalDate.of(2222, 2, 4))
                .duration(60)
                .mpa(
                        Mpa.builder()
                                .id(1)
                                .name("test")
                                .build()
                )
                .build();
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