package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contratadas")
public class Contratada {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contratadas")
    @SequenceGenerator(name = "seq_contratadas", sequenceName = "seq_contratadas", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_contratada")
    private Long idContratada;

    @Column(name = "razao_social", nullable = false, length = 255)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 255)
    private String nomeFantasia;

    @Column(name = "cnpj", nullable = false, length = 18)
    private String cnpj;

    @Column(name = "inscricao_estadual", length = 50)
    private String inscricaoEstadual;

    @Column(name = "inscricao_municipal", length = 50)
    private String inscricaoMunicipal;

    @Column(name = "endereco", length = 500)
    private String endereco;

    @Column(name = "telefone", length = 30)
    private String telefone;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "nome_representante", length = 255)
    private String nomeRepresentante;

    @Column(name = "cpf_representante", length = 14)
    private String cpfRepresentante;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
