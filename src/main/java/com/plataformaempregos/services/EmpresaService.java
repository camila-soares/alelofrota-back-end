package com.plataformaempregos.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataformaempregos.domain.Empresa;
import com.plataformaempregos.domain.Plano;
import com.plataformaempregos.dtos.EmpresaDTO;
import com.plataformaempregos.enums.TipoPlano;
import com.plataformaempregos.enums.TipoUsuario;
import com.plataformaempregos.repositories.EmpresaRepository;
import com.plataformaempregos.repositories.PlanoRepository;
import com.plataformaempregos.repositories.UsuarioRepository;

@Service
public class EmpresaService {
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private PlanoRepository planoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthService authService;
    
    @Transactional
    public Empresa criarEmpresa(EmpresaDTO empresaDTO) {
        if (usuarioRepository.existsByEmail(empresaDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        if (empresaDTO.getCnpj() != null && empresaRepository.existsByCnpj(empresaDTO.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado");
        }
        
        Empresa empresa = Empresa.builder()
                .email(empresaDTO.getEmail())
                .nome(empresaDTO.getNome())
                .cnpj(empresaDTO.getCnpj())
                .razaoSocial(empresaDTO.getRazaoSocial())
                .nomeFantasia(empresaDTO.getNomeFantasia())
                .telefone(empresaDTO.getTelefone())
                .endereco(empresaDTO.getEndereco())
                .cidade(empresaDTO.getCidade())
                .estado(empresaDTO.getEstado())
                .cep(empresaDTO.getCep())
                .descricao(empresaDTO.getDescricao())
                .tipoUsuario(TipoUsuario.EMPRESA)
                .dataCriacao(LocalDateTime.now())
                .build();
        
        // Definir plano padrão (GRATUITO)
        if (empresaDTO.getPlanoId() == null) {
            Optional<Plano> planoGratuito = planoRepository.findByTipoPlano(TipoPlano.GRATUITO);
            if (planoGratuito.isPresent()) {
                empresa.setPlano(planoGratuito.get());
            }
        } else {
            Optional<Plano> plano = planoRepository.findById(empresaDTO.getPlanoId());
            plano.ifPresent(empresa::setPlano);
        }
        
        empresa.setDataInicioPlano(LocalDateTime.now());
        
        return authService.registrar(empresa);
    }
    
    public Optional<Empresa> obterPorId(Long id) {
        return empresaRepository.findById(id);
    }
    
    public List<Empresa> listarEmpresasAtivas() {
        return empresaRepository.findByAtivoTrue();
    }
    
    @Transactional
    public Empresa atualizarEmpresa(Long id, EmpresaDTO empresaDTO) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        
        empresa.setNome(empresaDTO.getNome());
        empresa.setRazaoSocial(empresaDTO.getRazaoSocial());
        empresa.setNomeFantasia(empresaDTO.getNomeFantasia());
        empresa.setTelefone(empresaDTO.getTelefone());
        empresa.setEndereco(empresaDTO.getEndereco());
        empresa.setCidade(empresaDTO.getCidade());
        empresa.setEstado(empresaDTO.getEstado());
        empresa.setCep(empresaDTO.getCep());
        empresa.setDescricao(empresaDTO.getDescricao());
        empresa.setDataAtualizacao(LocalDateTime.now());
        
        return empresaRepository.save(empresa);
    }
    
    @Transactional
    public void atualizarPlano(Long empresaId, Long planoId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        
        Plano plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        
        empresa.setPlano(plano);
        empresa.setDataInicioPlano(LocalDateTime.now());
        empresaRepository.save(empresa);
    }
}
