package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "select it.id, it.name, it.description, it.is_available, it.owner_id, it.request_id " +
            "from items as it where it.owner_id = ?1", nativeQuery = true)
    List<Item> findItemsByOwner(Long userId);

    @Query(value = "select * from items where " )
}
