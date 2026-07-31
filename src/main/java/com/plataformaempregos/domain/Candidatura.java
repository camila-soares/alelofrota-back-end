package com.plataformaempregos.domain;

import java.time.LocalDateTime;

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

import com.plataformaempregos.enums.StatusCandidatura;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "candidatura")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidatura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "candidato_id")
    private Candidato candidato;
    
    @ManyToOne
    @JoinColumn(name = "vaga_id")
    private Vaga vaga;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusCandidatura status;
    
    @Column(name = "score_compatibilidade")
    private Double scoreCompatibilidade; // Score calculado pela IA
    
    @Column(name = "observacoes_ia", columnDefinition = "TEXT")
    private String observacoesIA; // Observações geradas pela IA
    
    @Column(name = "data_candidatura")
    private LocalDateTime dataCandidatura;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "feedback_empresa", columnDefinition = "TEXT")
    private String feedbackEmpresa;
}
