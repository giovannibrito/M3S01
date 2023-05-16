package com.example.demo.controllers;

import com.example.demo.dtos.VeiculoDto;
import com.example.demo.exceptions.PlacaCadastradaException;
import com.example.demo.exceptions.VeiculoMultadoException;
import com.example.demo.models.Veiculo;
import com.example.demo.services.VeiculoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest
class VeiculoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private VeiculoService service;
    private final ModelMapper modelMapper = new ModelMapper();
    private Veiculo veiculo;
    private String requestBody;
    @BeforeEach()
    public void setup() throws JsonProcessingException {
        veiculo = new Veiculo("Placa","Tipo","Cor",2000,0);
        VeiculoDto veiculoDto = new VeiculoDto("Placa", "Tipo", "Cor", 2000);
        requestBody = objectMapper.writeValueAsString(veiculoDto);
    }
    @Test
    @DisplayName("Quando tenta cadastrar veiculo não cadastrado, deve cadastrar corretamente")
    void VeiculoControllerTest_1() throws Exception {
        Mockito.when(service.cadastrar(Mockito.any(VeiculoDto.class))).thenReturn(veiculo);

        mockMvc.perform(post("/api/veiculos")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.placa",is("Placa")));
    }
    @Test
    @DisplayName("Quando tenta cadastrar veiculo já cadastrado, deve retornar erro")
    void VeiculoControllerTest_2() throws Exception {
        Mockito.when(service.cadastrar(Mockito.any(VeiculoDto.class))).thenThrow(PlacaCadastradaException.class);

        mockMvc.perform(post("/api/veiculos")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
    @Test
    @DisplayName("Quando há veiculos cadastrados, deve retornar a lista de veículos")
    void VeiculoControllerTest_3() throws Exception {
        Veiculo veiculo2 = new Veiculo("Placa 2", "Tipo 2", "Cor 2", 2000, 0);

        Mockito.when(service.buscarTodos()).thenReturn(
                List.of(veiculo,veiculo2)
        );

        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].placa",is("Placa")))
                .andExpect(jsonPath("$[1].placa",is("Placa 2")));
    }
    @Test
    @DisplayName("Quando não há veiculos cadastrados, deve retornar a lista vazia")
    void VeiculoControllerTest_4() throws Exception {
        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$",is(empty())));
    }
    @Test
    @DisplayName("Quando busca veículo com placa cadastrada, deve retornar o veículo")
    void VeiculoControllerTest_5() throws Exception {
        Mockito.when(service.buscarVeiculoPelaPlaca(Mockito.anyString())).thenReturn(veiculo);

        mockMvc.perform(get("/api/veiculos/{id}","Placa"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.placa",is("Placa")))
                .andExpect(jsonPath("$.cor",is("Cor")));
    }
    @Test
    @DisplayName("Quando busca veículo com placa não cadastrada, deve retornar erro")
    void VeiculoControllerTest_6() throws Exception {
        Mockito.when(service.buscarVeiculoPelaPlaca(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/veiculos/{id}","Placa")).andExpect(status().is(404));
    }
    @Test
    @DisplayName("Quando tenta excluir veiculo cadastrado e sem multas, deve excluir com sucesso")
    void VeiculoControllerTest_7() throws Exception {
        mockMvc.perform(delete("/api/veiculos/{id}","Placa")).andExpect(status().isNoContent());
    }
    @Test
    @DisplayName("Quando tenta excluir veiculo cadastrado e com multas, deve retornar erro")
    void VeiculoControllerTest_8() throws Exception {
        Mockito.doThrow(VeiculoMultadoException.class).when(service).excluirVeiculo(Mockito.anyString());

        mockMvc.perform(delete("/api/veiculos/{id}","Placa")).andExpect(status().is(400));
    }
    @Test
    @DisplayName("Quando tenta excluir veiculo não cadastrado, deve retornar erro")
    void VeiculoControllerTest_9() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(service).excluirVeiculo(Mockito.anyString());

        mockMvc.perform(delete("/api/veiculos/{id}","Placa")).andExpect(status().is(404));
    }
    @Test
    @DisplayName("Quando tenta cadastrar multa para veículo cadastrado, deve cadastrar com sucesso")
    void VeiculoControllerTest_10() throws Exception {
        Veiculo veiculo2 = new Veiculo("Placa", "Tipo", "Cor", 2000, 1);

        Mockito.when(service.cadastrarMulta(Mockito.anyString())).thenReturn(veiculo2);

        mockMvc.perform(put("/api/veiculos/{id}/multas","Placa"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.placa",is("Placa")))
                .andExpect(jsonPath("$.qtdMultas",is(1)));
    }
    @Test
    @DisplayName("Quando tenta cadastrar multa para veículo não cadastrado, deve retornar erro")
    void VeiculoControllerTest_11() throws Exception {
        Mockito.when(service.cadastrarMulta(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/veiculos/{id}/multas","Placa"))
                .andExpect(status().is(404));
    }
}