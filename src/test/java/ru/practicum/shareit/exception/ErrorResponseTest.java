package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorResponseTest {
    @Test
    void getError() {
        String message = "error message";

        ErrorResponse errorResponse = new ErrorResponse(message);

        assertThat(errorResponse.getError()).isEqualTo(message);
    }
}
