package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.as;
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
}
