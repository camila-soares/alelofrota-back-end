package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "planejamento_mensal_item")
public class PlanejamentoMensalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_planejamento_mensal_item")
    @SequenceGenerator(name = "seq_planejamento_mensal_item", sequenceName = "seq_planejamento_mensal_item", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_planejamento_mensal")
    private Long idPlanejamentoMensal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_contrato", nullable = false)
    private ItemContrato itemContrato;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Column(name = "quantidade_planejada", precision = 18, scale = 4)
    private BigDecimal quantidadePlanejada;

    @Column(name = "valor_planejado", precision = 18, scale = 2)
    private BigDecimal valorPlanejado;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
