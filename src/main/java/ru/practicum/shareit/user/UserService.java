package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.EmailFormatException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UserEmailAlreadyExistException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto) throws UserEmailAlreadyExistException, EmailFormatException;

    UserDto update(UserDto userDto) throws EmailFormatException, UserEmailAlreadyExistException;

    void deleteById(Long id);

    List<UserDto> findAll();

    UserDto findById(Long id) throws EntityNotFoundException;
}
