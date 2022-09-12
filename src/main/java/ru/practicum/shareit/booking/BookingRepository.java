package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker(User booker);

    List<Booking> findAllByBookerAndStatus(User booker, BookingStatus status);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfter(User booker, LocalDateTime nowStart,
                                                                  LocalDateTime nowEnd);

    List<Booking> findAllByBookerAndEndIsBefore(User booker, LocalDateTime now);

    List<Booking> findAllByBookerAndStartIsAfter(User booker, LocalDateTime now);

    List<Booking> findAllByItemOwner(User owner);

    List<Booking> findAllByItemOwnerAndStatus(User owner, BookingStatus status);

    List<Booking> findAllByItemOwnerAndStartIsBeforeAndEndIsAfter(User owner, LocalDateTime nowStart,
                                                                      LocalDateTime nowEnd);

    List<Booking> findAllByItemOwnerAndEndIsBefore(User owner, LocalDateTime now);

    List<Booking> findAllByItemOwnerAndStartIsAfter(User owner, LocalDateTime now);

    @Query(value = "select * from bookings where item_id = ?1 and end_dt < ?2 order by  end_dt desc limit 1",
            nativeQuery = true)
    Optional<Booking> findLastBookingByItem(Item item, LocalDateTime now);

    @Query(value = "select * from bookings where item_id = ?1 and start_dt > ?2 order by  start_dt asc limit 1",
            nativeQuery = true)
    Optional<Booking> findNextBookingByItem(Item item, LocalDateTime now);
}
