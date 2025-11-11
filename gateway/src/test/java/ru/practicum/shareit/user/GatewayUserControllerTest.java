package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUser;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class GatewayUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @Test
    void shouldGetUserById() throws Exception {
        Long userId = 1L;
        String expectedResponse = """
                {
                    "id": 1,
                    "name": "Kate",
                    "email": "user@mail.com"
                }
                """;

        when(userClient.getUserById(userId))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // when & then
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        // given
        String expectedResponse = """
                [{
                    "id": 1,
                    "name": "user1",
                    "email": "user1@mail.com"
                }, {
                    "id": 2,
                    "name": "user2",
                    "email": "user2@mail.com"
                }]
                """;

        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // when & then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldAddUser() throws Exception {
        // given
        NewUserDto userDto = new NewUserDto();
        userDto.setName("New user");
        userDto.setEmail("user@mail.ru");

        String expectedResponse = """
                {
                    "id": 1,
                    "name": "New user",
                    "email": "user@mail.ru"
                }
                """;

        when(userClient.createUser(any(NewUserDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(expectedResponse));

        // when & then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // given
        Long userId = 1L;
        UpdateUser updateDto = new UpdateUser();
        updateDto.setName("Update user");
        updateDto.setEmail("updateuser@mail.ru");

        String expectedResponse = """
                {
                    "id": 1,
                    "name": "Updated User",
                    "email": "updated@example.com"
                }
                """;

        when(userClient.updateUser(eq(userId), any(UpdateUser.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // when & then
        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }
}