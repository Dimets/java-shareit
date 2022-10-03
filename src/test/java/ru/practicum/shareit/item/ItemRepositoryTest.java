package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.model.Item;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user = new User("user first", "user@first.ru");

    private Item item = new Item("fiRst item name", "First item description", true,
            user, null);

    @Test
    void findItemsByCriteria() {
        entityManager.persist(user);

        entityManager.persist(item);

        Page<Item> items = itemRepository.findItemsByCriteria("first", PageRequest.of(0, 1));

        Assertions.assertEquals(1, items.getSize());
        Assertions.assertEquals(item, items.stream().findAny().get());
    }
}
