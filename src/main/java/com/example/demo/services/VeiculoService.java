package com.example.demo.services;

import com.example.demo.dtos.VeiculoDto;
import com.example.demo.exceptions.PlacaCadastradaException;
import com.example.demo.exceptions.VeiculoMultadoException;
import com.example.demo.models.Veiculo;
import com.example.demo.repositories.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {
    private final VeiculoRepository repository;
    private final ModelMapper mapper = new ModelMapper();

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public Veiculo cadastrar(VeiculoDto request) {
        if (repository.findByPlaca(request.getPlaca()) != null) {
            throw new PlacaCadastradaException();
        }
        Veiculo veiculo = mapper.map(request, Veiculo.class);
        return repository.save(veiculo);
    }

    public List<Veiculo> buscarTodos() {
        return repository.findAll();
    }

    public Veiculo buscarVeiculoPelaPlaca(String placa) {
        return repository.findById(placa).orElseThrow(EntityNotFoundException::new);
    }

    public void excluirVeiculo(String placa){
        Veiculo veiculo = repository.findById(placa).orElseThrow(EntityNotFoundException::new);
        if (veiculo.getQtdMultas() > 0) {
            throw new VeiculoMultadoException();
        }
        repository.delete(veiculo);
    }

    public Veiculo cadastrarMulta(String placa) {
        Veiculo veiculo = repository.findById(placa).orElseThrow(EntityNotFoundException::new);
        veiculo.setQtdMultas(veiculo.getQtdMultas() + 1);
        return repository.save(veiculo);
    }
}
