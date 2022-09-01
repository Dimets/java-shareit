package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) throws EntityNotFoundException {
        userService.findById(userId);
        itemDto.setId(Item.getNextId());
        return ItemMapper.toItemDto(itemRepository.createItem(ItemMapper.toItem(userId, itemDto)));
    }

    @Override
    public ItemDto update(Long userId, ItemDto itemDto) throws EntityNotFoundException, UsersDoNotMatchException {
        if (!userService.findById(userId).getId().equals(itemRepository.getItemById(itemDto.getId()).get().getOwner())) {
            throw new UsersDoNotMatchException("Изменения дступны только для владельца вещи");
        }
        itemRepository.updateItem(ItemMapper.toItem(userId, itemDto));
        return ItemMapper.toItemDto(itemRepository.getItemById(itemDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Вещь с id=%d не найдена", itemDto.getId())))
        );
    }

    @Override
    public ItemDto findById(Long itemId) throws EntityNotFoundException {
        return ItemMapper.toItemDto(itemRepository.getItemById(itemId).orElseThrow(() -> new EntityNotFoundException(
                String.format("Вещь с id=%d не найдена", itemId))));
    }

    @Override
    public List<ItemDto> findByUser(Long userid) {
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.getItemsByUser(userid)) {
            items.add(ItemMapper.toItemDto(item));
        }
        return items;
    }

    @Override
    public List<ItemDto> findByCriteria(String text) {
        List<ItemDto> items = new ArrayList<>();
        if (text.length() > 0) {
            for (Item item : itemRepository.getItemsByCriteria(text)) {
                items.add(ItemMapper.toItemDto(item));
            }
        }
        return items;
    }
}
