package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingExtDto implements Comparable<BookingExtDto> {
    private Long id;

    private BookingStatus status;

    private UserDto booker;

    private ItemDto item;

    private LocalDateTime start;

    private LocalDateTime end;

    @Override
    public int compareTo(BookingExtDto o) {
        if (this.getStart().isAfter(o.getStart())) {
            return -1;
        }

        if (this.getStart().isBefore(o.getStart())) {
            return 1;
        }

        return 0;
    }
}
