package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtDto;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

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

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @MockBean
    BookingService bookingService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    private BookingDto bookingDto = new BookingDto(1L, LocalDateTime.MAX.minusDays(5), LocalDateTime.MAX,
            1L, 1L, BookingStatus.WAITING);

    private UserDto userDto = new UserDto(1L, "user first", "user@first.ru");

    private ItemDto itemDto = new ItemDto(1L, "first item  name", "first item description",
            Boolean.TRUE, 1L, 1L);

    private BookingExtDto bookingExtDto = new BookingExtDto(1L, BookingStatus.APPROVED, userDto, itemDto,
            LocalDateTime.MAX.minusDays(5), LocalDateTime.MAX);

    @Test
    void create() throws Exception {
        when(bookingService.create(any(), any()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").value(bookingDto.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingDto.getEnd().toString()))
                .andExpect(jsonPath("$.itemId").value(bookingDto.getItemId()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()));

        Mockito.verify(bookingService, Mockito.times(1)).create(1L, bookingDto);
    }

    @Test
    void approve() throws Exception {
        when(bookingService.approve(any(), any(), any()))
                .thenReturn(bookingExtDto);

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingExtDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingExtDto.getStatus().toString()))
                .andExpect(jsonPath("$.booker", is(bookingExtDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.item", is(bookingExtDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.start").value(bookingDto.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingDto.getEnd().toString()));

        Mockito.verify(bookingService, Mockito.times(1)).approve(1L, 1L,
                Boolean.TRUE);
    }

    @Test
    void getById() throws Exception {
        when((bookingService.findById(any(), any())))
                .thenReturn(bookingExtDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingExtDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingExtDto.getStatus().toString()))
                .andExpect(jsonPath("$.booker", is(bookingExtDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.item", is(bookingExtDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.start").value(bookingDto.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingDto.getEnd().toString()));

        Mockito.verify(bookingService, Mockito.times(1)).findById(1L, 1L);
    }

    @Test
    void getAllByBooker() throws Exception {
        when(bookingService.findAllByBooker(any(), any(), any(), any()))
                .thenReturn(List.of(bookingExtDto));

        mockMvc.perform(get("/bookings?state=APPROVED")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookingExtDto.getId()))
                .andExpect(jsonPath("$[0].status").value(bookingExtDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].booker", is(bookingExtDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$[0].item", is(bookingExtDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart().toString()))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd().toString()));

        Mockito.verify(bookingService, Mockito.times(1)).findAllByBooker(1L,
                "APPROVED", 0, Integer.MAX_VALUE);
    }

    @Test
    void getAllByOwner() throws Exception {
        when(bookingService.findAllByOwner(any(), any(), any(), any()))
                .thenReturn(List.of(bookingExtDto));

        mockMvc.perform(get("/bookings/owner?state=APPROVED")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookingExtDto.getId()))
                .andExpect(jsonPath("$[0].status").value(bookingExtDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].booker", is(bookingExtDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$[0].item", is(bookingExtDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart().toString()))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd().toString()));

        Mockito.verify(bookingService, Mockito.times(1)).findAllByOwner(1L,
                "APPROVED", 0, Integer.MAX_VALUE);
    }

    @Test
    void testHandleUnsupportedStatusException() throws Exception {
        when(bookingService.findAllByBooker(any(), any(), any(), any()))
                .thenThrow((new EntityNotFoundException("msg")));

        mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}
