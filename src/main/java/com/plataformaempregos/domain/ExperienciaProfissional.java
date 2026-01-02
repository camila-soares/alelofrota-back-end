package com.plataformaempregos.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.plataformaempregos.enums.NivelExperiencia;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "experiencia_profissional")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienciaProfissional {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "perfil_profissional_id")
    private PerfilProfissional perfilProfissional;
    
    @Column(name = "empresa")
    private String empresa;
    
    @Column(name = "cargo")
    private String cargo;
    
    @Column(name = "nivel_experiencia")
    @Enumerated(EnumType.STRING)
    private NivelExperiencia nivelExperiencia;
    
    @Column(name = "data_inicio")
    private LocalDate dataInicio;
    
    @Column(name = "data_fim")
    private LocalDate dataFim;
    
    @Column(name = "emprego_atual")
    private Boolean empregoAtual;
    
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
}
