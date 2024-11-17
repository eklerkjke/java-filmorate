package ru.yandex.practicum.filmorate.controller;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User user1;
    private User user2;

    @BeforeEach
    public void beforeEach() {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(new JdbcDataSource());

        UserService userService = new UserService(new UserRepository(
                template
        ));
        userController = new UserController(userService);
        user1 = User.builder()
                .email("test@test")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2001, 5, 10))
                .build();
        user2 = User.builder()
                .email("test@test")
                .login("gef")
                .name("ssds")
                .birthday(LocalDate.of(1985, 5, 10))
                .build();
    }

    @Test
    public void getListTest() {
        userController.add(user1);
        userController.add(user2);

        List<User> users = userController.getList();
        assertEquals(2, users.size(), "пользователь не добавился");
        assertTrue(users.contains(user1), "Список пользователей не обновился");
    }

    @Test
    public void addTest() {
        User newUser1 = userController.add(user1);
        userController.add(user2);

        assertTrue(userController.getList().contains(newUser1), "");
    }

    @Test
    public void updateTest() {
        User user = userController.add(user1);
        user.setLogin("test 123");
        User newUser = userController.update(user);

        assertEquals(user.getLogin(), newUser.getLogin(), "Поле не обновилось");
        assertEquals(1, userController.getList().size(), "Список изменился");
    }
}