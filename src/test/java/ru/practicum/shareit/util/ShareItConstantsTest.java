package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.util.ShareItConstants.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.util.ShareItConstants.MAX_PAGE_SIZE;

public class ShareItConstantsTest {
    @Test
    void getDEFAULT_PAGE_SIZE() {
        assertThat(DEFAULT_PAGE_SIZE).isEqualTo("20");
    }

    @Test
    void getMAX_PAGE_SIZE() {
        assertThat(MAX_PAGE_SIZE).isEqualTo(20);
    }
}
