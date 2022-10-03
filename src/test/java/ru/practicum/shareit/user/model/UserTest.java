package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    void equals() {
        User user = new User("user name", "user@name");

        User otherUser = new User("other user name", "other_user@name");

        assertThat(user.equals(otherUser)).isEqualTo(false);
    }
}
