package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingUserDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemResponseDto implements Comparable<ItemResponseDto> {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingUserDto lastBooking;

    private BookingUserDto nextBooking;

    private List<CommentDto> comments;

    @Override
    public int compareTo(ItemResponseDto o) {
        if (this.getId() < o.getId()) {
            return -1;
        }

        if (this.getId() > o.getId()) {
            return 1;
        }

        return 0;
    }
}
