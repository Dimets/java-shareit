package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBooker(User booker, Pageable pageable);

    Page<Booking> findAllByBookerAndStatus(User booker, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfter(User booker, LocalDateTime nowStart,
                                                                  LocalDateTime nowEnd, Pageable pageable);

    Page<Booking> findAllByBookerAndEndIsBefore(User booker, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByBookerAndStartIsAfter(User booker, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByItemOwner(User owner, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStatus(User owner, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStartIsBeforeAndEndIsAfter(User owner, LocalDateTime nowStart,
                                                                      LocalDateTime nowEnd, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndEndIsBefore(User owner, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStartIsAfter(User owner, LocalDateTime now, Pageable pageable);

    @Query(value = "select * from bookings where item_id = ?1 and end_dt < ?2 order by  end_dt desc limit 1",
            nativeQuery = true)
    Optional<Booking> findLastBookingByItem(Item item, LocalDateTime now);

    @Query(value = "select * from bookings where item_id = ?1 and start_dt > ?2 order by  start_dt asc limit 1",
            nativeQuery = true)
    Optional<Booking> findNextBookingByItem(Item item, LocalDateTime now);

    Boolean existsByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime nowStart,
                                                          LocalDateTime nowEnd);

    Boolean existsByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime nowStart);
}
