package ru.practicum.shareit.item.model;

import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

public class ItemBookingMapper {
    public static ItemBookingDto toItemBookingDto(ItemDto itemDto, BookingUserDto lastBookingDto,
                                                  BookingUserDto nextBookingDto) {

        return new ItemBookingDto(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), lastBookingDto, nextBookingDto);
    }

}
