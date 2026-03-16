package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orgaos")
public class Orgao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_orgaos")
    @SequenceGenerator(name = "seq_orgaos", sequenceName = "seq_orgaos", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_orgao")
    private Long idOrgao;

    @Column(name = "sigla", nullable = false, length = 50)
    private String sigla;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "cnpj", length = 18)
    private String cnpj;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
