package ru.practicum.shareit.requests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByUser(User user);

    List<ItemRequest> findAllByUserNotOrderByCreatedDesc(User user);

    Page<ItemRequest> findAllByUserNotOrderByCreatedDesc(User user, Pageable pageable);
}
