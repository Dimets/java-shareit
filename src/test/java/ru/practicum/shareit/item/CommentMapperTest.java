package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentMapperTest {
    CommentMapper commentMapper = new CommentMapper(new UserMapper());

    @Test
    void toCommentDto() {
        User user = new User("user first", "user@first.ru");

        Item item = new Item("fiRst item name", "First item description", true,
                user, null);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment text");
        comment.setCreate(LocalDateTime.MAX);
        comment.setItem(item);
        comment.setAuthor(user);

        CommentDto result = commentMapper.toCommentDto(comment);

        assertThat(result.getId()).isEqualTo(comment.getId());
        assertThat(result.getText()).isEqualTo(comment.getText());
        assertThat(result.getCreate()).isEqualTo(comment.getCreate());
        assertThat(result.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(result.getItemId()).isEqualTo(comment.getItem().getId());
    }

    @Test
    void toListCommentDto() {
        User user = new User("user first", "user@first.ru");

        Item item = new Item("fiRst item name", "First item description", true,
                user, null);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment text");
        comment.setCreate(LocalDateTime.MAX);
        comment.setItem(item);
        comment.setAuthor(user);

        List<CommentDto> commentDtoList = commentMapper.toCommentDto(List.of(comment));

        assertThat(commentDtoList).hasSize(1);
    }

    @Test
    void toComment() {
        CommentDto commentDto = new CommentDto(1L, "comment text", 1L, "user first",
                LocalDateTime.MAX);

        User user = new User("user first", "user@first.ru");

        UserDto userDto = new UserDto(1L, "user first", "user@first.ru");

        Item item = new Item("fiRst item name", "First item description", true,
                user, null);

        Comment comment = commentMapper.toComment(commentDto, userDto, item);

        assertThat(comment.getId()).isEqualTo(1L);
        assertThat(comment.getAuthor().getName()).isEqualTo(user.getName());
        assertThat(comment.getCreate()).isEqualTo(commentDto.getCreate());
        assertThat(comment.getItem().getDescription()).isEqualTo(item.getDescription());
    }
}
