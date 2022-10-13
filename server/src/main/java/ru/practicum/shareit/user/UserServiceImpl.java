package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailFormatException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) throws EmailFormatException {
        validate(userDto);
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) throws EmailFormatException {
        validate(userDto);
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            users.add(userMapper.toUserDto(user));
        }
        return users;
    }

    @Override
    public UserDto findById(Long id) throws EntityNotFoundException {
        return userMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Пользователь с id=%d не существует", id))));
    }

    void validate(UserDto userDto) throws EmailFormatException {
        if (!userDto.getEmail().contains("@")) {
            throw new EmailFormatException(String.format("Неправильный формат email: %s", userDto.getEmail()));
        }
    }
}
