package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.CommentValidationException;
import ru.practicum.shareit.exception.EmailFormatException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoTest;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    @Autowired
    private final ItemService itemService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final ItemRequestService itemRequestService;

    @Autowired
    private final BookingService bookingService;

    CommentDto commentDto;

    UserDto userDto;

    ItemRequestMapper itemRequestMapper = new ItemRequestMapper(new UserMapper());

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createWithEmailFormatException() throws EmailFormatException {
        userDto = new UserDto(1L, "name", "name@email");

        userService.create(userDto);

        commentDto = new CommentDto(1L, "text", 1L, "author", LocalDateTime.now());

        final CommentValidationException exception = Assertions.assertThrows(
                CommentValidationException.class,
                () -> itemService.create(1L, 1L, commentDto));
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateWithUsersDoNotMatchException() throws EmailFormatException, EntityNotFoundException {
        userDto = userService.create(new UserDto(1L, "name", "name@email"));

        UserDto wrongUserDto = userService.create(new UserDto(5L, "name", "wrongname@email"));


        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, null);

        itemService.create(userDto.getId(), itemDto);

        final UsersDoNotMatchException exception = Assertions.assertThrows(
                UsersDoNotMatchException.class,
                () -> itemService.update(wrongUserDto.getId(), itemDto));


    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create() throws EmailFormatException, EntityNotFoundException {
        userDto = userService.create(new UserDto(1L, "name", "name@email"));

        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, null);

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto, new ItemRequestDto(1L,
                "request desc",LocalDateTime.MAX, userDto.getId(), null));

        itemDto.setRequestId(itemRequestDto.getId());

        ItemDto result = itemService.create(userDto.getId(), itemDto);

        assertThat(result).isEqualTo(itemDto);
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById() throws EntityNotFoundException, EmailFormatException {
        userDto = userService.create(new UserDto(1L, "name", "name@email"));

        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, null);

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto, new ItemRequestDto(1L,
                "request desc",LocalDateTime.MAX, userDto.getId(), null));

        itemDto.setRequestId(itemRequestDto.getId());

        itemService.create(userDto.getId(), itemDto);


        ItemDto result = itemService.findById(itemDto.getId());

        assertThat(result).isEqualTo(itemDto);
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWithEntityNotFoundException() {
        final EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> itemService.findById(-1L));
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUser() throws EntityNotFoundException, EmailFormatException {
        userDto = userService.create(new UserDto(1L, "name", "name@email"));

        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, null);

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto, new ItemRequestDto(1L,
                "request desc",LocalDateTime.MAX, userDto.getId(), null));

        itemDto.setRequestId(itemRequestDto.getId());

        itemService.create(userDto.getId(), itemDto);

        List<ItemResponseDto> result = itemService.findByUser(userDto.getId(), 0, 1);

        assertThat(result).hasSize(1);
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByCriteria() throws EntityNotFoundException, EmailFormatException {
        userDto = userService.create(new UserDto(1L, "name", "name@email"));

        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, null);

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto, new ItemRequestDto(1L,
                "request desc",LocalDateTime.MAX, userDto.getId(), null));

        itemDto.setRequestId(itemRequestDto.getId());

        itemService.create(userDto.getId(), itemDto);

        List<ItemDto> result =  itemService.findByCriteria("first", 0, 1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(itemDto);

        result =  itemService.findByCriteria("", 0, 1);
        assertThat(result).hasSize(0);
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdExt() throws Exception {
        userDto = userService.create(new UserDto(1L, "name", "name@email"));

        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, null);

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto, new ItemRequestDto(1L,
                "request desc",LocalDateTime.MAX, userDto.getId(), null));

        itemDto.setRequestId(itemRequestDto.getId());

        itemService.create(userDto.getId(), itemDto);

        UserDto otherUserDto = userService.create(new UserDto(2L, "other name", "other_name@email"));

        ItemResponseDto result = itemService.findById(userDto.getId(), itemDto.getId());

        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(result.getId()).isEqualTo(itemDto.getId());

        result = itemService.findById(otherUserDto.getId(), itemDto.getId());
        assertThat(result.getLastBooking()).isNull();
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update() throws Exception {
        userDto = userService.create(new UserDto(1L, "name", "name@email"));

        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, null);

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto, new ItemRequestDto(1L,
                "request desc",LocalDateTime.MAX, userDto.getId(), null));

        itemService.create(userDto.getId(), itemDto);

        itemDto.setDescription("updated");

        ItemDto result = itemService.update(userDto.getId(), itemDto);

        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());

        assertThat(result).isEqualTo(itemDto);
    }


    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByRequest() throws Exception {
        userDto = userService.create(new UserDto(1L, "name", "name@email"));

        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, null);

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto, new ItemRequestDto(1L,
                "request desc",LocalDateTime.MAX, userDto.getId(), null));

        itemDto.setRequestId(itemRequestDto.getId());

        itemService.create(userDto.getId(), itemDto);

        List<ItemDto> result = itemService.findByRequest(itemRequestMapper.toItemRequest(itemRequestDto, userDto));

        assertThat(result).hasSize(1);
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createWithComment() throws Exception {
        userDto = userService.create(new UserDto(1L, "name", "name@email"));

        ItemDto itemDto = itemService.create(userDto.getId(), new ItemDto(1L, "first item  name",
                "first item description", Boolean.TRUE, 1L, null));

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto, new ItemRequestDto(1L,
                "request desc",LocalDateTime.MAX, userDto.getId(), null));

        itemDto.setRequestId(itemRequestDto.getId());

        commentDto = new CommentDto(1L, "comment text", itemDto.getId(), "name", LocalDateTime.now());

        UserDto bookerDto = userService.create(new UserDto(2L, "booker name", "booker_name@email"));

        BookingDto bookingDto = bookingService.create(bookerDto.getId(), new BookingDto(1L,
                LocalDateTime.now().plusNanos(500000000), LocalDateTime.now().plusDays(1), itemDto.getId(), bookerDto.getId(),
                BookingStatus.APPROVED));

        TimeUnit.SECONDS.sleep(2);


        CommentDto result = itemService.create(bookerDto.getId(), itemDto.getId(), commentDto);

        assertThat(result.getText()).isEqualTo(commentDto.getText());
        assertThat(result.getItemId()).isEqualTo(itemDto.getId());
        assertThat(result.getAuthorName()).isEqualTo(bookerDto.getName());
    }
}
