package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserDto create(NewUserDto newUser) {
        User user = UserMapper.mapToUser(newUser);

        userStorage.checkIExistEmail(newUser.getEmail());

        user = userStorage.create(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(int userId, UpdateUser updateUser) {
        User user = userStorage.getById(userId);

        userStorage.checkIExistEmail(updateUser.getEmail());
        User updatedUser = UserMapper.updateUserFields(user, updateUser);
        userStorage.update(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);

    }

    public UserDto getById(int userId) {
        return UserMapper.mapToUserDto(userStorage.getById(userId));
    }

    public List<UserDto> getAll() {
        return userStorage.getAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    public void delete(int userId) {
        userStorage.delete(userId);
    }
}
