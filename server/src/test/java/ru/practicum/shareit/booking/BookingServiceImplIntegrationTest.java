package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.StatusEnum;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;


    @Test
    void createBookingSuccess() {
        User user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        userRepository.save(user);

        Item item = new Item();
        item.setName("Test Item");
        item.setAvailable(true);
        item.setOwner(user);

        itemRepository.save(item);
        NewBookingDto newBooking = new NewBookingDto();
        newBooking.setItemId(item.getId());
        newBooking.setStart(LocalDateTime.of(2026, 10, 20, 14, 30));
        newBooking.setEnd(LocalDateTime.of(2026, 10, 25, 18, 0));

        BookingDto bookingDto = bookingService.create(user.getId(), newBooking);

        assertNotNull(bookingDto);
        assertEquals(StatusEnum.WAITING, bookingDto.getStatus());
        assertEquals(item.getId(), bookingDto.getItem().getId());
        assertEquals(user.getId(), bookingDto.getBooker().getId());
        assertTrue(bookingRepository.existsById(bookingDto.getId()));
    }
}