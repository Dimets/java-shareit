package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingUserDto;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UsersDoNotMatchException;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemBookingMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingService bookingService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, BookingService bookingService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingService = bookingService;
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
    public ItemBookingDto findById(Long userId, Long itemId) throws EntityNotFoundException {
        ItemDto itemDto = findById(itemId);
        BookingUserDto lastBooking;
        BookingUserDto nextBooking;


        if (userId.equals(itemDto.getOwner())) {
            lastBooking = bookingService.findLastByItem(itemId);
            nextBooking = bookingService.findNextByItem(itemId);
        } else {
            lastBooking = null;
            nextBooking = null;
        }

        return ItemBookingMapper.toItemBookingDto(itemDto, lastBooking, nextBooking);
    }

    @Override
    public List<ItemBookingDto> findByUser(Long userId) throws EntityNotFoundException {
        List<ItemBookingDto> itemBookingDtos = new ArrayList<>();
        BookingUserDto lastBooking;
        BookingUserDto nextBooking;

        List<ItemDto> itemDtoList = ItemMapper.toItemDto(itemRepository.findAllByOwner_Id(userId));

        for (ItemDto itemDto : itemDtoList) {
            lastBooking = bookingService.findLastByItem(itemDto.getId());
            nextBooking = bookingService.findNextByItem(itemDto.getId());
            itemBookingDtos.add(ItemBookingMapper.toItemBookingDto(itemDto, lastBooking, nextBooking));
        }

        Collections.sort(itemBookingDtos);
        return itemBookingDtos;
    }

    @Override
    public List<ItemDto> findByCriteria(String text) {
        if (text.length() > 0) {
            return ItemMapper.toItemDto(itemRepository.findItemsByCriteria(text.toLowerCase()));
        } else {
            return new ArrayList<>();
        }
    }
}
