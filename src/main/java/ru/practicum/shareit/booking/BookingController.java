package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtDto;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingDto bookingDto)
            throws EntityNotFoundException, BookingValidationException {
        log.info("POST /bookings userId={}", userId);
        log.debug("POST /bookings userId={} bookingDto={}", userId, bookingDto);
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingExtDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("bookingId") Long bookingId,
                                 @RequestParam(value = "approved") Boolean status)
            throws EntityNotFoundException, UsersDoNotMatchException, BookingValidationException {
        log.info("PATCH /bookings/{bookingId} userId={} approved={}", bookingId, userId, status);
        return bookingService.approve(bookingId, userId, status);
    }

    @GetMapping("/{bookingId}")
    public BookingExtDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("bookingId") Long bookingId)
            throws EntityNotFoundException {
        log.info("GET /bookings/{bookingId}", bookingId);
        return bookingService.findById(userId, bookingId);
    }

    @GetMapping
    public List<BookingExtDto> getAllByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(required = false, defaultValue = "ALL",
                                                      value = "state") String state)
            throws EntityNotFoundException, UnsupportedStatusException {
        log.info("GET /bookings bookerId={} state={}", userId, state);
        return bookingService.findAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingExtDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(required = false, defaultValue = "ALL",
                                                     value = "state") String state)
            throws EntityNotFoundException, UnsupportedStatusException {
        log.info("GET /bookings/owner ownerId={} state={}", userId, state);
        return bookingService.findAllByOwner(userId, state);
    }
}
