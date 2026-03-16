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
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contratos")
    @SequenceGenerator(name = "seq_contratos", sequenceName = "seq_contratos", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_contrato")
    private Long idContrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orgao", nullable = false)
    private Orgao orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contratada", nullable = false)
    private Contratada contratada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ata_registro_preco")
    private AtaRegistroPreco ataRegistroPreco;

    @Column(name = "numero_contrato", nullable = false, length = 50)
    private String numeroContrato;

    @Column(name = "ano_contrato", nullable = false)
    private Integer anoContrato;

    @Column(name = "objeto", nullable = false, columnDefinition = "TEXT")
    private String objeto;

    @Column(name = "data_assinatura")
    private LocalDate dataAssinatura;

    @Column(name = "data_inicio_vigencia")
    private LocalDate dataInicioVigencia;

    @Column(name = "data_fim_vigencia")
    private LocalDate dataFimVigencia;

    @Column(name = "valor_inicial", precision = 18, scale = 2)
    private BigDecimal valorInicial;

    @Column(name = "valor_atual", precision = 18, scale = 2)
    private BigDecimal valorAtual;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
