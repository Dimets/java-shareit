package ru.practicum.shareit.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepositoryCustom extends JpaRepository<ItemRequest, Long> {
  /*  @Query(value = "select new ru.practicum.shareit.requests.dto.ItemRequestDto(r.id, " +
            "r.description, r.requestor_id, r.create_dt, null)\n" +
            "from requests r\n" +
            "where r.requestor_id != ?1")*/
    List<ItemRequestDto> findAllByUserIdNot(Long userId);
}
