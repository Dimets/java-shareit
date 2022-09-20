package ru.practicum.shareit.requests;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRequestMapper {
    private final UserMapper userMapper;

    public ItemRequestMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setRequesterId(itemRequest.getUser().getId());

        return itemRequestDto;
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, UserDto userDto) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(itemRequestDto.getCreated());
        itemRequest.setUser(userMapper.toUser(userDto));

        return itemRequest;
    }

    public List<ItemRequestDto> toItemRequestDto(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        for (ItemRequest itemRequest : itemRequests) {
            itemRequestDtos.add(toItemRequestDto(itemRequest));
        }

        return itemRequestDtos;
    }
}
