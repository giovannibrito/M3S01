package com.example.demo.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VeiculoDto {
    @NotEmpty(message = "O campo placa é obrigatório")
    private String placa;
    @NotEmpty(message = "O campo tipo é obrigatório")
    private String tipo;
    @NotEmpty(message = "O campo cor é obrigatório")
    private String cor;
    @NotEmpty(message = "O campo anoDeFabricacao é obrigatório")
    private Integer anoDeFabricacao;
}
