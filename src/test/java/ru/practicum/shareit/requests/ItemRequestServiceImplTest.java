package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {

    @Autowired
    private final ItemRequestService itemRequestService;

    @Autowired
    private final UserService userService;

    UserDto userDto;
    ItemRequestDto itemRequestDto;


    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create() throws Exception {
        userDto = userService.create(new UserDto(null, "user name", "user@email"));

        itemRequestDto = new ItemRequestDto(null, "item request description",
                LocalDateTime.now(),
                userDto.getId(), Collections.emptyList());

        ItemRequestDto result = itemRequestService.create(userDto, itemRequestDto);

        assertThat(result.getDescription()).isNotNull();
        assertThat(result.getDescription()).isEqualTo(itemRequestDto.getDescription());
        assertThat(result.getCreated()).isEqualTo(itemRequestDto.getCreated());
        assertThat(result.getRequesterId()).isEqualTo(itemRequestDto.getRequesterId());
    }


    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Transactional
    void findAll() throws Exception {
        userDto = userService.create(new UserDto(null, "user name", "user@email"));

        itemRequestDto = new ItemRequestDto(null, "item request description",
                LocalDateTime.of(2022, 9, 20, 22, 00, 55),
                userDto.getId(), Collections.emptyList());

        ItemRequestDto resultItemRequestDto = itemRequestService.create(userDto, itemRequestDto);

        List<ItemRequestDto> result = itemRequestService.findAll(userDto.getId());

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(resultItemRequestDto.getId());
        assertThat(result.get(0).getDescription()).isEqualTo(resultItemRequestDto.getDescription());
        assertThat(result.get(0).getRequesterId()).isEqualTo(resultItemRequestDto.getRequesterId());
        assertThat(result.get(0).getCreated()).isEqualTo(resultItemRequestDto.getCreated());
        assertThat(result.get(0).getItems()).hasSize(0);
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Transactional
    void findAllOther() throws Exception {
        userDto = userService.create(new UserDto(null, "user name", "user@email"));

        itemRequestDto = new ItemRequestDto(null, "item request description",
                LocalDateTime.of(2022, 9, 20, 22, 00, 55),
                userDto.getId(), Collections.emptyList());

        ItemRequestDto resultItemRequestDto = itemRequestService.create(userDto, itemRequestDto);

        UserDto otherUserDto = userService.create(new UserDto(null, "other user name",
                "other_user@email"));

        List<ItemRequestDto> result = itemRequestService.findAllOther(otherUserDto.getId(), 0, 1);

        assertThat(result).hasSize(1);
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWithEntityNotFoundException() {
        final EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> itemRequestService.findById(-1L));
        assertThat(exception.getMessage()).isEqualTo("Запрос с id=-1 не найден");
    }
}
