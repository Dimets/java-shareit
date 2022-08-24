package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailFormatException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UserEmailAlreadyExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(UserDto userDto) throws UserEmailAlreadyExistException, EmailFormatException {
        validate(userDto);
        userDto.setId(User.getNextId());
        return UserMapper.toUserDto(userRepository.createUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto) throws EmailFormatException, UserEmailAlreadyExistException {
        validate(userDto);
        return UserMapper.toUserDto(userRepository.updateUser(UserMapper.toUser(userDto)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteUser(id);
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto> users = new ArrayList<>();
        for (User user : userRepository.getAllUsers()) {
            users.add(UserMapper.toUserDto(user));
        }
        return users;
    }

    @Override
    public UserDto findById(Long id) throws EntityNotFoundException {
        return UserMapper.toUserDto(userRepository.getUserById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Пользователь с id=%d не существует", id))));
    }

    void validate(UserDto userDto) throws UserEmailAlreadyExistException, EmailFormatException {
        if (userRepository.getAllUsers().stream()
                .filter(x -> x.getEmail().equals(userDto.getEmail()))
                .filter(x -> !x.getId().equals(userDto.getId()))
                .count() > 0) {
            throw new UserEmailAlreadyExistException(
                    String.format("Пользователь с email %s уже существует", userDto.getEmail()));
        }

        if (!userDto.getEmail().contains("@")) {
            throw new EmailFormatException(String.format("Неправильный формат email: %s", userDto.getEmail()));
        }
    }
}
