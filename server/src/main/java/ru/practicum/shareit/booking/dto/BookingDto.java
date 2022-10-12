package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private Long id;

    @NotNull(message = "Начало периода бронирования указывать обязательно!")
    private LocalDateTime start;

    @NotNull(message = "Конец периода бронирования указывать обязательно!")
    private LocalDateTime end;

    @NotNull(message = "Id вещи указывать обязательно!")
    private Long itemId;

    private Long booker;

    private BookingStatus status;
}
