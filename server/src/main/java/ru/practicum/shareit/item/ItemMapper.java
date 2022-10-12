package ru.practicum.shareit.item;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {
    private final UserMapper userMapper;
    @Lazy
    private final ItemRequestMapper itemRequestMapper;

    public ItemMapper(UserMapper userMapper, ItemRequestMapper itemRequestMapper) {
        this.userMapper = userMapper;
        this.itemRequestMapper = itemRequestMapper;
    }

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                item.getItemRequest() != null ? item.getItemRequest().getId() : null
        );
    }

    public List<ItemDto> toItemDto(Iterable<Item> items) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : items) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }

    public Item toItem(ItemDto itemDto, UserDto userDto) {
        Item item = new Item();

        if (itemDto.getId() != null) {
            item.setId(itemDto.getId());
        }

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(userMapper.toUser(userDto));

        return item;
    }

    public Item toItem(ItemDto itemDto, UserDto userDto, ItemRequestDto itemRequestDto) {
        Item item = new Item();

        if (itemDto.getId() != null) {
            item.setId(itemDto.getId());
        }

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(userMapper.toUser(userDto));
        item.setItemRequest(itemRequestMapper.toItemRequest(itemRequestDto, userDto));

        return item;
    }

}
