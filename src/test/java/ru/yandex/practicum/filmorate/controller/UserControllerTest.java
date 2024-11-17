package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    private final UserController userController;
    private User user1;
    private User user2;

    @BeforeEach
    public void beforeEach() {
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
    }

    @Test
    public void addTest() {
        User newUser1 = userController.add(user1);
        userController.add(user2);

        User addedUser = userController.getById(newUser1.getId());

        assertEquals(newUser1.getId(), addedUser.getId(), "");
    }

    @Test
    public void updateTest() {
        User user = userController.add(user1);
        user.setLogin("test 123");
        User newUser = userController.update(user);

        assertEquals(user.getLogin(), newUser.getLogin(), "Поле не обновилось");
    }
}