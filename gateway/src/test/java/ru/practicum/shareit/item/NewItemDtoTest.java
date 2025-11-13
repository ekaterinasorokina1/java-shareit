package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.NewItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class NewItemDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeAndDeserialize_ShouldWorkCorrectly() throws Exception {
        // Создаем пример DTO
        NewItemDto dto = new NewItemDto();
        dto.setName("Test Item");
        dto.setDescription("Description of item");
        dto.setAvailable(true);
        dto.setRequestId(123L);

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"name\":\"Test Item\"");
        assertThat(json).contains("\"description\":\"Description of item\"");
        assertThat(json).contains("\"available\":true");
        assertThat(json).contains("\"requestId\":123");

        NewItemDto deserializedDto = objectMapper.readValue(json, NewItemDto.class);

        assertThat(deserializedDto.getName()).isEqualTo(dto.getName());
        assertThat(deserializedDto.getDescription()).isEqualTo(dto.getDescription());
        assertThat(deserializedDto.getAvailable()).isEqualTo(dto.getAvailable());
        assertThat(deserializedDto.getRequestId()).isEqualTo(dto.getRequestId());
    }
}
