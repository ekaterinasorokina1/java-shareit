package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody NewItemDto item) {
        return itemService.create(item, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody UpdateItemDto item) {
        return itemService.update(id, userId, item);
    }

    @GetMapping("/{id}")
    public ItemFullInfoDto getById(@PathVariable Long id,
                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getById(id, userId);
    }

    @GetMapping()
    public List<ItemFullInfoDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAll(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemService.search(userId, text);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody NewCommentDto newComment) {
        return itemService.createComment(id, userId, newComment.getText());
    }
}
