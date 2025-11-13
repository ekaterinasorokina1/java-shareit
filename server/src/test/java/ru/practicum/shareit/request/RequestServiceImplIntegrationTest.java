package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
public class RequestServiceImplIntegrationTest {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;


    @Test
    void createAddNewRequest() {
        User user = new User();
        user.setEmail("requestuser@example.com");
        user.setName("Request User");
        userRepository.save(user);

        NewRequestDto newRequestDto = new NewRequestDto();
        newRequestDto.setDescription("Test request description");

        ItemRequestDto createdRequest = requestService.create(user.getId(), newRequestDto);

        assertThat(createdRequest).isNotNull();
        assertThat(createdRequest.getId()).isNotNull();
        assertThat(createdRequest.getDescription()).isEqualTo("Test request description");
    }

    @Test
    void getAll_shouldReturnRequestsExcludingUserRequests() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setName("User One");
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setName("User Two");
        userRepository.save(user2);

        Request req1 = new Request();
        req1.setDescription("Request 1");
        req1.setRequestor(user1);
        requestRepository.save(req1);

        Request req2 = new Request();
        req2.setDescription("Request 2");
        req2.setRequestor(user2);
        requestRepository.save(req2);

        List<ItemRequestDto> requests = requestService.getAll(user1.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.getFirst().getId()).isEqualTo(req2.getId());
    }

}