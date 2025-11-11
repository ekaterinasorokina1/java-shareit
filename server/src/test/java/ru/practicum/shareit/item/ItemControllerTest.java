package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;


    @Test
    void ShouldCreateItem() throws Exception {
        NewItemDto newItem = new NewItemDto();
        newItem.setDescription("item description");
        newItem.setName("item name");
        newItem.setAvailable(true);
        newItem.setRequestId(1L);

        ItemDto createdDto = getItemDto(1L, 4L);

        when(itemService.create(any(NewItemDto.class), eq(4L))).thenReturn(createdDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 4L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdDto.getId()))
                .andExpect(jsonPath("$.name").value(createdDto.getName()))
                .andExpect(jsonPath("$.description").value(createdDto.getDescription()));

        verify(itemService).create(any(NewItemDto.class), eq(4L));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        UpdateItemDto updateDto = new UpdateItemDto();
        updateDto.setAvailable(true);
        updateDto.setOwnerId(4);
        updateDto.setRequestId(1L);
        updateDto.setName("Updated Name");
        updateDto.setDescription("Updated Description");

        ItemDto updatedDto = getItemDto(1L, 4L);
        updatedDto.setName("Updated Name");
        updateDto.setDescription("Updated Description");

        when(itemService.update(anyLong(), anyLong(), any(UpdateItemDto.class))).thenReturn(updatedDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 4L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedDto.getName()))
                .andExpect(jsonPath("$.description").value(updatedDto.getDescription()));

        verify(itemService).update(eq(1L), eq(4L), any(UpdateItemDto.class));
    }

    @Test
    void shouldReturnItemFullInfo() throws Exception {
        ItemFullInfoDto fullInfoDto = getSampleItemFullInfoDto();
        when(itemService.getById(anyLong(), anyLong())).thenReturn(fullInfoDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 789L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fullInfoDto.getId()))
                .andExpect(jsonPath("$.name").value(fullInfoDto.getName()));

        verify(itemService).getById(1L, 789L);
    }

    @Test
    void ShouldReturnListOfItems() throws Exception {
        List<ItemFullInfoDto> list = List.of(getSampleItemFullInfoDto(), getSampleItemFullInfoDto());
        when(itemService.getAll(anyLong())).thenReturn(list);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 321L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(list.getFirst().getId()));

        verify(itemService).getAll(321L);
    }

    @Test
    void shouldDeleteItem() throws Exception {
        doNothing().when(itemService).delete(10L);

        mockMvc.perform(delete("/items/10"))
                .andExpect(status().isOk());

        verify(itemService).delete(10L);
    }

    @Test
    void search_ShouldReturnEmptyList_WhenTextEmpty() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 555L)
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldCreateComment() throws Exception {
        NewCommentDto newComment = new NewCommentDto();
        newComment.setText("Test comment");

        CommentDto commentDto = getSampleCommentDto();

        when(itemService.createComment(anyLong(), anyLong(), any(String.class))).thenReturn(commentDto);

        mockMvc.perform(post("/items/20/comment")
                        .header("X-Sharer-User-Id", 333L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()));

        verify(itemService).createComment(20L, 333L, "Test comment");
    }


    private ItemFullInfoDto getSampleItemFullInfoDto() {
        ItemFullInfoDto dto = new ItemFullInfoDto();
        dto.setId(1L);
        dto.setName("Test Item");
        dto.setDescription("Test Description");
        return dto;
    }

    private CommentDto getSampleCommentDto() {
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setText("Test comment");
        dto.setAuthorName("Author name");
        dto.setCreated(Instant.now());
        return dto;
    }

    private ItemDto getItemDto(Long id, Long userId) {
        CommentDto commentDto = getSampleCommentDto();

        ItemDto itemDto = new ItemDto();
        itemDto.setId(id);
        itemDto.setOwnerId(userId);
        itemDto.setDescription("item description");
        itemDto.setName("item name");
        itemDto.setAvailable(true);
        itemDto.setComments(List.of(commentDto));
        return itemDto;
    }
}
