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
@Table(name = "ata_registro_preco")
public class AtaRegistroPreco {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ata_registro_preco")
    @SequenceGenerator(name = "seq_ata_registro_preco", sequenceName = "seq_ata_registro_preco", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_ata_registro_preco")
    private Long idAtaRegistroPreco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orgao", nullable = false)
    private Orgao orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contratada", nullable = false)
    private Contratada contratada;

    @Column(name = "numero_ata", nullable = false, length = 50)
    private String numeroAta;

    @Column(name = "ano_ata", nullable = false)
    private Integer anoAta;

    @Column(name = "objeto", nullable = false, columnDefinition = "TEXT")
    private String objeto;

    @Column(name = "data_assinatura")
    private LocalDate dataAssinatura;

    @Column(name = "data_inicio_vigencia")
    private LocalDate dataInicioVigencia;

    @Column(name = "data_fim_vigencia")
    private LocalDate dataFimVigencia;

    @Column(name = "valor_total", precision = 18, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
