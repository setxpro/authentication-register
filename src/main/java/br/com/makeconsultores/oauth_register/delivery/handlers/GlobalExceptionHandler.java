package br.com.makeconsultores.oauth_register.delivery.handlers;

import br.com.makeconsultores.oauth_register.delivery.exceptions.*;
import br.com.makeconsultores.oauth_register.infra.services.dtos.MessageDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest request) {
        MessageDTO error = new MessageDTO(e.getMessage(), HttpStatus.NOT_FOUND.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(FieldCannotEmptyException.class)
    public ResponseEntity<Object> handleFieldCannotEmptyException(FieldCannotEmptyException e, WebRequest request) {
        MessageDTO error = new MessageDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(FoundedException.class)
    public ResponseEntity<Object> handleFoundedException(FoundedException e, WebRequest request) {
        MessageDTO error = new MessageDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException e, WebRequest request) {
        MessageDTO error = new MessageDTO(e.getMessage(), HttpStatus.UNAUTHORIZED.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessException(UnauthorizedAccessException e, WebRequest request) {
        MessageDTO error = new MessageDTO(e.getMessage(), HttpStatus.UNAUTHORIZED.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(BadValidationException.class)
    public ResponseEntity<Object> handleBadValidationException(BadValidationException e, WebRequest request) {
        MessageDTO error = new MessageDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e, WebRequest request) {
        MessageDTO error = new MessageDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
