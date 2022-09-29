package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemResponseDtoTest {
    @Autowired
    private JacksonTester<ItemResponseDto> json;

    @Test
    void testItemResponseDto() throws IOException {
        BookingUserDto lastBooking = new BookingUserDto(1L, 1L);
        BookingUserDto nextBooking = new BookingUserDto(1L, 1L);

        CommentDto commentDto = new CommentDto(1L, "comment text", 1L, "author name",
                LocalDateTime.MAX);

        ItemResponseDto itemResponseDto = new ItemResponseDto(1L, "item name", "item description",
                true, lastBooking, nextBooking, List.of(commentDto));

        JsonContent<ItemResponseDto> result = json.write(itemResponseDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.comments");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemResponseDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemResponseDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemResponseDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        System.out.println(lastBooking);
        System.out.println(result.getJson());
        assertThat(result).extractingJsonPathValue("$.lastBooking.id")
                .isEqualTo(lastBooking.getId().intValue());
        assertThat(result).extractingJsonPathValue("$.nextBooking.bookerId")
                .isEqualTo(nextBooking.getBookerId().intValue());
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(1);
    }

}
