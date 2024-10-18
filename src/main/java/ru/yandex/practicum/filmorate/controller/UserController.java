package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public User add(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping()
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }


    @GetMapping()
    public List<User> getList() {
        return userService.getList();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addUserFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeUserFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListFields(@PathVariable Long id) {
        return userService.getListFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListCommonFields(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getListCommonFriends(id, otherId);
    }

}
