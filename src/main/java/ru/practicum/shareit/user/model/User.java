package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {
    private Integer id;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String name;
}
