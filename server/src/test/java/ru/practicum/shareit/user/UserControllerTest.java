package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;


import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setName("New user");
        newUserDto.setEmail("user@mail.ru");

        UserDto userDto = getUserDto(1L);

        when(userService.create(any(NewUserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
        verify(userService).create(any(NewUserDto.class));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UpdateUser updateDto = new UpdateUser();
        updateDto.setName("Update user");
        updateDto.setEmail("updateuser@mail.ru");

        UserDto userDto = new UserDto();
        userDto.setId((1L));
        userDto.setName("Update user");
        userDto.setEmail("updateuser@mail.ru");

        when(userService.update(anyLong(), any(UpdateUser.class))).thenReturn(userDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));

        verify(userService).update(anyLong(), any(UpdateUser.class));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        UserDto userDto = getUserDto(1L);
        when(userService.getById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));

        verify(userService).getById(1L);
    }

    @Test
    void shouldReturnUserList() throws Exception {
        UserDto userDto = getUserDto(1L);
        List<UserDto> users = List.of(userDto, getUserDto(2L));
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()));

        verify(userService).getAll();
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        when(userService.getById(4L)).thenThrow(new NotFoundException("Пользователь с id = 4 не найден"));

        mockMvc.perform(get("/users/4"))
                .andExpect(status().isNotFound());

        verify(userService).getById(4L);
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService).delete(1L);
    }

    private UserDto getUserDto(Long id) {
        UserDto userDto = new UserDto();
        userDto.setId((id));
        userDto.setName("New user");
        userDto.setEmail("user@mail.ru");
        return userDto;
    }
}
