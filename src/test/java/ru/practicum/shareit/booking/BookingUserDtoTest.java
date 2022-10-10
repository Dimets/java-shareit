package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingUserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingUserDtoTest {
    @Autowired
    private JacksonTester<BookingUserDto> json;

    @Test
    void testBookingUserDto() throws IOException {
        BookingUserDto bookingUserDto = new BookingUserDto(1L, 1L);

        JsonContent<BookingUserDto> result = json.write(bookingUserDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.bookerId");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingUserDto.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(bookingUserDto.getBookerId().intValue());
    }
}
