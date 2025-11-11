package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto create(NewUserDto newUser) {
        User user = userRepository.save(UserMapper.mapToUser(newUser));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(Long userId, UpdateUser updateUser) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь с id = " + userId + " не найден"));

        checkIExistEmail(updateUser.getEmail(), userId);
        User updatedUser = UserMapper.updateUserFields(user, updateUser);
        userRepository.save(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);

    }

    public UserDto getById(Long userId) {
        return UserMapper.mapToUserDto(userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь с id = " + userId + " не найден")));
    }

    public List<UserDto> getAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(UserMapper::mapToUserDto).toList();
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    private void checkIExistEmail(String email, Long id) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && (!user.get().getId().equals(id))) {
            log.error("Данный email уже используется");
            throw new ConflictException("Данный email занят");
        }
    }
}
