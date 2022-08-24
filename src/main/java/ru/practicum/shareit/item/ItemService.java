package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create (Long userId, ItemDto itemDto) throws EntityNotFoundException;

    ItemDto update (Long userId, ItemDto itemDto) throws EntityNotFoundException, UsersDoNotMatchException;

    ItemDto findById(Long itemId) throws EntityNotFoundException;

    List<ItemDto> findByUser(Long userid);

    List<ItemDto> findByCriteria(String text);
}
