package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User (String login, String name, String email, LocalDate birthday) {
        if (!email.contains("@")) {
            throw new ValidationException("Почта должна содержать \"@\"");
        }

        this.email = email;

        if (login.contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }

        this.login = login;
        this.name = (name.isEmpty()) ? login : name;

        if (birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        this.birthday = birthday;
    }
}
