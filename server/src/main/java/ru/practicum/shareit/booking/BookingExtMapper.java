package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingExtMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;

    public BookingExtMapper(UserMapper userMapper, ItemMapper itemMapper, BookingMapper bookingMapper) {
        this.userMapper = userMapper;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
    }

    public BookingExtDto toBookingExtMapper(BookingDto bookingDto, UserDto userDto, ItemDto itemDto) {
        UserDto newUserDto = new UserDto();
        newUserDto.setId(userDto.getId());

        ItemDto newItemDto = new ItemDto();
        newItemDto.setId(itemDto.getId());
        newItemDto.setName(itemDto.getName());

        return new BookingExtDto(bookingDto.getId(), bookingDto.getStatus(), newUserDto, newItemDto,
                bookingDto.getStart(), bookingDto.getEnd());
    }

    public List<BookingExtDto> toBookingExtMapper(List<Booking> bookings) {
        List<BookingExtDto> bookingExtDtoList = new ArrayList<>();

        for (Booking booking : bookings) {
            UserDto userDto = userMapper.toUserDto(booking.getBooker());
            ItemDto itemDto = itemMapper.toItemDto(booking.getItem());
            BookingDto bookingDto = bookingMapper.toBookingDto(booking);
            bookingExtDtoList.add(toBookingExtMapper(bookingDto, userDto, itemDto));
        }

        return bookingExtDtoList;
    }
}
