package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestRequestDto {

        private Long id;

        @NotNull(message = "Текст запроса обязателен!")
        private String description;

        private LocalDateTime created;

        private Long requesterId;

        private List<ItemRequestDto> items;

}
