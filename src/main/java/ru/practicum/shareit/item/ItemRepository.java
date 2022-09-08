package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select * from items where is_available = true " +
            "and (lower(description) like %?1% or lower(name) like %?1%)", nativeQuery = true)
    List<Item> findItemsByCriteria(String text);

    List<Item> findAllByOwner_Id(Long ownerId);
}
