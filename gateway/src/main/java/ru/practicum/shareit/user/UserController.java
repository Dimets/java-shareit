package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        log.info("POST /users");
        log.debug("POST /users {}", userRequestDto);
        return userClient.createUser(userRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("GET /users");
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("GET /users/{}", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") Long userId) {
        log.info("DELETE /users/{}", userId);
        return userClient.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable("userId") Long userId,
                                             @RequestBody Map<String, Object> userDataForUpdate) {
        log.info("PATCH /users/{}", userId);
        log.debug("PATCH /users/{} body={}", userId, userDataForUpdate);
        UserRequestDto userRequestDto = new UserRequestDto();

        if (userDataForUpdate.containsKey("email")) {
            userRequestDto.setEmail(userDataForUpdate.get("email").toString());
        }

        if (userDataForUpdate.containsKey("name")) {
            userRequestDto.setName(userDataForUpdate.get("name").toString());
        }

        return userClient.updateUser(userId, userRequestDto);
    }

}
