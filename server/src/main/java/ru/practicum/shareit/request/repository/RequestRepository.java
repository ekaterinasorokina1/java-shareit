package ru.practicum.shareit.request.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Long> {
    List<Request> findAllByRequestorIdOrderByCreatedDesc(Long userId);

    List<Request> findAllByRequestorIdNot(Long userId);
}
