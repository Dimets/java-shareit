package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.CommentValidationException;
import ru.practicum.shareit.exception.EmailFormatException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    @Autowired
    private final ItemService itemService;

    @Autowired
    private final UserService userService;

    CommentDto commentDto;

    UserDto userDto;

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create() throws EmailFormatException {
        userDto = new UserDto(1L, "name", "name@email");

        userService.create(userDto);

        commentDto = new CommentDto(1L, "text", 1L, "author", LocalDateTime.now());

        final CommentValidationException exception = Assertions.assertThrows(
                CommentValidationException.class,
                () -> itemService.create(1L, 1L, commentDto));


    }
}
