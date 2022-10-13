package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	@NotNull(message = "Id вещи указывать обязательно!")
	private long itemId;

	@FutureOrPresent
	@NotNull(message = "Начало периода бронирования указывать обязательно!")
	private LocalDateTime start;

	@Future
	@NotNull(message = "Конец периода бронирования указывать обязательно!")
	private LocalDateTime end;

	@Override
	public String toString() {
		return "BookItemRequestDto{" +
				"itemId=" + itemId +
				", start=" + start +
				", end=" + end +
				'}';
	}
}
