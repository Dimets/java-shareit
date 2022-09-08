package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId()
        );
    }

    public static List<ItemDto> toItemDto(Iterable<Item> items) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : items) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }

    public static Item toItem(ItemDto itemDto, UserDto userDto) {
        Item item = new Item();

        if (itemDto.getId() != null) {
            item.setId(itemDto.getId());
        }

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(UserMapper.toUser(userDto));

        return item;
    }
}
