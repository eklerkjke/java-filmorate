package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    @Email
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    public Set<Long> friends = new HashSet<>();

    public User(String login, String name, String email, LocalDate birthday) {
        this.email = email;
        if (login.contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        this.login = login;
        this.name = (name == null || name.isEmpty()) ? login : name;
        this.birthday = birthday;
    }
}
