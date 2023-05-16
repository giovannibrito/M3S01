package com.example.demo.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoDto {
    @NotEmpty(message = "O campo placa é obrigatório")
    private String placa;
    @NotEmpty(message = "O campo tipo é obrigatório")
    private String tipo;
    @NotEmpty(message = "O campo cor é obrigatório")
    private String cor;
    @NotNull(message = "O campo anoDeFabricacao é obrigatório")
    private Integer anoDeFabricacao;
}
