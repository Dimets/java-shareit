package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.item.model.ItemResponseMapper;
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
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BookingService bookingService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, BookingService bookingService,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingService = bookingService;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemDto itemDto) throws EntityNotFoundException {
        UserDto userDto = userService.findById(userId);
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, userDto)));
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, ItemDto itemDto) throws EntityNotFoundException, UsersDoNotMatchException {
        UserDto userDto = userService.findById(userId);

        System.out.println("userId=" + userId);
        System.out.println("owner=" + itemRepository.findById(itemDto.getId()).get().getOwner());

        if (!userId.equals(itemRepository.findById(itemDto.getId()).get().getOwner().getId())) {
            throw new UsersDoNotMatchException("Изменения доступны только для владельца вещи");
        }
        itemRepository.save(ItemMapper.toItem(itemDto, userDto));

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
    public ItemResponseDto findById(Long userId, Long itemId) throws EntityNotFoundException {
        ItemDto itemDto = findById(itemId);
        BookingUserDto lastBooking;
        BookingUserDto nextBooking;
        List<CommentDto> commentDtoList =  CommentMapper.toCommentDto(commentRepository
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
    public List<ItemResponseDto> findByUser(Long userId) throws EntityNotFoundException {
        User user = UserMapper.toUser(userService.findById(userId));

        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        BookingUserDto lastBooking;
        BookingUserDto nextBooking;
        List<CommentDto> commentDtoList;

        List<ItemDto> itemDtoList = ItemMapper.toItemDto(itemRepository.findAllByOwner(user));

        for (ItemDto itemDto : itemDtoList) {
            lastBooking = bookingService.findLastByItem(itemDto.getId());
            nextBooking = bookingService.findNextByItem(itemDto.getId());
            commentDtoList = CommentMapper.toCommentDto(commentRepository.findAllByItem_Id(itemDto.getId()));
            itemResponseDtos.add(ItemResponseMapper.toItemResponseDto(itemDto, lastBooking,
                    nextBooking, commentDtoList));
        }

        Collections.sort(itemResponseDtos);
        return itemResponseDtos;
    }

    @Override
    public List<ItemDto> findByCriteria(String text) {
        if (text.length() > 0) {
            return ItemMapper.toItemDto(itemRepository.findItemsByCriteria(text.toLowerCase()));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public CommentDto create(Long userId, Long itemId, CommentDto commentDto)
            throws EntityNotFoundException, UnsupportedStatusException, CommentValidationException {

        if (bookingService.findAllByBooker(userId, BookingStatus.CURRENT.toString()).size() > 0 ||
                bookingService.findAllByBooker(userId, BookingStatus.PAST.toString()).size()  > 0) {
            UserDto userDto = userService.findById(userId);
            Item item = itemRepository.findById(itemId).get();
            return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto, userDto, item)));
        } else {
            throw new CommentValidationException("Оставить комментарий к вещи можно только," +
                    " если пользователь ее бронировал");
        }
    }
}
