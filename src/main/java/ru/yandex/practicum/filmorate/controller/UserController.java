package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping()
    public User add(@RequestBody User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User newUser) {
        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь не найден");
            throw new RuntimeException("Пользователь не найден");
        }

        User user = users.get(newUser.getId());
        if (newUser.getName() != null) {
            log.debug("Смена имени пользователя");
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            log.debug("Смена email пользователя");
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getLogin() != null) {
            log.debug("Смена логина пользователя");
            user.setLogin(newUser.getLogin());
        }
        if (newUser.getBirthday() != null) {
            log.debug("Смена даты рождения пользователя");
            user.setBirthday(newUser.getBirthday());
        }
        return user;
    }

    @GetMapping()
    public List<User> getList() {
        return new ArrayList<>(users.values());
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
