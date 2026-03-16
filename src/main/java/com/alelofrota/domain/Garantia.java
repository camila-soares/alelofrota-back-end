package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "garantias")
public class Garantia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_garantias")
    @SequenceGenerator(name = "seq_garantias", sequenceName = "seq_garantias", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_garantia")
    private Long idGarantia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato contrato;

    @Column(name = "tipo_garantia", nullable = false, length = 100)
    private String tipoGarantia;

    @Column(name = "numero_documento", length = 100)
    private String numeroDocumento;

    @Column(name = "valor_garantia", precision = 18, scale = 2)
    private BigDecimal valorGarantia;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "emissor", length = 255)
    private String emissor;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
