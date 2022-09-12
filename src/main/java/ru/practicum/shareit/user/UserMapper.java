package ru.practicum.shareit.user;

import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;


public class UserMapper {
    @Bean
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    @Bean
    public static User toUser(UserDto userDto) {
        User user = new User();
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
