package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.CommentValidationException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto) throws EntityNotFoundException;

    ItemDto update(Long userId, ItemDto itemDto) throws EntityNotFoundException, UsersDoNotMatchException;

    ItemResponseDto findById(Long userId, Long itemId) throws EntityNotFoundException;

    ItemDto findById(Long itemId) throws EntityNotFoundException;

    List<ItemResponseDto> findByUser(Long userid) throws EntityNotFoundException;

    List<ItemDto> findByCriteria(String text);

    List<ItemDto> findByRequest(ItemRequest itemRequest);

    CommentDto create(Long userId, Long itemId, CommentDto commentDto) throws EntityNotFoundException,
            UnsupportedStatusException, CommentValidationException;
}
