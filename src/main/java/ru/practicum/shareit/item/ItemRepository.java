package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Long id);

    Optional<Item> getItemById(Long id);

    List<Item> getItemsByUser(Long userId);

    List<Item> getItemsByCriteria(String text);
}
