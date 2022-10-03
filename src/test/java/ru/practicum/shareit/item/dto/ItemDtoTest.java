package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class ItemDtoTest {
    @Autowired
    private JacksonTester<ru.practicum.shareit.item.dto.ItemDto> json;

    @Test
    void testItemDto() throws IOException {
        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, 1L);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.owner");
        assertThat(result).hasJsonPath("$.requestId");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.owner").isEqualTo(itemDto.getOwner().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(itemDto.getRequestId().intValue());

    }

    @Test
    void testCompareTo() {
        ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
                Boolean.TRUE, 1L, 1L);

        ItemDto otherItemDto = new ItemDto(2L, "second item  name", "second item description",
                Boolean.TRUE, 1L, 1L);

        assertThat(itemDto.compareTo(otherItemDto)).isEqualTo(-1);
    }
}
