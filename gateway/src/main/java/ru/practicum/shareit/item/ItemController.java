package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("POST /items userId={}", userId);
        log.debug("POST /items userId={}  itemRequestDto={}", userId, itemRequestDto);
        return itemClient.createItem(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET /items userId={}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId) {
        log.info("GET /items/{}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody Map<String, Object> itemDataForUpdate) {

        log.info("PATCH /items/{}", itemId);
        log.debug("PATCH /items/{} userId={}  body={}", itemId, userId, itemDataForUpdate);

        ItemRequestDto itemRequestDto = new ItemRequestDto();

        if (itemDataForUpdate.containsKey("name")) {
            itemRequestDto.setName(itemDataForUpdate.get("name").toString());
        }

        if (itemDataForUpdate.containsKey("description")) {
            itemRequestDto.setDescription(itemDataForUpdate.get("description").toString());
        }

        if (itemDataForUpdate.containsKey("available")) {
            itemRequestDto.setAvailable((Boolean) (itemDataForUpdate.get("available")));
        }

        return itemClient.updateItem(itemId, userId, itemRequestDto);
    }

    @GetMapping("/search")
    ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text,
                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                  @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        log.info("GET /search?text={}&from={}&size={}", text, from, size);
        return itemClient.search(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createItemRequestComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @PathVariable("itemId") Long itemId,
                                                           @Valid @RequestBody CommentRequestDto commentRequestDto) {
        log.info("POST /items/{itemId}/comment userId={}", itemId, userId);
        log.debug("POST /items/{itemId} userId={} commentRequestDto={}", itemId, userId, commentRequestDto);
        commentRequestDto.setCreate(LocalDateTime.now());
        return itemClient.createItemComment(userId, itemId, commentRequestDto);
    }
}
