package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "perfil_permissao")
public class PerfilPermissao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_perfil_permissao")
    @SequenceGenerator(name = "seq_perfil_permissao", sequenceName = "seq_perfil_permissao", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_perfil_permissao")
    private Long idPerfilPermissao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_permissao", nullable = false)
    private Permissao permissao;
}
