package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingUserMapper {
    public static BookingUserDto toBookingUserDto(Booking booking, Long userId) {
        return new BookingUserDto(booking.getId(), userId);
    }
}
