package ru.practicum.shareit.exception;

public class UserEmailAlreadyExistException extends Exception {
    public UserEmailAlreadyExistException(String message) {
        super(message);
    }
}
