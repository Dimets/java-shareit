package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody ItemRequestRequestDto itemRequestRequestDto) {
        log.info("POST /requests userId={}", userId);
        log.debug("POST /requests userId={}  itemRequestRequestDto={}", userId, itemRequestRequestDto);

        itemRequestRequestDto.setCreated(LocalDateTime.now());

        return itemRequestClient.createItemRequest(userId, itemRequestRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findOwnItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /requests userId={}", userId);

        return itemRequestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                    @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        log.info("GET /requests/all?from={}&size={} userId={}", from, size, userId);

        return itemRequestClient.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("requestId") Long requestId) {
        log.info("GET /requests/{} userId={}", requestId, userId);

        return itemRequestClient.getItemRequest(userId, requestId);
    }

}
