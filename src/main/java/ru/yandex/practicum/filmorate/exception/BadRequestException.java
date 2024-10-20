package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
        log.error("Системная ошибка {}: {}", HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
