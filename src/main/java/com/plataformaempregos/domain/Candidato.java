package com.plataformaempregos.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "candidato")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidato extends Usuario {
    
    @Column(name = "cpf", unique = true)
    private String cpf;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Column(name = "telefone")
    private String telefone;
    
    @Column(name = "endereco")
    private String endereco;
    
    @Column(name = "cidade")
    private String cidade;
    
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "cep")
    private String cep;
    
    @Column(name = "caminho_curriculo")
    private String caminhoCurriculo;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "perfil_profissional_id")
    private PerfilProfissional perfilProfissional;
    
    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidatura> candidaturas;
}
