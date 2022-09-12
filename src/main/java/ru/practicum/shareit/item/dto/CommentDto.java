package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;

    @NotNull(message = "Текст комментария обязателен!")
    @NotBlank(message = "Текст комментария не может быть пустым!")
    private String text;

    private Long itemId;

    private String authorName;

    private LocalDateTime create;
}
