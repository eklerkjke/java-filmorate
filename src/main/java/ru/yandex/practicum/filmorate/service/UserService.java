package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void delete(User user) {
        userStorage.delete(user);
    }

    public List<User> getList() {
        return new ArrayList<>(userStorage.getList());
    }

    public User getById(Long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException("Юзер не найден: " + id);
        }

        return user;
    }

    public void addUserFriend(Long targetUserId, Long friendUserId) {
        User fried = getById(friendUserId);
        User targetUser = getById(targetUserId);

        targetUser.getFriends().add(friendUserId);
        fried.getFriends().add(targetUserId);
    }

    public void removeUserFriend(Long targetUserId, Long friendUserId) {
        User fried = getById(friendUserId);
        User targetUser = getById(targetUserId);

        targetUser.getFriends().remove(friendUserId);
        fried.getFriends().remove(targetUserId);
    }

    public List<User> getListFriends(Long userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        return user.getFriends()
                .stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public List<User> getListCommonFriends(Long id, Long otherId) {
        HashSet<Long> intersect = new HashSet<>(getById(id).getFriends());
        intersect.retainAll(getById(otherId).getFriends());
        return intersect
                .stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }
}
