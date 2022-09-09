package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(),
                comment.getItem().getId(), comment.getAuthor().getName(),
                comment.getCreate());
    }

    public static List<CommentDto> toCommentDto(List<Comment> comments) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment :comments) {
            commentDtoList.add(toCommentDto(comment));
        }

        return commentDtoList;
    }

    public static Comment toComment(CommentDto commentDto, UserDto userDto, Item item) {
        Comment comment = new Comment();

        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setAuthor(UserMapper.toUser(userDto));
        comment.setItem(item);
        comment.setCreate(commentDto.getCreate());

        return comment;
    }
}
