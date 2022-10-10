package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ItemResponseDtoTest {
    @Test
    void compareTo() {
        ItemResponseDto itemResponseDto = new ItemResponseDto(1L, "first name", "first desc", true,
                null, null, null);

        ItemResponseDto otherItemResponseDto = new ItemResponseDto(2L, "first name", "first desc",
                true, null, null, null);

        assertThat(itemResponseDto.compareTo(otherItemResponseDto)).isEqualTo(-1);
    }

}
