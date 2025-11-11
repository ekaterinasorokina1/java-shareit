package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, NewItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> updateItem(Long id, Long userId, UpdateItemDto item) {
        return patch("/" + id, userId, item);
    }

    public ResponseEntity<Object> getItemById(Long id, Long userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getAllItems(Long userId) {
        return get("", userId);
    }

    public void deleteItem(Long id) {
        delete("/" + id);
    }

    public ResponseEntity<Object> searchItem(Long userId, String text) {
        Map<String, Object> params = Map.of(
                "text", text
        );
        return get("", userId, params);
    }

    public ResponseEntity<Object> createComment(Long id, Long userId, NewCommentDto comment) {
        Map<String, Object> parameters = Map.of(
                "id", id
        );
        return post("/{id}/comment", userId, parameters, comment);
    }
}
