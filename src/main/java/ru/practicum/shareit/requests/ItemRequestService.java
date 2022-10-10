package ru.practicum.shareit.requests;

import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(UserDto userDto, ItemRequestDto itemRequestDto) throws EntityNotFoundException;

    ItemRequestDto findById(Long requestId) throws EntityNotFoundException;

    List<ItemRequestDto> findAll(Long userId) throws EntityNotFoundException;

    List<ItemRequestDto> findAllOther(Long userId, Integer from, Integer size) throws EntityNotFoundException;

}
