package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {
    @Test
    void equals() {
        Item item = new Item();
        item.setDescription("desc one");

        Item otherItem = new Item();
        otherItem.setDescription("desc one");

        assertThat(item.equals(otherItem)).isEqualTo(false);
    }
}
