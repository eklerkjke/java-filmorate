package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User user1;
    private User user2;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        user1 = new User(
                "test",
                "test",
                "test@test",
                LocalDate.of(2001, 5, 10)
        );
        user2 = new User(
                "test",
                "",
                "test@test",
                LocalDate.of(1985, 4, 12)
        );
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