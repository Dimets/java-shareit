package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class InMemoryItemRepository implements ItemRepository {
    private Map<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public void deleteItem(Long id) {
        items.remove(id);
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> getItemsByUser(Long userId) {
        List<Item> userItemsList = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().equals(userId)) {
                userItemsList.add(item);
            }
        }
        return userItemsList;
    }

    @Override
    public List<Item> getItemsByCriteria(String text) {
        List<Item> itemsList = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getAvailable() == Boolean.TRUE &&
                    (item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                            item.getName().toLowerCase().contains(text.toLowerCase()))) {
                itemsList.add(item);
            }
        }
        return itemsList;
    }
}
