package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestDto {
    private Long id;

    @NotNull(message = "Название вещи обязательно!")
    @NotBlank(message = "Название вещи не может быть пустым!")
    private String name;

    @NotNull(message = "Описание вещи обязательно!")
    private String description;

    @NotNull
    private Boolean available;

    private Long owner;

    private Long requestId;
}