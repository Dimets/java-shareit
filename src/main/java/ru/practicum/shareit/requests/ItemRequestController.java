package ru.practicum.shareit.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    public ItemRequestController(ItemRequestService itemRequestService, UserService userService) {
        this.itemRequestService = itemRequestService;
        this.userService = userService;
    }

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto) throws EntityNotFoundException {
        log.info("POST /requests userId={}", userId);
        log.debug("POST /requests userId={}  itemRequestDto={}", userId, itemRequestDto);

        itemRequestDto.setCreated(LocalDateTime.now());
        UserDto userDto = userService.findById(userId);

        return itemRequestService.create(userDto, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("requestId") Long requestId) throws EntityNotFoundException {
        log.info("GET /requests/{} userId={}", requestId, userId);
        userService.findById(userId);
        return itemRequestService.findById(requestId);
    }

    @GetMapping
    public List<ItemRequestDto> findOwnItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId)
            throws EntityNotFoundException {
        log.info("GET /requests userId={}", userId);

        return itemRequestService.findAll(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                    @RequestParam(defaultValue = "0x7fffffff") @Min(1) Integer size)
            throws EntityNotFoundException {
        log.info("GET /requests/all?from={}&size={} userId={}", from, size, userId);

        return itemRequestService.findAllOther(userId, from, size);
    }
}
