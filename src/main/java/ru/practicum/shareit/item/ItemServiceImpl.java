package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.exception.CommentValidationException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;
    private final ItemRequestService itemRequestService;

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemDto itemDto) throws EntityNotFoundException {
        UserDto userDto = userService.findById(userId);

        if (itemDto.getRequestId() != null) {
            ItemRequestDto itemRequestDto = itemRequestService.findById(itemDto.getRequestId());
            return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, userDto, itemRequestDto)));
        }

        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, userDto)));
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, ItemDto itemDto) throws EntityNotFoundException, UsersDoNotMatchException {
        UserDto userDto = userService.findById(userId);

        if (!userId.equals(itemRepository.findById(itemDto.getId()).get().getOwner().getId())) {
            throw new UsersDoNotMatchException("Изменения доступны только для владельца вещи");
        }
        itemRepository.save(itemMapper.toItem(itemDto, userDto));

        return itemMapper.toItemDto(itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Вещь с id=%d не найдена", itemDto.getId())))
        );
    }

    @Override
    public ItemDto findById(Long itemId) throws EntityNotFoundException {
        return itemMapper.toItemDto(itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException(
                String.format("Вещь с id=%d не найдена", itemId))));
    }

    @Override
    public ItemResponseDto findById(Long userId, Long itemId) throws EntityNotFoundException {
        ItemDto itemDto = findById(itemId);
        BookingUserDto lastBooking;
        BookingUserDto nextBooking;
        List<CommentDto> commentDtoList =  commentMapper.toCommentDto(commentRepository
                .findAllByItem_Id(itemDto.getId()));


        if (userId.equals(itemDto.getOwner())) {
            lastBooking = bookingService.findLastByItem(itemId);
            nextBooking = bookingService.findNextByItem(itemId);
        } else {
            lastBooking = null;
            nextBooking = null;
        }

        return ItemResponseMapper.toItemResponseDto(itemDto, lastBooking, nextBooking, commentDtoList);
    }

    @Override
    public List<ItemResponseDto> findByUser(Long userId, Integer from, Integer size) throws EntityNotFoundException {
        User user = userMapper.toUser(userService.findById(userId));

        Pageable pageable = PageRequest.of(from / size, size);

        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        BookingUserDto lastBooking;
        BookingUserDto nextBooking;
        List<CommentDto> commentDtoList;

        List<ItemDto> itemDtoList = itemMapper.toItemDto(itemRepository.findAllByOwner(user, pageable));

        for (ItemDto itemDto : itemDtoList) {
            lastBooking = bookingService.findLastByItem(itemDto.getId());
            nextBooking = bookingService.findNextByItem(itemDto.getId());
            commentDtoList = commentMapper.toCommentDto(commentRepository.findAllByItem_Id(itemDto.getId()));
            itemResponseDtos.add(ItemResponseMapper.toItemResponseDto(itemDto, lastBooking,
                    nextBooking, commentDtoList));
        }

        Collections.sort(itemResponseDtos);
        return itemResponseDtos;
    }

    @Override
    public List<ItemDto> findByCriteria(String text, Integer from, Integer size) {
        if (text.length() > 0) {
            Pageable pageable = PageRequest.of(from / size, size);
            return itemMapper.toItemDto(itemRepository.findItemsByCriteria(text.toLowerCase(), pageable));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public CommentDto create(Long userId, Long itemId, CommentDto commentDto)
            throws EntityNotFoundException, UnsupportedStatusException, CommentValidationException {

        if (bookingService.isExistsCurrentByBooker(userId) ||
                bookingService.isExistsPastByBooker(userId)) {
            UserDto userDto = userService.findById(userId);
            Item item = itemRepository.findById(itemId).get();
            return commentMapper.toCommentDto(commentRepository.save(commentMapper.toComment(commentDto,
                    userDto, item)));
        } else {
            throw new CommentValidationException("Оставить комментарий к вещи можно только," +
                    " если пользователь ее бронировал");
        }
    }

    @Override
    public List<ItemDto> findByRequest(ItemRequest itemRequest) {
        return itemMapper.toItemDto(itemRepository.findAllByItemRequest(itemRequest));
    }
}
