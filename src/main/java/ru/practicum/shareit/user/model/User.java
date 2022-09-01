package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    @NonNull
    private String email;
    private static Long initialUserId = 1L;

    public static Long getNextId() {
        return initialUserId++;
    }
}
