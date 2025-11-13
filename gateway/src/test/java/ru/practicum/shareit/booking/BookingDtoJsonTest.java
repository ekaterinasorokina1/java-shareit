package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testNewBookingDto() throws Exception {
        NewBookingDto dto = new NewBookingDto();
        dto.setItemId(42L);
        dto.setStart(LocalDateTime.of(2026, 10, 20, 14, 30));
        dto.setEnd(LocalDateTime.of(2026, 10, 25, 18, 0));

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("itemId\":42");
        assertThat(json).contains("\"start\":\"2026-10-20T14:30:00\"");
        assertThat(json).contains("\"end\":\"2026-10-25T18:00:00\"");

        NewBookingDto deserializedDto = objectMapper.readValue(json, NewBookingDto.class);

        assertThat(deserializedDto.getItemId()).isEqualTo(dto.getItemId());
        assertThat(deserializedDto.getStart()).isEqualTo(dto.getStart());
        assertThat(deserializedDto.getEnd()).isEqualTo(dto.getEnd());
    }
}
