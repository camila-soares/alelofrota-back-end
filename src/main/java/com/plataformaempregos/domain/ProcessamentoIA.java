package com.plataformaempregos.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.plataformaempregos.enums.StatusProcessamentoIA;
import com.plataformaempregos.enums.TipoProcessamentoIA;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "processamento_ia")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessamentoIA {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "tipo_processamento")
    @Enumerated(EnumType.STRING)
    private TipoProcessamentoIA tipoProcessamento;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusProcessamentoIA status;
    
    @Column(name = "entidade_id")
    private Long entidadeId; // ID da entidade relacionada (Candidatura, Vaga, Candidato, etc)
    
    @Column(name = "entidade_tipo")
    private String entidadeTipo; // Tipo da entidade (CANDIDATURA, VAGA, CANDIDATO, etc)
    
    @Column(name = "resultado", columnDefinition = "TEXT")
    private String resultado; // JSON com resultado do processamento
    
    @Column(name = "score_calculado")
    private Double scoreCalculado;
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "erro", columnDefinition = "TEXT")
    private String erro;
    
    @Column(name = "progresso")
    private Integer progresso; // 0-100
    
    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;
    
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
    
    @Column(name = "tempo_processamento_ms")
    private Long tempoProcessamentoMs;
    
    @Column(name = "usuario_id")
    private Long usuarioId; // Usuário que solicitou o processamento
}
