package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public class ItemResponseMapper {
    public static ItemResponseDto toItemResponseDto(ItemDto itemDto, BookingUserDto lastBookingDto,
                                                    BookingUserDto nextBookingDto, List<CommentDto> commentDtoList) {

        return new ItemResponseDto(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), lastBookingDto, nextBookingDto, commentDtoList);
    }



}
