package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_empenho")
public class ItemEmpenho {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_item_empenho")
    @SequenceGenerator(name = "seq_item_empenho", sequenceName = "seq_item_empenho", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_item_empenho")
    private Long idItemEmpenho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_contrato", nullable = false)
    private ItemContrato itemContrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empenho", nullable = false)
    private Empenho empenho;

    @Column(name = "valor_associado", precision = 18, scale = 2)
    private BigDecimal valorAssociado;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
