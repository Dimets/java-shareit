package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.EmailFormatException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserServerController {
    private final UserService userService;

    public UserServerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) throws EmailFormatException {
        log.info("POST /users");
        log.info("POST /users {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping
    public List<UserDto> findAll() {
        log.info("GET /users");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findUser(@PathVariable("userId") Long userId) throws EntityNotFoundException {
        log.info("GET /users/{}", userId);
        return userService.findById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable("userId") Long userId, @RequestBody Map<String, Object> userDataForUpdate)
            throws EmailFormatException, EntityNotFoundException {
        log.info("PATCH /users/{}", userId);
        log.debug("PATCH /users/{} body={}", userId, userDataForUpdate);

        UserDto userDtoForUpdate = userService.findById(userId);

        if (userDataForUpdate.containsKey("email")) {
            userDtoForUpdate.setEmail(userDataForUpdate.get("email").toString());
        }

        if (userDataForUpdate.containsKey("name")) {
            userDtoForUpdate.setName(userDataForUpdate.get("name").toString());
        }

        return userService.update(userDtoForUpdate);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        log.info("DELETE /users/{}", userId);
        userService.deleteById(userId);
    }
}
