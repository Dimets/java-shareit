package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
            Boolean.TRUE, 1L, 1L);

    private ItemResponseDto itemResponseDto = new ItemResponseDto(2L, "second item name",
            "second item description", Boolean.TRUE, new BookingUserDto(), new BookingUserDto(),
            List.of(new CommentDto()));

    private CommentDto commentDto =  new CommentDto(1L, "First comment", 1L,
            "First comment author", LocalDateTime.now());

    @Test
    void create() throws Exception {
        when(itemService.create(any(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.owner").value(itemDto.getOwner()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()));

        Mockito.verify(itemService, Mockito.times(1)).create(1L, itemDto);
    }

    @Test
    void findItem() throws Exception {
        when(itemService.findById(any(), any()))
                .thenReturn(itemResponseDto);

        mockMvc.perform(get("/items/" + itemResponseDto.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(itemResponseDto.getName()))
                .andExpect(jsonPath("$.description").value(itemResponseDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemResponseDto.getAvailable()))
                .andExpect(jsonPath("$.lastBooking", is(itemResponseDto.getLastBooking()),
                        BookingUserDto.class))
                .andExpect(jsonPath("$.nextBooking", is(itemResponseDto.getLastBooking()),
                        BookingUserDto.class))
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments", hasSize(1)));

        Mockito.verify(itemService, Mockito.times(1)).findById(1L, itemResponseDto.getId());
    }

    @Test
    void findItems() throws Exception {
        when(itemService.findByUser(any(), any(), any()))
                .thenReturn(List.of(itemResponseDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemResponseDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemResponseDto.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemResponseDto.getAvailable()))
                .andExpect(jsonPath("$[0].lastBooking", is(itemResponseDto.getLastBooking()),
                        BookingUserDto.class))
                .andExpect(jsonPath("$[0].nextBooking", is(itemResponseDto.getLastBooking()),
                        BookingUserDto.class))
                .andExpect(jsonPath("$[0].comments").isArray())
                .andExpect(jsonPath("$[0].comments", hasSize(1)));

        Mockito.verify(itemService, Mockito.times(1)).findByUser(1L, 0,
                Integer.MAX_VALUE);
    }

    @Test
    void search() throws Exception {
        when(itemService.findByCriteria(any(), any(), any()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search?text=first")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$[0].owner").value(itemDto.getOwner()))
                .andExpect(jsonPath("$[0].requestId").value(itemDto.getRequestId()));


        Mockito.verify(itemService, Mockito.times(1)).findByCriteria("first", 0,
                Integer.MAX_VALUE);
    }

    @Test
    void update() throws Exception {
        ItemDto updatedItemDto = new ItemDto(1L, "updated first item  name", "updated first " +
                "item description", Boolean.TRUE, 1L, 1L);

        when(itemService.update(any(), any()))
                .thenReturn(updatedItemDto);

        when(itemService.findById(any()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(updatedItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedItemDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedItemDto.getName()))
                .andExpect(jsonPath("$.description").value(updatedItemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(updatedItemDto.getAvailable()))
                .andExpect(jsonPath("$.owner").value(updatedItemDto.getOwner()))
                .andExpect(jsonPath("$.requestId").value(updatedItemDto.getRequestId()));

        Mockito.verify(itemService, Mockito.times(1)).update(1L, updatedItemDto);
    }

    @Test
    void createComment() throws Exception {
        when(itemService.create(any(), any(), any()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                .header("X-Sharer-User-Id", 1L)
                .content(mapper.writeValueAsString(commentDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.create").isNotEmpty());

    }
}
