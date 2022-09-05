package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotNull(message = "Название вещи обязательно!")
    @NotBlank(message = "Название вещи не может быть пустым!")
    private String name;
    @NotNull(message = "Описание вещи обязательно!")
    private String description;
    @NotNull
    private Boolean available;
}
