package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> getList() {
        return userStorage.getList();
    }

    public User getById(Long id) {
        return userStorage.getById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден: " + id));
    }

    public void addUserFriend(Long targetUserId, Long friendUserId) {
        userStorage.addFriend(targetUserId, friendUserId);
    }

    public void removeUserFriend(Long targetUserId, Long friendUserId) {
        userStorage.removeFriend(targetUserId, friendUserId);
    }

    public List<User> getListFriends(Long userId) {
        return userStorage.getListFriends(userId);
    }

    public List<User> getListCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
