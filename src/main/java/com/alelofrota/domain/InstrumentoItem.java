package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "instrumento_itens")
public class InstrumentoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_instrumento_itens")
    @SequenceGenerator(name = "seq_instrumento_itens", sequenceName = "seq_instrumento_itens", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_instrumento_item")
    private Long idInstrumentoItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instrumento", nullable = false)
    private Instrumento instrumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_contrato", nullable = false)
    private ItemContrato itemContrato;

    @Column(name = "novo_valor_unitario", precision = 18, scale = 2)
    private BigDecimal novoValorUnitario;

    @Column(name = "nova_quantidade", precision = 18, scale = 4)
    private BigDecimal novaQuantidade;

    @Column(name = "novo_valor_total", precision = 18, scale = 2)
    private BigDecimal novoValorTotal;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
