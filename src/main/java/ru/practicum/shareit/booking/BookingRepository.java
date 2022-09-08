package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_Id(Long bookerId);

    List<Booking> findAllByBooker_IdAndStatus(Long booker_id, BookingStatus status);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(Long booker_id, LocalDateTime nowStart,
                                                                  LocalDateTime nowEnd);

    List<Booking> findAllByBooker_IdAndEndIsBefore(Long booker_id, LocalDateTime now);

    List<Booking> findAllByBooker_IdAndStartIsAfter(Long booker_id, LocalDateTime now);

    List<Booking> findAllByItem_Owner_Id(Long ownerId);

    List<Booking> findAllByItem_Owner_IdAndStatus(Long ownerId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime nowStart,
                                                                      LocalDateTime nowEnd);

    List<Booking> findAllByItem_Owner_IdAndEndIsBefore(Long ownerId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndStartIsAfter(Long ownerId, LocalDateTime now);

    @Query(value = "select * from bookings where item_id = ?1 and end_dt < ?2 order by  end_dt desc limit 1",
            nativeQuery = true)
    Optional<Booking> findLastBookingByItem_Id(Long itemId, LocalDateTime now);

    @Query(value = "select * from bookings where item_id = ?1 and start_dt > ?2 order by  start_dt asc limit 1",
            nativeQuery = true)
    Optional<Booking> findNextBookingByItem_Id(Long itemId, LocalDateTime now);
}
