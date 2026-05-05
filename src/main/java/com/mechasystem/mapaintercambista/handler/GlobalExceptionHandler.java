package com.mechasystem.mapaintercambista.handler;

import com.mechasystem.mapaintercambista.exception.ConflictException;
import com.mechasystem.mapaintercambista.exception.CurtidaNegativaException;
import com.mechasystem.mapaintercambista.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound (NotFoundException ex, HttpServletRequest r) {
        ErrorResponse err = mapperErrorResponse(
                404,
                "Não encontrado",
                ex.getMessage(),
                r.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict (ConflictException ex, HttpServletRequest r) {
        ErrorResponse err = mapperErrorResponse(
                409,
                "Conflito no banco",
                ex.getMessage(),
                r.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(CurtidaNegativaException.class)
    public ResponseEntity<ErrorResponse> handleCurtidaNegativa(CurtidaNegativaException ex, HttpServletRequest r) {
        ErrorResponse err = mapperErrorResponse(
                409,
                "Impossível descurtir esse post",
                ex.getMessage(),
                r.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    private ErrorResponse mapperErrorResponse (int status, String error, String massage, String path) {
        return new ErrorResponse(
                status,
                error,
                massage,
                path,
                LocalDateTime.now()
        );
    }
}
