package com.plataformaempregos.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.domain.PerfilProfissional;
import com.plataformaempregos.dtos.CandidatoDTO;
import com.plataformaempregos.enums.TipoUsuario;
import com.plataformaempregos.repositories.CandidatoRepository;
import com.plataformaempregos.repositories.UsuarioRepository;

@Service
public class CandidatoService {
    
    @Autowired
    private CandidatoRepository candidatoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private PerfilProfissionalService perfilProfissionalService;
    
    @Transactional
    public Candidato criarCandidato(CandidatoDTO candidatoDTO) {
        if (usuarioRepository.existsByEmail(candidatoDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        if (candidatoDTO.getCpf() != null && candidatoRepository.existsByCpf(candidatoDTO.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        
        Candidato candidato = Candidato.builder()
                .email(candidatoDTO.getEmail())
                .nome(candidatoDTO.getNome())
                .cpf(candidatoDTO.getCpf())
                .dataNascimento(candidatoDTO.getDataNascimento())
                .telefone(candidatoDTO.getTelefone())
                .endereco(candidatoDTO.getEndereco())
                .cidade(candidatoDTO.getCidade())
                .estado(candidatoDTO.getEstado())
                .cep(candidatoDTO.getCep())
                .caminhoCurriculo(candidatoDTO.getCaminhoCurriculo())
                .tipoUsuario(TipoUsuario.CANDIDATO)
                .dataCriacao(LocalDateTime.now())
                .build();
        
        return authService.registrar(candidato);
    }
    
    public Optional<Candidato> obterPorId(Long id) {
        return candidatoRepository.findById(id);
    }
    
    public List<Candidato> listarCandidatosAtivos() {
        return candidatoRepository.findByAtivoTrue();
    }
    
    @Transactional
    public Candidato atualizarCandidato(Long id, CandidatoDTO candidatoDTO) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
        
        candidato.setNome(candidatoDTO.getNome());
        candidato.setTelefone(candidatoDTO.getTelefone());
        candidato.setEndereco(candidatoDTO.getEndereco());
        candidato.setCidade(candidatoDTO.getCidade());
        candidato.setEstado(candidatoDTO.getEstado());
        candidato.setCep(candidatoDTO.getCep());
        candidato.setCaminhoCurriculo(candidatoDTO.getCaminhoCurriculo());
        candidato.setDataAtualizacao(LocalDateTime.now());
        
        return candidatoRepository.save(candidato);
    }
    
    @Transactional
    public Candidato atualizarCurriculo(Long candidatoId, String caminhoCurriculo) {
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
        
        candidato.setCaminhoCurriculo(caminhoCurriculo);
        candidato.setDataAtualizacao(LocalDateTime.now());
        
        return candidatoRepository.save(candidato);
    }
}
