package edu.dosw.parcial.controller.handlers;

import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.core.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(error(400, "Datos inválidos: " + mensaje));
    }

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<ErrorResponse> handleDatosInvalidos(DatosInvalidosException ex) {
        return ResponseEntity.badRequest().body(error(400, ex.getMessage()));
    }

    @ExceptionHandler(CorreoYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleCorreoExiste(CorreoYaExisteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error(409, ex.getMessage()));
    }

    @ExceptionHandler(PedidoActivoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoActivo(PedidoActivoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error(409, ex.getMessage()));
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleStockInsuficiente(StockInsuficienteException ex) {
        return ResponseEntity.badRequest().body(error(400, ex.getMessage()));
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(404, ex.getMessage()));
    }

    private ErrorResponse error(int codigo, String mensaje) {
        return ErrorResponse.builder().codigo(codigo).mensaje(mensaje).build();
    }
}
