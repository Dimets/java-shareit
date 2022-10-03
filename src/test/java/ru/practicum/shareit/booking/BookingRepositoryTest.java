package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user = new User("user first", "user@first.ru");

    private Item item = new Item("fiRst item name", "First item description", true,
            user, null);

    private Booking booking = new Booking(LocalDateTime.MAX.minusDays(5), LocalDateTime.MAX.minusDays(1), item, user,
            BookingStatus.WAITING);

    @BeforeEach
    void setUp() {
        entityManager.persist(user);
        entityManager.persist(item);
        entityManager.persist(booking);
    }


    @Test
    void findLastBookingByItem() {
        Booking lastBooking = bookingRepository.findLastBookingByItem(item, LocalDateTime.MAX).get();

        Assertions.assertEquals(booking, lastBooking);
    }

    @Test
    void findNextBookingByItem() {
        Booking nextBooking = bookingRepository.findNextBookingByItem(item, LocalDateTime.MAX.minusDays(10)).get();

        Assertions.assertEquals(booking, nextBooking);
    }
}
