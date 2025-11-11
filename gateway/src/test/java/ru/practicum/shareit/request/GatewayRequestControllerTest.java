package ru.practicum.shareit.request;

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

import ru.practicum.shareit.request.dto.NewRequestDto;

@WebMvcTest(RequestController.class)
public class GatewayRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestClient requestClient;

    private final String headerName = "X-Sharer-User-Id";

    @Test
    void shouldGetCurrentRequests() throws Exception {
        Long userId = 1L;
        String expectedResponse = """
                [{
                    "id": 1,
                    "description": "Need a drill",
                    "created": "2025-10-01T10:00:00"
                }]
                """;

        when(requestClient.getUserRequests(userId))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(get("/requests")
                        .header(headerName, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldGetAllRequests() throws Exception {
        Long userId = 1L;
        String expectedResponse = """
                [{
                    "id": 1,
                    "description": "Need a drill",
                    "created": "2025-10-01T10:00:00"
                }, {
                    "id": 2,
                    "description": "Looking for a ladder",
                    "created": "2025-10-02T10:00:00"
                }]
                """;

        when(requestClient.getAllRequests(userId))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(get("/requests/all")
                        .header(headerName, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldGetRequestById() throws Exception {
        Long requestId = 1L;
        Long userId = 1L;
        String expectedResponse = """
                {
                    "id": 1,
                    "description": "Need a drill",
                    "created": "2025-10-01T10:00:00",
                    "items": []
                }
                """;

        when(requestClient.getRequestById(requestId, userId))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header(headerName, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldCreateRequest() throws Exception {
        Long userId = 1L;

        NewRequestDto dto = new NewRequestDto();
        dto.setDescription("New request");

        String expectedResponse = """
                {
                    "id": 1,
                    "description": "New request",
                    "created": "2025-11-01T10:00:00"
                }
                """;

        when(requestClient.createRequest(any(Long.class), any(NewRequestDto.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        mockMvc.perform(post("/requests")
                        .header(headerName, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }
}
