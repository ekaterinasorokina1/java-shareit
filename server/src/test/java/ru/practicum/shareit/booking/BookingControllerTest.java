package ru.practicum.shareit.booking;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.StateEnum;
import ru.practicum.shareit.booking.model.StatusEnum;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void shouldCreateBooking() throws Exception {
        NewBookingDto newBooking = getSampleNewBookingDto();
        BookingDto savedDto = getSampleBookingDto();

        when(bookingService.create(anyLong(), any(NewBookingDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 123L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBooking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedDto.getId()))
                .andExpect(jsonPath("$.status").value(savedDto.getStatus().name()));

        verify(bookingService).create(eq(123L), any(NewBookingDto.class));
    }

    @Test
    void shouldUpdatedBooking() throws Exception {
        BookingDto updatedDto = getSampleBookingDto();

        when(bookingService.update(anyLong(), anyLong(), eq(true))).thenReturn(updatedDto);

        mockMvc.perform(patch("/bookings/5")
                        .header("X-Sharer-User-Id", 456L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDto.getId()))
                .andExpect(jsonPath("$.status").value(updatedDto.getStatus().name()));

        verify(bookingService).update(eq(5L), eq(456L), eq(true));
    }

    @Test
    void shouldReturnBookingById() throws Exception {
        BookingDto dto = getSampleBookingDto();

        when(bookingService.getById(anyLong(), eq(789L))).thenReturn(dto);

        mockMvc.perform(get("/bookings/10")
                        .header("X-Sharer-User-Id", 789L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));

        verify(bookingService).getById(eq(10L), eq(789L));
    }

    @Test
    void shouldReturnListOfUserBooking() throws Exception {
        List<BookingDto> list = List.of(getSampleBookingDto(), getSampleBookingDto());

        when(bookingService.getAllUserBooking(anyLong(), eq(StateEnum.ALL))).thenReturn(list);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 321L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(list.getFirst().getId()));

        verify(bookingService).getAllUserBooking(eq(321L), eq(StateEnum.ALL));
    }

    @Test
    void getAllBooking_ShouldReturnListOfBookingDto() throws Exception {
        List<BookingDto> list = List.of(getSampleBookingDto());

        when(bookingService.getAllBookerBooking(anyLong(), eq(StateEnum.FUTURE))).thenReturn(list);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 654L)
                        .param("state", "FUTURE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(list.getFirst().getId()));

        verify(bookingService).getAllBookerBooking(eq(654L), eq(StateEnum.FUTURE));
    }

    private BookingDto getSampleBookingDto() {
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now());
        dto.setStatus(StatusEnum.APPROVED);
        return dto;
    }

    private NewBookingDto getSampleNewBookingDto() {
        NewBookingDto dto = new NewBookingDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now());
        return dto;
    }
}
