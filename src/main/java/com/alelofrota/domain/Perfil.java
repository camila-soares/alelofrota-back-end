package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "perfis")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_perfis")
    @SequenceGenerator(name = "seq_perfis", sequenceName = "seq_perfis", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_perfil")
    private Long idPerfil;

    @Column(name = "nome_perfil", nullable = false, length = 100)
    private String nomePerfil;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
