package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingDto;

@WebMvcTest(BookingController.class)
public class GatewayBookingTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private final String headerName = "X-Sharer-User-Id";

    @Test
    void shouldGetAllUserBookings() throws Exception {
        Long userId = 1L;
        String expectedResponse = """
                [{
                    "id": 1,
                    "start": "2025-10-01T10:00:00",
                    "end": "2025-10-10T10:00:00",
                    "status": "WAITING"
                }]
                """;

        when(bookingClient.getUserBookings(userId, BookingState.ALL))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(get("/bookings/owner")
                        .header(headerName, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldGetBookingById() throws Exception {
        long userId = 1L;
        Long bookingId = 100L;
        String expectedResponse = """
                {
                    "id": 100,
                    "start": "2025-10-01T10:00:00",
                    "end": "2025-10-10T10:00:00",
                    "status": "APPROVED"
                }
                """;

        when(bookingClient.getBooking(userId, bookingId))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(headerName, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldCreateNewBooking() throws Exception {
        Long userId = 1L;
        NewBookingDto newBooking = new NewBookingDto();
        newBooking.setItemId(1L);

        String expectedResponse = """
                {
                    "id": 101,
                    "start": "2025-10-01T10:00:00",
                    "end": "2025-10-10T10:00:00",
                    "status": "WAITING"
                }
                """;

        when(bookingClient.bookItem(eq(userId), any(NewBookingDto.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(post("/bookings")
                        .header(headerName, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBooking)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }
}
