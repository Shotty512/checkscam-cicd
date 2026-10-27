package com.example.checkscam.exception;

import com.example.checkscam.constant.ErrorCodeEnum;
import com.example.checkscam.response.CheckScamResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler({
            IdInvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<CheckScamResponse<Object>> handleAuthExceptions(RuntimeException ex) {
        CheckScamResponse<Object> res = new CheckScamResponse<>(ErrorCodeEnum.INVALID_REQUEST);
        res.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }

    @ExceptionHandler(InvalidCaptchaException.class)
    public ResponseEntity<String> handleInvalidCaptcha(InvalidCaptchaException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CheckScamResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        CheckScamResponse<Object> res = new CheckScamResponse<>(ErrorCodeEnum.BAD_REQUEST);
        res.setMessage(errors.size() > 1 ? errors.toString() : errors.get(0));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CheckScamResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        CheckScamResponse<Object> res = new CheckScamResponse<>(ErrorCodeEnum.BAD_REQUEST);
        res.setMessage(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CheckScamResponse<Object>> handleGenericException(Exception ex) {
        CheckScamResponse<Object> res = new CheckScamResponse<>(ErrorCodeEnum.INTERNAL_ERROR);
        res.setMessage(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(res);
    }
}
