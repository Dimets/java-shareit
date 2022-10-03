package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {
    @Test
    void equals() {
        Comment comment = new Comment();
        comment.setText("text one");

        Comment otherComment = new Comment();
        comment.setText("text two");

        assertThat(comment.equals(otherComment)).isEqualTo(false);
    }
}
