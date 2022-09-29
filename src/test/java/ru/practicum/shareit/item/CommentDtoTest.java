package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testCommentDto() throws IOException {
        CommentDto commentDto = new CommentDto(1L, "comment text", 1L, "author name",
                LocalDateTime.MAX);

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.text");
        assertThat(result).hasJsonPath("$.itemId");
        assertThat(result).hasJsonPath("$.authorName");
        assertThat(result).hasJsonPath("$.create");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(commentDto.getItemId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.create")
                .isEqualTo(commentDto.getCreate().toString());
    }
}
