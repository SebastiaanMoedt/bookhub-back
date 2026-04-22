package fr.eni.bookhub_back.exception;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.stream.Collectors;

@AllArgsConstructor
@ControllerAdvice
public class AppExceptionHandler {

    private MessageSource messageSource ;

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> capturerException(MethodArgumentNotValidException e, Locale locale){

        String titre = messageSource.getMessage("notvalidexception", null, locale);

        String message = e.getFieldErrors()
                .stream()
                .map(err->err.getDefaultMessage())
                .collect(Collectors.joining(" / "));

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(titre + message);
    }

}