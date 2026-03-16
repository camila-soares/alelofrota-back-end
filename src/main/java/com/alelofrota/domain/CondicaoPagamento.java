package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "condicoes_pagamento")
public class CondicaoPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_condicoes_pagamento")
    @SequenceGenerator(name = "seq_condicoes_pagamento", sequenceName = "seq_condicoes_pagamento", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_condicao_pagamento")
    private Long idCondicaoPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato contrato;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "prazo_pagamento_dias")
    private Integer prazoPagamentoDias;

    @Column(name = "forma_pagamento", length = 100)
    private String formaPagamento;

    @Column(name = "criterio_medicao", length = 255)
    private String criterioMedicao;

    @Column(name = "exige_ateste", nullable = false)
    private Boolean exigeAteste;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
