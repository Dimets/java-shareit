package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class BookingServerController {
    private final BookingService bookingService;

    public BookingServerController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingExtDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingDto bookingDto)
            throws EntityNotFoundException, BookingValidationException {
        log.info("POST /bookings userId={}", userId);
        log.info("POST /bookings userId={} bookingDto={}", userId, bookingDto);
        return bookingService.findById(userId, bookingService.create(userId, bookingDto).getId());
    }

    @PatchMapping("/{bookingId}")
    public BookingExtDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("bookingId") Long bookingId,
                                 @RequestParam(value = "approved") Boolean status)
            throws EntityNotFoundException, UsersDoNotMatchException, BookingValidationException {
        log.info("PATCH /bookings/{} userId={} approved={}", bookingId, userId, status);
        return bookingService.approve(bookingId, userId, status);
    }

    @GetMapping("/{bookingId}")
    public BookingExtDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("bookingId") Long bookingId)
            throws EntityNotFoundException {
        log.info("GET /bookings/{}", bookingId);
        return bookingService.findById(userId, bookingId);
    }

    @GetMapping
    public List<BookingExtDto> getAllByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam String state,
                                              @RequestParam Integer from,
                                              @RequestParam Integer size)
            throws EntityNotFoundException, UnsupportedStatusException {

        log.info("GET /bookings?from={}&size={} bookerId={} state={}", from, size, userId, state);
        return bookingService.findAllByBooker(userId, state, from, size);
    }


    @GetMapping("/owner")
    public List<BookingExtDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam String state,
                                             @RequestParam Integer from,
                                             @RequestParam Integer size)
            throws EntityNotFoundException, UnsupportedStatusException {
        log.info("GET /bookings/owner?from={}&size={} ownerId={} state={}", from, size, userId, state);
        return bookingService.findAllByOwner(userId, state, from, size);
    }
}
