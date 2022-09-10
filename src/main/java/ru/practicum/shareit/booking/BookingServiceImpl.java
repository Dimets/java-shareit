package ru.practicum.shareit.booking;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtDto;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Lazy
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    @Transactional
    public BookingDto create(Long userId, BookingDto bookingDto)
            throws EntityNotFoundException, BookingValidationException {

        ItemDto itemDto = itemService.findById(bookingDto.getItemId());
        UserDto userDto = userService.findById(userId);

        if (itemDto.getOwner().equals(userId)) {
            throw new EntityNotFoundException("Владелец вещи не может создавать бронирование");
        }

        validate(bookingDto);

        bookingDto.setBooker(userId);
        bookingDto.setStatus(BookingStatus.WAITING);

        return BookingMapper.toBookingDto(
                bookingRepository.save(BookingMapper.toBooking(bookingDto, userDto, itemDto)));
    }

    @Override
    @Transactional
    public BookingExtDto approve(Long bookingId, Long userId, Boolean status)
            throws EntityNotFoundException, BookingValidationException {
        BookingDto bookingDto = findById(bookingId);
        ItemDto itemDto = itemService.findById(bookingDto.getItemId());
        UserDto userDto = userService.findById(bookingDto.getBooker());

        if (!itemDto.getOwner().equals(userId)) {
            throw new EntityNotFoundException("Подтверждать/отклонять бронирование может только владелец вещи");
        }

        if (bookingDto.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BookingValidationException(String.format("Бронирование id=%d уже подтверждено", bookingId));
        }

        if (status.equals(true)) {
            bookingDto.setStatus(BookingStatus.APPROVED);
        } else if (status.equals(false)) {
            bookingDto.setStatus(BookingStatus.REJECTED);
        }

        bookingRepository.save(BookingMapper.toBooking(bookingDto, userDto, itemDto));

        return BookingExtMapper.toBookingExtMapper(bookingDto, userDto, itemDto);
    }

    @Override
    public BookingDto findById(Long bookingId) throws EntityNotFoundException {
        return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Бронирование с id=%d не найдено", bookingId))));
    }

    @Override
    public BookingExtDto findById(Long userId, Long bookingId)
            throws EntityNotFoundException {
        BookingDto bookingDto = findById(bookingId);
        ItemDto itemDto = itemService.findById(bookingDto.getItemId());
        UserDto userDto = userService.findById(bookingDto.getBooker());

        if (bookingDto.getBooker().equals(userId) || itemDto.getOwner().equals(userId)) {
            return BookingExtMapper.toBookingExtMapper(bookingDto, userDto, itemDto);
        } else {
            throw new EntityNotFoundException("Просматривать бронирование может только владелец вещи " +
                    "или создатель брони");
        }
    }

    @Override
    public List<BookingExtDto> findAllByBooker(Long userId, String state)
            throws EntityNotFoundException, UnsupportedStatusException {

        userService.findById(userId);

        List<BookingExtDto> bookingExtDtoList = new ArrayList<>();
        BookingStatus status;

        try {
            status = BookingStatus.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }

        switch (status) {
            case ALL :
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(
                        bookingRepository.findAllByBooker_Id(userId));
                break;
            case CURRENT:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(
                        bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                                LocalDateTime.now()));
                break;
            case PAST:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(
                        bookingRepository.findAllByBooker_IdAndEndIsBefore(userId, LocalDateTime.now()));
                break;
            case FUTURE:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(
                        bookingRepository.findAllByBooker_IdAndStartIsAfter(userId, LocalDateTime.now()));
                break;
            case WAITING:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(
                        bookingRepository.findAllByBooker_IdAndStatus(userId, BookingStatus.WAITING));
                break;
            case REJECTED:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(
                        bookingRepository.findAllByBooker_IdAndStatus(userId, BookingStatus.REJECTED));
                break;
        }

        Collections.sort(bookingExtDtoList);
        return bookingExtDtoList;
    }

    @Override
    public List<BookingExtDto> findAllByOwner(Long userId, String state)
            throws EntityNotFoundException, UnsupportedStatusException {
        userService.findById(userId);

        List<BookingExtDto> bookingExtDtoList = new ArrayList<>();
        BookingStatus status;

        try {
            status = BookingStatus.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }

        switch (status) {
            case ALL:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(bookingRepository
                        .findAllByItem_Owner_Id(userId));
                break;
            case CURRENT:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(bookingRepository
                        .findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                                LocalDateTime.now()));
                break;
            case PAST:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(bookingRepository
                        .findAllByItem_Owner_IdAndEndIsBefore(userId, LocalDateTime.now()));
                break;
            case FUTURE:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(bookingRepository
                        .findAllByItem_Owner_IdAndStartIsAfter(userId, LocalDateTime.now()));
                break;
            case WAITING:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(bookingRepository
                        .findAllByItem_Owner_IdAndStatus(userId, BookingStatus.WAITING));
                break;
            case REJECTED:
                bookingExtDtoList = BookingExtMapper.toBookingExtMapper(bookingRepository
                        .findAllByItem_Owner_IdAndStatus(userId, BookingStatus.REJECTED));
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: " + state);
        }

        Collections.sort(bookingExtDtoList);
        return bookingExtDtoList;
    }

    @Override
    public BookingUserDto findLastByItem(Long itemId) {
        if (bookingRepository.findLastBookingByItem_Id(itemId, LocalDateTime.now()).isPresent()) {
            Booking booking = bookingRepository.findLastBookingByItem_Id(itemId, LocalDateTime.now()).get();
            return BookingUserMapper.toBookingUserDto(booking, booking.getBooker().getId());
        }

        return null;
    }


    @Override
    public BookingUserDto findNextByItem(Long itemId) {
        if (bookingRepository.findNextBookingByItem_Id(itemId, LocalDateTime.now()).isPresent()) {
            Booking booking = bookingRepository.findNextBookingByItem_Id(itemId, LocalDateTime.now()).get();
            return BookingUserMapper.toBookingUserDto(booking, booking.getBooker().getId());
        }

        return null;
    }

    void validate(BookingDto bookingDto) throws EntityNotFoundException, BookingValidationException {
        if (itemService.findById(bookingDto.getItemId()).getAvailable().equals(false)) {
            throw new BookingValidationException(String.format("Вещь с id=%d недоступна для бронирования",
                    bookingDto.getItemId()));
        }

        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BookingValidationException("Дата окончания бронирования не может быть в прошлом");
        }

        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new BookingValidationException("Дата начала бронирования не может быть после даты окончания");
        }

        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingValidationException("Дата начала бронирования не может быть в прошлом");
        }
    }
}
