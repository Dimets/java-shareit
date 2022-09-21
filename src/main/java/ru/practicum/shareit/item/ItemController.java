package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.CommentValidationException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/items")
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto)
            throws EntityNotFoundException {
        log.info("POST /items userId={}", userId);
        log.debug("POST /items userId={}  itemDto={}", userId, itemDto);
        return itemService.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto findItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId)
            throws EntityNotFoundException {
        log.info("GET /items/{} userId={}", itemId, userId);
        ItemResponseDto itemResponseDto = itemService.findById(userId, itemId);
        return itemResponseDto;
    }

    @GetMapping
    List<ItemResponseDto> findItems(@RequestHeader("X-Sharer-User-Id") Long userId) throws EntityNotFoundException {
        log.info("GET /items/ by userId={}", userId);
        return itemService.findByUser(userId);
    }

    @GetMapping("/search")
    List<ItemDto> search(@RequestParam String text) {
        log.info("GET /search");
        log.debug("GET /search?text={}", text);
        return itemService.findByCriteria(text);
    }

    @PatchMapping("/{itemId}")
    ItemDto update(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                   @RequestBody Map<String, Object> itemDataForUpdate)
            throws EntityNotFoundException, UsersDoNotMatchException {

        log.info("PATCH /items/{}", itemId);
        log.debug("PATCH /items/{} userId={}  body={}", itemId, userId, itemDataForUpdate);

        ItemDto itemDtoForUpdate = itemService.findById(itemId);

        if (itemDataForUpdate.containsKey("name")) {
            itemDtoForUpdate.setName(itemDataForUpdate.get("name").toString());
        }

        if (itemDataForUpdate.containsKey("description")) {
            itemDtoForUpdate.setDescription(itemDataForUpdate.get("description").toString());
        }

        if (itemDataForUpdate.containsKey("available")) {
            itemDtoForUpdate.setAvailable((Boolean) (itemDataForUpdate.get("available")));
        }

        return itemService.update(userId, itemDtoForUpdate);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId,
                      @Valid @RequestBody CommentDto commentDto)
            throws EntityNotFoundException, UnsupportedStatusException, CommentValidationException {
            log.info("POST /items/{itemId}/comment userId={}", itemId, userId);
            log.debug("POST /items/{itemId} userId={} commentDto={}", itemId, userId, commentDto);
            commentDto.setCreate(LocalDateTime.now());
        return itemService.create(userId, itemId, commentDto);
    }
}
