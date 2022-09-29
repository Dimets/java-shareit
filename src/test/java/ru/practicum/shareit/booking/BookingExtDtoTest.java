package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingExtDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingExtDtoTest {
    @Autowired
    private JacksonTester<BookingExtDto> json;

    @Test
    void testBookingExtDtoTest() throws IOException {
        UserDto userDto = new UserDto(1L, "user first", "user@first.ru");

        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, 1L);

        BookingExtDto bookingExtDto = new BookingExtDto(1L, BookingStatus.WAITING, userDto, itemDto,
                LocalDateTime.MAX.minusDays(5), LocalDateTime.MAX.minusDays(3));

        JsonContent<BookingExtDto> result = json.write(bookingExtDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.status");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingExtDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo(bookingExtDto.getStatus().toString());
        assertThat(result).extractingJsonPathValue("$.booker.id")
                .isEqualTo(bookingExtDto.getBooker().getId().intValue());
        assertThat(result).extractingJsonPathValue("$.booker.name")
                .isEqualTo(bookingExtDto.getBooker().getName());
        assertThat(result).extractingJsonPathValue("$.booker.email")
                .isEqualTo(bookingExtDto.getBooker().getEmail());
        assertThat(result).extractingJsonPathValue("$.item.id")
                .isEqualTo(bookingExtDto.getItem().getId().intValue());
        assertThat(result).extractingJsonPathValue("$.item.name")
                .isEqualTo(bookingExtDto.getItem().getName());
        assertThat(result).extractingJsonPathValue("$.item.description")
                .isEqualTo(bookingExtDto.getItem().getDescription());
        assertThat(result).extractingJsonPathValue("$.item.available")
                .isEqualTo(bookingExtDto.getItem().getAvailable());
        assertThat(result).extractingJsonPathValue("$.item.owner")
                .isEqualTo(bookingExtDto.getItem().getOwner().intValue());
        assertThat(result).extractingJsonPathValue("$.item.requestId")
                .isEqualTo(bookingExtDto.getItem().getRequestId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingExtDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingExtDto.getEnd().toString());
    }
}
