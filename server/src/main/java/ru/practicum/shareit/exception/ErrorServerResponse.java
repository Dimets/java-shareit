package ru.practicum.shareit.exception;

public class ErrorServerResponse {
    private final String error;

    public ErrorServerResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
