package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorServerHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorServerResponse handleSqlExceptionHelper(final DataIntegrityViolationException e) {
        log.error(e.getMessage());
        if (e.getRootCause().toString().contains("users_email_key")) {
            return new ErrorServerResponse("Пользователь с таким email уже существует");
        } else {
            return new ErrorServerResponse(e.getMessage());
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorServerResponse handleEmptyResultDataAccessException(final EmptyResultDataAccessException e) {
        log.error((e.getMessage()));
        return new ErrorServerResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorServerResponse handleEmailFormatException(final EmailFormatException e) {
        log.error(e.getMessage());
        return new ErrorServerResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorServerResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.error(e.getMessage());
        return new ErrorServerResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorServerResponse handleUsersDoNotMatchException(final UsersDoNotMatchException e) {
        log.error(e.getMessage());
        return new ErrorServerResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorServerResponse handleBookingValidationException(final BookingValidationException e) {
        log.error(e.getMessage());
        return new ErrorServerResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorServerResponse handleUnsupportedStatusException(final UnsupportedStatusException e) {
        log.error(e.getMessage());
        return new ErrorServerResponse(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorServerResponse handleUserNotHaveBookingException(final CommentValidationException e) {
        log.error(e.getMessage());
        return new ErrorServerResponse(e.getMessage());
    }




}