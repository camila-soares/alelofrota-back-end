package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuarios")
    @SequenceGenerator(name = "seq_usuarios", sequenceName = "seq_usuarios", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nome_usuario", nullable = false, length = 255)
    private String nomeUsuario;

    @Column(name = "matricula", nullable = false, length = 50)
    private String matricula;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "login", nullable = false, length = 100)
    private String login;

    @Column(name = "unidade", length = 255)
    private String unidade;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
