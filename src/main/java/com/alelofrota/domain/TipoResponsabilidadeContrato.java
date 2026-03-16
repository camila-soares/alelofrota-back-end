package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipos_responsabilidade_contrato")
public class TipoResponsabilidadeContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tipos_responsabilidade")
    @SequenceGenerator(name = "seq_tipos_responsabilidade", sequenceName = "seq_tipos_responsabilidade", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_tipo_responsabilidade")
    private Long idTipoResponsabilidade;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;
}
