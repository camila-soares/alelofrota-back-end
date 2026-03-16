package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "instrumento_objetos")
public class InstrumentoObjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_instrumento_objetos")
    @SequenceGenerator(name = "seq_instrumento_objetos", sequenceName = "seq_instrumento_objetos", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_instrumento_objeto")
    private Long idInstrumentoObjeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instrumento", nullable = false)
    private Instrumento instrumento;

    @Column(name = "tipo_objeto", nullable = false, length = 100)
    private String tipoObjeto;

    @Column(name = "descricao_objeto", columnDefinition = "TEXT")
    private String descricaoObjeto;

    @Column(name = "afeta_valor", nullable = false)
    private Boolean afetaValor;

    @Column(name = "afeta_vigencia", nullable = false)
    private Boolean afetaVigencia;

    @Column(name = "afeta_item", nullable = false)
    private Boolean afetaItem;
}
