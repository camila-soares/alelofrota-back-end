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
@Table(name = "instrumentos")
public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_instrumentos")
    @SequenceGenerator(name = "seq_instrumentos", sequenceName = "seq_instrumentos", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_instrumento")
    private Long idInstrumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato contrato;

    @Column(name = "tipo_instrumento", length = 100)
    private String tipoInstrumento;

    @Column(name = "numero_instrumento", length = 50)
    private String numeroInstrumento;

    @Column(name = "ano_instrumento")
    private Integer anoInstrumento;

    @Column(name = "data_instrumento", nullable = false)
    private LocalDate dataInstrumento;

    @Column(name = "data_inicio_efeito")
    private LocalDate dataInicioEfeito;

    @Column(name = "data_fim_efeito")
    private LocalDate dataFimEfeito;

    @Column(name = "valor_informado", precision = 18, scale = 2)
    private BigDecimal valorInformado;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;
}
