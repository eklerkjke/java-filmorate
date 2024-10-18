package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User update(User newUser) {
        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь не найден");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь не найден");
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

    @Override
    public void delete(User user) {
        users.remove(user.getId());
        log.info("Пользователь удален");
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

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
