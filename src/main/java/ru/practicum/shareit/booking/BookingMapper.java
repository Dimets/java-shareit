package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public BookingMapper(ItemMapper itemMapper, UserMapper userMapper) {
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
    }

    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem().getId(),
                booking.getBooker().getId(), booking.getStatus());
    }

    public List<BookingDto> toBookingDto(List<Booking> bookings) {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDtoList.add(toBookingDto(booking));
        }
        return bookingDtoList;
    }

    public Booking toBooking(BookingDto bookingDto, UserDto userDto, ItemDto itemDto) {
        Booking booking = new Booking();

        if (bookingDto.getId() != null) {
            booking.setId(bookingDto.getId());
        }

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(itemMapper.toItem(itemDto, userDto));
        booking.setBooker(userMapper.toUser(userDto));
        booking.setStatus(bookingDto.getStatus());

        return booking;
    }
}
