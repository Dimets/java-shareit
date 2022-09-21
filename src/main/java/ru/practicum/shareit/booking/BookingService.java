package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtDto;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingDto bookingDto) throws EntityNotFoundException, BookingValidationException;

    BookingDto findById(Long bookingId) throws EntityNotFoundException;

    BookingExtDto findById(Long userId, Long bookingId) throws EntityNotFoundException;

    List<BookingExtDto> findAllByBooker(Long userId, String state, Integer from, Integer size)
            throws EntityNotFoundException, UnsupportedStatusException;

    List<BookingExtDto> findAllByOwner(Long userId, String state, Integer from, Integer size)
            throws EntityNotFoundException, UnsupportedStatusException;

    BookingExtDto approve(Long bookingId, Long userId, Boolean status)
            throws EntityNotFoundException, UsersDoNotMatchException, BookingValidationException;

    BookingUserDto findLastByItem(Long itemId) throws EntityNotFoundException;

    BookingUserDto findNextByItem(Long itemId) throws EntityNotFoundException;
}
