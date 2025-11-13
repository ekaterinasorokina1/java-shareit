package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplIntegrationTest {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Test
    void createItem() {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setName("New user");
        newUserDto.setEmail("user@mail.ru");

        UserDto user = userService.create(newUserDto);

        NewItemDto newItem = new NewItemDto();
        newItem.setDescription("item description");
        newItem.setName("item name");
        newItem.setAvailable(true);

        ItemDto itemDto = itemService.create(newItem, user.getId());

        assertThat(itemDto.getId(), notNullValue());
        assertThat(itemDto.getName(), equalTo(newItem.getName()));
        assertThat(itemDto.getDescription(), equalTo(newItem.getDescription()));

        Item saved = itemRepository.findById(itemDto.getId()).orElseThrow();
        assertThat(saved.getOwner().getId(), equalTo(user.getId()));
    }

}
