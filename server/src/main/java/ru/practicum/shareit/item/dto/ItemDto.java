package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto implements Comparable<ItemDto> {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private Long requestId;

    @Override
    public int compareTo(ItemDto o) {
        if (this.getId() < o.getId()) {
            return -1;
        }

        if (this.getId() > o.getId()) {
            return 1;
        }

        return 0;
    }
}
