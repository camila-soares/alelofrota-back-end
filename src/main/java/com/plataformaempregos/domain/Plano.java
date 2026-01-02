package com.plataformaempregos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.plataformaempregos.enums.TipoPlano;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "plano")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plano {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "tipo_plano")
    @Enumerated(EnumType.STRING)
    private TipoPlano tipoPlano;
    
    @Column(name = "valor_mensal")
    private Double valorMensal;
    
    @Column(name = "limite_vagas")
    private Integer limiteVagas;
    
    @Column(name = "limite_candidatos_visualizacao")
    private Integer limiteCandidatosVisualizacao;
    
    @Column(name = "suporte_prioritario")
    private Boolean suportePrioritario;
    
    @Column(name = "analise_ia_avancada")
    private Boolean analiseIAAvancada;
    
    @Column(name = "relatorios_avancados")
    private Boolean relatoriosAvancados;
    
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
}
