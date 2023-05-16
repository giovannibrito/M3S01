package com.example.demo.services;

import com.example.demo.dtos.VeiculoDto;
import com.example.demo.exceptions.PlacaCadastradaException;
import com.example.demo.exceptions.VeiculoMultadoException;
import com.example.demo.models.Veiculo;
import com.example.demo.repositories.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {
    @Mock
    private VeiculoRepository repository;
    @InjectMocks
    private VeiculoService service;
    private Veiculo veiculo;
    private VeiculoDto veiculoDto;
    @BeforeEach()
    public void setup(){
        System.out.println("Setup executado");
        veiculo = new Veiculo("Placa","Tipo", "Cor", 2000, 0);
        veiculoDto = new VeiculoDto("Placa","Tipo","Cor",2000);

    }
    @Test
    @DisplayName("Quando cadastra um veiculo com placa ainda não cadastrada, deve retornar o veículo criado")
    public void VeiculoServiceTest_1(){
        Mockito.when(repository.findByPlaca(Mockito.anyString())).thenReturn(null);
        Mockito.when(repository.save(veiculo)).thenReturn(veiculo);
        Veiculo veiculoCriado = service.cadastrar(veiculoDto);

        assertNotNull(veiculoCriado);
        assertEquals("Placa",veiculoCriado.getPlaca());
    }
    @Test
    @DisplayName("Quando cadastra um veículo com placa já cadastrada, deve lançar uma exceção")
    void VeiculoServiceTest_2(){
        Mockito.when(repository.findByPlaca(Mockito.anyString())).thenReturn(veiculo);

        assertThrows(PlacaCadastradaException.class,() -> {
            service.cadastrar(veiculoDto);
        });
    }
    @Test
    @DisplayName("Quando não há veículos cadastrados deve retornar lista vazia")
    void VeiculoServiceTest_3(){
        List<Veiculo> lista = service.buscarTodos();

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }
    @Test
    @DisplayName("Quando há veículos cadastrados deve retornar a lista de veículos")
    void VeiculoServiceTest_4(){
        Veiculo veiculo1 = new Veiculo("Placa","Tipo","Cor",2000,0);
        Veiculo veiculo2 = new Veiculo("Placa 2", "Tipo 2", "Cor 2", 2000, 0);

        Mockito.when(repository.findAll()).thenReturn(List.of(veiculo1,veiculo2));
        List<Veiculo> lista = service.buscarTodos();

        assertNotNull(lista);
        assertEquals(2,lista.size());
        assertEquals(veiculo1.getPlaca(), lista.get(0).getPlaca());
    }
    @Test
    @DisplayName("Quando busca veiculo cadastrado deve retornar o veículo")
    void VeiculoServiceTest_5(){
        Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(veiculo));
        Veiculo resultado = service.buscarVeiculoPelaPlaca(veiculoDto.getPlaca());

        assertNotNull(resultado);
        assertEquals(veiculoDto.getPlaca(),resultado.getPlaca());
    }
    @Test
    @DisplayName("Quando busca veiculo não cadastrado deve lançar exceção")
    void VeiculoServiceTest_6(){
        Mockito.when(repository.findById(veiculoDto.getPlaca())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.buscarVeiculoPelaPlaca(veiculoDto.getPlaca());
        });
    }
    @Test
    @DisplayName("Quando tenta excluir veiculo não cadastrado deve lançar exceção")
    void VeiculoServiceTest_7() {
        Mockito.when(repository.findById(veiculoDto.getPlaca())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.excluirVeiculo(veiculoDto.getPlaca());
        });
    }
    @Test
    @DisplayName("Quando tenta excluir veiculo multado deve lançar exceção")
    void VeiculoServiceTest_8() {
        veiculo.setQtdMultas(1);

        Mockito.when(repository.findById(veiculoDto.getPlaca())).thenReturn(Optional.of(veiculo));

        assertThrows(VeiculoMultadoException.class, () -> {
            service.excluirVeiculo(veiculoDto.getPlaca());
        });
    }
    @Test
    @DisplayName("Quando tenta excluir veiculo existente e sem multa, deve excluir")
    void VeiculoServiceTest_9() {
        Mockito.when(repository.findById(veiculoDto.getPlaca())).thenReturn(Optional.of(veiculo));

        assertDoesNotThrow(()-> {
            service.excluirVeiculo(veiculoDto.getPlaca());
        });
    }
    @Test
    @DisplayName("Quando tenta cadastrar multa em veiculo não cadastrado, deve lançar exceção")
    void VeiculoServiceTest_10() {
        Mockito.when(repository.findById(veiculoDto.getPlaca())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.cadastrarMulta(veiculoDto.getPlaca());
        });
    }
    @Test
    @DisplayName("Quando tenta cadastrar multa em veiculo cadastrado, deve cadastrar a multa")
    void VeiculoServiceTest_11() {
        Mockito.when(repository.findById(veiculoDto.getPlaca())).thenReturn(Optional.of(veiculo));

        assertDoesNotThrow(() -> {
            service.cadastrarMulta(veiculoDto.getPlaca());
        });
    }
}