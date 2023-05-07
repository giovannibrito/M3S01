package com.example.demo.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> veiculoNaoEncontrado(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(VeiculoMultadoException.class)
    public ResponseEntity<String> veiculoMultado(){
        return ResponseEntity.badRequest().body("Não pode excluir veículo com multas cadastradas");
    }

    @ExceptionHandler(PlacaCadastradaException.class)
    public ResponseEntity<String> placaCadastrada(){
        return ResponseEntity.badRequest().body("Placa já cadastrada no sistema");
    }
}
