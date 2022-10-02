package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtDto;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {

    @Autowired
    private final BookingService bookingService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final ItemService itemService;

    BookingDto bookingDto;

    ItemDto itemDto;

    UserDto ownerUserDto;

    UserDto bookerUserDto;

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create() throws Exception {
        ownerUserDto = userService.create(new UserDto(null, "owner user name", "owner_user@email"));

        bookerUserDto = userService.create(new UserDto(null, "booker user name", "booker_user@email"));

        itemDto = itemService.create(ownerUserDto.getId(), new ItemDto(null, "item name",
                "item description", true, ownerUserDto.getId(), null));

        bookingDto =  new BookingDto(null, LocalDateTime.now().plusHours(3),
                LocalDateTime.now().plusDays(5), itemDto.getId(), bookerUserDto.getId(), BookingStatus.WAITING);

        BookingDto result = bookingService.create(bookerUserDto.getId(), bookingDto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStart()).isEqualTo(bookingDto.getStart());
        assertThat(result.getEnd()).isEqualTo(bookingDto.getEnd());
        assertThat(result.getItemId()).isEqualTo(itemDto.getId());
        assertThat(result.getBooker()).isEqualTo(bookerUserDto.getId());
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void approve() throws Exception {
        ownerUserDto = userService.create(new UserDto(null, "owner user name", "owner_user@email"));

        bookerUserDto = userService.create(new UserDto(null, "booker user name", "booker_user@email"));

        itemDto = itemService.create(ownerUserDto.getId(), new ItemDto(null, "item name",
                "item description", true, ownerUserDto.getId(), null));

        bookingDto =  bookingService.create(bookerUserDto.getId(), new BookingDto(null,
                LocalDateTime.now().plusHours(3),
                LocalDateTime.now().plusDays(5), itemDto.getId(), bookerUserDto.getId(), BookingStatus.WAITING));

        BookingExtDto result = bookingService.approve(bookingDto.getId(), ownerUserDto.getId(), true);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.APPROVED);

        final BookingValidationException exception = Assertions.assertThrows(
                BookingValidationException.class,
                () -> bookingService.approve(bookingDto.getId(), ownerUserDto.getId(), true));
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById() throws Exception {
        ownerUserDto = userService.create(new UserDto(null, "owner user name", "owner_user@email"));

        bookerUserDto = userService.create(new UserDto(null, "booker user name", "booker_user@email"));

        itemDto = itemService.create(ownerUserDto.getId(), new ItemDto(null, "item name",
                "item description", true, ownerUserDto.getId(), null));

        bookingDto =  bookingService.create(bookerUserDto.getId(), new BookingDto(null,
                LocalDateTime.of(2050, 1, 1, 10, 05, 06),
                LocalDateTime.of(2050, 1, 10, 12, 34, 39),
                itemDto.getId(), bookerUserDto.getId(), BookingStatus.WAITING));

        BookingDto result = bookingService.findById(bookingDto.getId());

        assertThat(result.getId()).isEqualTo(bookingDto.getId());
        assertThat(result.getStart()).isEqualTo(bookingDto.getStart());
        assertThat(result.getEnd()).isEqualTo(bookingDto.getEnd());
        assertThat(result.getItemId()).isEqualTo(itemDto.getId());
        assertThat(result.getBooker()).isEqualTo(bookerUserDto.getId());
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void findAllByBookerWithUnsupportedStatusException() {
        final UnsupportedStatusException exception = Assertions.assertThrows(
                UnsupportedStatusException.class,
                () -> bookingService.findAllByBooker(1L, "Unknown", 1, 1));
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByOwner() throws Exception {
        ownerUserDto = userService.create(new UserDto(null, "owner user name", "owner_user@email"));

        bookerUserDto = userService.create(new UserDto(null, "booker user name", "booker_user@email"));

        itemDto = itemService.create(ownerUserDto.getId(), new ItemDto(null, "item name",
                "item description", true, ownerUserDto.getId(), null));

        bookingDto =  bookingService.create(bookerUserDto.getId(), new BookingDto(null,
                LocalDateTime.of(2050, 1, 1, 10, 05, 06),
                LocalDateTime.of(2050, 1, 10, 12, 34, 39),
                itemDto.getId(), bookerUserDto.getId(), BookingStatus.WAITING));

        List<BookingExtDto> result = bookingService.findAllByOwner(ownerUserDto.getId(), "ALL",
                0, 1);
        assertThat(result).hasSize(1);

        result = bookingService.findAllByOwner(ownerUserDto.getId(), "CURRENT",0, 1);
        assertThat(result).hasSize(0);

        result = bookingService.findAllByOwner(ownerUserDto.getId(), "PAST",0, 1);
        assertThat(result).hasSize(0);

        result = bookingService.findAllByOwner(ownerUserDto.getId(), "FUTURE",0, 1);
        assertThat(result).hasSize(1);

        result = bookingService.findAllByOwner(ownerUserDto.getId(), "WAITING",0, 1);
        assertThat(result).hasSize(1);

        result = bookingService.findAllByOwner(ownerUserDto.getId(), "REJECTED",0, 1);
        assertThat(result).hasSize(0);
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findNextByItem() throws Exception {
        ownerUserDto = userService.create(new UserDto(null, "owner user name", "owner_user@email"));

        bookerUserDto = userService.create(new UserDto(null, "booker user name", "booker_user@email"));

        itemDto = itemService.create(ownerUserDto.getId(), new ItemDto(null, "item name",
                "item description", true, ownerUserDto.getId(), null));

        bookingDto =  bookingService.create(bookerUserDto.getId(), new BookingDto(null,
                LocalDateTime.of(2050, 1, 1, 10, 05, 06),
                LocalDateTime.of(2050, 1, 10, 12, 34, 39),
                itemDto.getId(), bookerUserDto.getId(), BookingStatus.WAITING));

        BookingUserDto result = bookingService.findNextByItem(itemDto.getId());

        assertThat(result.getId()).isEqualTo(bookingDto.getId());
        assertThat(result.getBookerId()).isEqualTo(bookingDto.getBooker());
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdExt() throws Exception {
        ownerUserDto = userService.create(new UserDto(null, "owner user name", "owner_user@email"));

        bookerUserDto = userService.create(new UserDto(null, "booker user name", "booker_user@email"));

        itemDto = itemService.create(ownerUserDto.getId(), new ItemDto(null, "item name",
                "item description", true, ownerUserDto.getId(), null));

        bookingDto =  bookingService.create(bookerUserDto.getId(), new BookingDto(null,
                LocalDateTime.of(2050, 1, 1, 10, 05, 06),
                LocalDateTime.of(2050, 1, 10, 12, 34, 39),
                itemDto.getId(), bookerUserDto.getId(), BookingStatus.WAITING));

        BookingExtDto result = bookingService.findById(bookingDto.getId(), bookingDto.getId());

        assertThat(result.getId()).isEqualTo(bookingDto.getId());
        assertThat(result.getBooker().getId()).isEqualTo(bookingDto.getBooker());
    }
}
