package com.example.demo.ex01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FolhaDePagamentoTest {

    private FolhaDePagamento folha;

    @BeforeEach()
    public void setup(){
        folha = new FolhaDePagamento();
    }

    @Test
    @DisplayName("Quando calcula salário bruto sem gratificação e sem bonus, deve retornar o próprio salario base")
    public void calculaSalarioBruto_1(){
        Double salario = folha.calcularSalarioBruto(1500D, 0D, "");
        assertEquals(1500D, salario);
    }

    @Test
    @DisplayName("Calcula salário bruto com gratificação e sem bonus, deve retornar o salário somado da gratificação")
    public void calculaSalarioBruto_2(){
        Double salario = folha.calcularSalarioBruto(1500D, 500D, "");
        assertEquals(2000D,salario);
    }

    @Test
    @DisplayName("Calcula salario bruto com gratificação e bonus")
    public void calculaSalarioBruto_3(){
        Double salario = folha.calcularSalarioBruto(1500D,500D,"gerente");
        assertEquals(2600D,salario);
    }

    @Test
    @DisplayName("Calcula salario liquido sem descontos, deve retornar o próprio salário")
    public void calculaSalarioLiquido_1(){
        Double salario = folha.calcularSalarioLiquido(1500D, null);
        assertEquals(1500D, salario);
    }

    @Test
    @DisplayName("Calcula salario liquido com descontos, deve retornar o salario subtraido dos descontos")
    public void calculaSalarioLiquido_2(){
        Double salario = folha.calcularSalarioLiquido(1500D, List.of(200D));
        assertEquals(1300D,salario);
    }

    @Test
    @DisplayName("Calcula salario liquido com descontos maiores que o salario, deve lancar uma excecao")
    public void calculaSalarioLiquido_3(){
        assertThrows(IllegalStateException.class,() -> {folha.calcularSalarioLiquido(1500D, List.of(200D, 1500D));} );
    }
}