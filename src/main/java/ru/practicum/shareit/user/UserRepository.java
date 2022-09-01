package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);
}
