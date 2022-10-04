package ru.practicum.shareit.requests.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestTest {
    @Test
    void equals() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("desc one");

        ItemRequest otherItemRequest = new ItemRequest();
        otherItemRequest.setDescription("desc one");

        assertThat(itemRequest.equals(otherItemRequest)).isEqualTo(false);
    }
}
