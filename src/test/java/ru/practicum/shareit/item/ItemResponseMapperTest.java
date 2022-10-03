package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoTest;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemResponseMapperTest {

    @Test
    void toItemResponseDto() {
        ItemDto itemDto =  new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, 1L);

        BookingUserDto lastBookingDto = new BookingUserDto(1L, 1L);

        BookingUserDto nextBookingDto = new BookingUserDto(2L, 2L);

        List<CommentDto> commentDtoList = List.of(new CommentDto(1L, "text", 1L, "name",
                LocalDateTime.MAX));

        ItemResponseDto itemResponseDto = new ItemResponseDto(itemDto.getId(), itemDto.getName(),
                itemDto.getDescription(), itemDto.getAvailable(), lastBookingDto, nextBookingDto, commentDtoList);

        ItemResponseDto result = ItemResponseMapper.toItemResponseDto(itemDto, lastBookingDto, nextBookingDto,
                commentDtoList);

        assertThat(result).isEqualTo(itemResponseDto);
    }
}
