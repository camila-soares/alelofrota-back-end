package com.plataformaempregos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.plataformaempregos.enums.NivelExperiencia;
import com.plataformaempregos.enums.StatusVaga;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vaga")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vaga {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
    @Column(name = "titulo")
    private String titulo;
    
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "requisitos", columnDefinition = "TEXT")
    private String requisitos;
    
    @Column(name = "beneficios", columnDefinition = "TEXT")
    private String beneficios;
    
    @Column(name = "salario_minimo")
    private Double salarioMinimo;
    
    @Column(name = "salario_maximo")
    private Double salarioMaximo;
    
    @Column(name = "nivel_experiencia")
    @Enumerated(EnumType.STRING)
    private NivelExperiencia nivelExperiencia;
    
    @Column(name = "tipo_contratacao")
    private String tipoContratacao; // CLT, PJ, ESTAGIO, TEMPORARIO
    
    @Column(name = "modalidade_trabalho")
    private String modalidadeTrabalho; // REMOTO, PRESENCIAL, HIBRIDO
    
    @Column(name = "localizacao")
    private String localizacao;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusVaga status;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "vaga_competencia",
        joinColumns = @JoinColumn(name = "vaga_id"),
        inverseJoinColumns = @JoinColumn(name = "competencia_id")
    )
    private List<Competencia> competenciasRequeridas;
    
    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidatura> candidaturas;
    
    @Column(name = "data_publicacao")
    private LocalDateTime dataPublicacao;
    
    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;
    
    @Column(name = "requisitos_ia", columnDefinition = "TEXT")
    private String requisitosIA; // Requisitos processados pela IA para matching
}
