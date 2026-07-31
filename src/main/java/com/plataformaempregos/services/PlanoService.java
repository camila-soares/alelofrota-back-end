package com.plataformaempregos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataformaempregos.domain.Plano;
import com.plataformaempregos.repositories.PlanoRepository;

@Service
public class PlanoService {
    
    @Autowired
    private PlanoRepository planoRepository;
    
    public List<Plano> listarTodos() {
        return planoRepository.findAll();
    }
    
    public Optional<Plano> obterPorId(Long id) {
        return planoRepository.findById(id);
    }
    
    public Plano criarPlano(Plano plano) {
        return planoRepository.save(plano);
    }
}
