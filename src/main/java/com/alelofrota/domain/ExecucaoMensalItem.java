package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "execucao_mensal_item")
public class ExecucaoMensalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_execucao_mensal_item")
    @SequenceGenerator(name = "seq_execucao_mensal_item", sequenceName = "seq_execucao_mensal_item", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_execucao_mensal")
    private Long idExecucaoMensal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_contrato", nullable = false)
    private ItemContrato itemContrato;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Column(name = "quantidade_executada", precision = 18, scale = 4)
    private BigDecimal quantidadeExecutada;

    @Column(name = "valor_executado", precision = 18, scale = 2)
    private BigDecimal valorExecutado;

    @Column(name = "origem_valor", length = 100)
    private String origemValor;

    @Column(name = "data_importacao")
    private LocalDateTime dataImportacao;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
