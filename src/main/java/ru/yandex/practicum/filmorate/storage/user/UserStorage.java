package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User add(User user);

    User update(User user);

    Optional<User> getById(Long id);

    List<User> getList();

    void addFriend(Long targetUserId, Long friendUserId);

    void removeFriend(Long targetUserId, Long friendUserId);

    List<User> getListFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);
}
