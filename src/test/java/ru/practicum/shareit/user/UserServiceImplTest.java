package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.EmailFormatException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {

    @Autowired
    private final UserService userService;

    UserDto userDto;

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testInvalidUserEmail() {
        userDto = new UserDto(1L, "name", "email");

        final EmailFormatException exception = Assertions.assertThrows(
                EmailFormatException.class,
                () -> userService.create(userDto));
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testNotFoundUser() {

        final EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(-99L));
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create() throws Exception {
        userDto = userService.create(new UserDto(1L, "name", "user@email"));

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getEmail()).isEqualTo("user@email");
        assertThat(userDto.getName()).isEqualTo("name");

    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById() throws EmailFormatException {
        userDto = userService.create(new UserDto(1L, "name", "user@email"));

        userService.deleteById(userDto.getId());

        final EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(userDto.getId()));

    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update() throws Exception {
        userDto = userService.create(new UserDto(1L, "name", "user@email"));

        userDto.setName("updated name");

        UserDto result = userService.update(userDto);

        assertThat(result.getName()).isEqualTo("updated name");
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
    }
}
