package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;
import ru.practicum.shareit.item.dto.ItemDto;
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
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(userId, itemDto)));
    }

    @Override
    public ItemDto update(Long userId, ItemDto itemDto) throws EntityNotFoundException, UsersDoNotMatchException {
        if (!userService.findById(userId).getId().equals(itemRepository.findById(itemDto.getId()).get().getOwner())) {
            throw new UsersDoNotMatchException("Изменения доступны только для владельца вещи");
        }
        itemRepository.save(ItemMapper.toItem(userId, itemDto));
        return ItemMapper.toItemDto(itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Вещь с id=%d не найдена", itemDto.getId())))
        );
    }

    @Override
    public ItemDto findById(Long itemId) throws EntityNotFoundException {
        return ItemMapper.toItemDto(itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException(
                String.format("Вещь с id=%d не найдена", itemId))));
    }

    @Override
    public List<ItemDto> findByUser(Long userid) {
        return ItemMapper.toItemDto(itemRepository.findItemsByOwner(userid));
    }

    @Override
    public List<ItemDto> findByCriteria(String text) {
        if (text.length() > 0) {
            return ItemMapper.toItemDto(itemRepository)
        } else {
            return new ArrayList<>();
        }
    }
}
