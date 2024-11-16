package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        if (user.getId() > 0 && users.containsKey(user.getId())) {
            throw new BadRequestException("Нельзя доабвить пользователя с таким ID: " + user.getId());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User update(User newUser) {
        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
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
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> getList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addFriend(Long targetUserId, Long friendUserId) {

    }

    @Override
    public void removeFriend(Long targetUserId, Long friendUserId) {

    }

    @Override
    public List<User> getListFriends(Long id) {
        return List.of();
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return List.of();
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
