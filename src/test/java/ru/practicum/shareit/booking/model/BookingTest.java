package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingTest {
    @Test
    void equals() {
        Booking booking = new Booking(LocalDateTime.now().plusHours(3), LocalDateTime.MAX, null, null,
                BookingStatus.WAITING);

        Booking otherBooking = new Booking(LocalDateTime.now().plusHours(10), LocalDateTime.MAX, null, null,
                BookingStatus.WAITING);

        assertThat(booking.equals(otherBooking)).isEqualTo(false);
    }

}
