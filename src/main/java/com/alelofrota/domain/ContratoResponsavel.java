package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contrato_responsaveis")
public class ContratoResponsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contrato_responsaveis")
    @SequenceGenerator(name = "seq_contrato_responsaveis", sequenceName = "seq_contrato_responsaveis", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_contrato_responsavel")
    private Long idContratoResponsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato contrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_responsabilidade", nullable = false)
    private TipoResponsabilidadeContrato tipoResponsabilidade;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
