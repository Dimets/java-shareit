package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestDto create(UserDto userDto, ItemRequestDto itemRequestDto) throws EntityNotFoundException {
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(
                itemRequestMapper.toItemRequest(itemRequestDto, userDto)));
    }

    @Override
    public ItemRequestDto findById(Long requestId) throws EntityNotFoundException {
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Запрос с id=%d не найден", requestId))));

        UserDto userDto = userService.findById(itemRequestDto.getRequesterId());

        itemRequestDto.setItems(itemMapper.toItemDto(itemRepository.findAllByItemRequest(
                itemRequestMapper.toItemRequest(itemRequestDto, userDto))));

        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> findAll(Long userId) throws EntityNotFoundException {
        User user = userMapper.toUser(userService.findById(userId));

        List<ItemRequestDto> itemRequestDtos = itemRequestMapper.toItemRequestDto(itemRequestRepository
                .findAllByUser(user));

        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            itemRequestDto.setItems(itemMapper.toItemDto(itemRepository.findAllByItemRequest(
                    itemRequestMapper.toItemRequest(itemRequestDto, userMapper.toUserDto(user)))));
        }

        return itemRequestDtos;
    }

    @Override
    public List<ItemRequestDto> findAllOther(Long userId, Integer from, Integer size) throws EntityNotFoundException {
        if (size != Integer.MAX_VALUE) {

            User user = userMapper.toUser(userService.findById(userId));

            Sort newestFirst = Sort.by(Sort.Direction.DESC, "created");

            Pageable pageable = PageRequest.of(from / size, size, newestFirst);

            List<ItemRequestDto> itemRequestDtos = itemRequestMapper.toItemRequestDto(itemRequestRepository
                    .findAllByUserNot(user, pageable).stream().collect(Collectors.toList()));


            for (ItemRequestDto itemRequestDto : itemRequestDtos) {
                itemRequestDto.setItems(itemMapper.toItemDto(itemRepository.findAllByItemRequest(
                        itemRequestMapper.toItemRequest(itemRequestDto, userMapper.toUserDto(user)))));
            }

            return itemRequestDtos;
        } else {
            return new ArrayList<>();
        }
    }

}
