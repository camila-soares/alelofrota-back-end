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
@Table(name = "empenhos")
public class Empenho {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_empenhos")
    @SequenceGenerator(name = "seq_empenhos", sequenceName = "seq_empenhos", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_empenho")
    private Long idEmpenho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orgao", nullable = false)
    private Orgao orgao;

    @Column(name = "numero_empenho", nullable = false, length = 50)
    private String numeroEmpenho;

    @Column(name = "ano_empenho", nullable = false)
    private Integer anoEmpenho;

    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    @Column(name = "valor_empenho", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorEmpenho;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
