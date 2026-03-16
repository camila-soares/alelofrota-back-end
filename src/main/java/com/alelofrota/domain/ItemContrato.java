package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "itens_contrato")
public class ItemContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_itens_contrato")
    @SequenceGenerator(name = "seq_itens_contrato", sequenceName = "seq_itens_contrato", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_item_contrato")
    private Long idItemContrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato contrato;

    @Column(name = "numero_item", length = 20)
    private String numeroItem;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "unidade_medida", length = 50)
    private String unidadeMedida;

    @Column(name = "quantidade", precision = 18, scale = 4)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario", precision = 18, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total_inicial", precision = 18, scale = 2)
    private BigDecimal valorTotalInicial;

    @Column(name = "valor_total_atual", precision = 18, scale = 2)
    private BigDecimal valorTotalAtual;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
