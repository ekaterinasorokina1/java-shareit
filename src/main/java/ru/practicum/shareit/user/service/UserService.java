package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUser;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserDto user);

    UserDto update(Long id, UpdateUser user);

    UserDto getById(Long userId);

    List<UserDto> getAll();

    void delete(Long userId);
}
