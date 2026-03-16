package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario_perfil")
public class UsuarioPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario_perfil")
    @SequenceGenerator(name = "seq_usuario_perfil", sequenceName = "seq_usuario_perfil", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_usuario_perfil")
    private Long idUsuarioPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
