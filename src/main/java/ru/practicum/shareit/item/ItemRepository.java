package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select * from items where is_available = true " +
            "and (lower(description) like %?1% or lower(name) like %?1%)", nativeQuery = true)
    Page<Item> findItemsByCriteria(String text, Pageable pageable);
    Page<Item> findAllByOwner(User owner, Pageable pageable);

    List<Item> findAllByItemRequest(ItemRequest itemRequest);
}
