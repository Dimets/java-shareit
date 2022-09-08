package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingUserDto;

@Data
@AllArgsConstructor
public class ItemBookingDto implements Comparable<ItemBookingDto> {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingUserDto lastBooking;

    private BookingUserDto nextBooking;

    @Override
    public int compareTo(ItemBookingDto o) {
        if (this.getId() < o.getId()) {
            return -1;
        }

        if (this.getId() > o.getId()) {
            return 1;
        }

        return 0;
    }
}
