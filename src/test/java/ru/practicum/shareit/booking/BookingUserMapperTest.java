package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingUserMapperTest {
    UserMapper userMapper = new UserMapper();

    @Test
    void toBookingUserDto() {
        User booker = new User("user name", "user@email");
        booker.setId(1L);

        User owner = new User("owner", "owner@email");

        Booking booking = new Booking(LocalDateTime.MAX.minusDays(10), LocalDateTime.MAX.minusDays(5),
                new Item("item name", "item desc", true,
                        owner, null), booker, BookingStatus.WAITING);
        booking.setId(1L);

        BookingUserDto result = BookingUserMapper.toBookingUserDto(booking, booker.getId());

        assertThat(result).hasOnlyFields("id", "bookerId");
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBookerId()).isEqualTo(1L);
    }
}
