package com.example.demo.controllers;

import com.example.demo.dtos.VeiculoDto;
import com.example.demo.models.Veiculo;
import com.example.demo.services.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/veiculos")
@RestController
public class VeiculoController {
    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @PostMapping()
    public Veiculo cadastrarVeiculo(@Valid @RequestBody VeiculoDto request){
        return service.cadastrar(request);
    }

    @GetMapping()
    public List<Veiculo> buscarTodosVeiculos(){
        return service.buscarTodos();
    }

    @GetMapping("/{placa}")
    public Veiculo buscarVeiculoPelaPlaca(@PathVariable String placa){
        return service.buscarVeiculoPelaPlaca(placa);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{placa}")
    public void excluirVeiculo(@PathVariable String placa){
        service.excluirVeiculo(placa);
    }

    @PutMapping("/{placa}/multas")
    public Veiculo adicionarMulta(@PathVariable String placa){
        return service.cadastrarMulta(placa);
    }
}
