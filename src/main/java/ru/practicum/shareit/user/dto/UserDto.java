package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;


@AllArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String name;
    @NotNull(message = "Email обязателен")
    private String email;
}
