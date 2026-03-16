package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permissoes")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_permissoes")
    @SequenceGenerator(name = "seq_permissoes", sequenceName = "seq_permissoes", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_permissao")
    private Long idPermissao;

    @Column(name = "modulo", nullable = false, length = 100)
    private String modulo;

    @Column(name = "funcionalidade", nullable = false, length = 100)
    private String funcionalidade;

    @Column(name = "descricao", length = 500)
    private String descricao;
}
