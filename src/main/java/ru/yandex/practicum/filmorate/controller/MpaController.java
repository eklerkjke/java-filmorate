package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService ratingService;

    @GetMapping()
    public List<Mpa> getList() {
        return ratingService.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable @Positive long id) {
        return ratingService.getRatingById(id);
    }
}
