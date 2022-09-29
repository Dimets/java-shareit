package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDto() throws IOException {
        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, 1L);

        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "request description", LocalDateTime.MAX,
                1L, List.of(itemDto));

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.requesterId");
        assertThat(result).hasJsonPath("$.items");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestDto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestDto.getCreated().toString());
        assertThat(result).extractingJsonPathNumberValue("$.requesterId").isEqualTo(itemRequestDto
                .getRequesterId().intValue());
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
    }
}
