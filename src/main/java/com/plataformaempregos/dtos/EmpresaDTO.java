package com.plataformaempregos.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    private Long id;
    private String email;
    private String nome;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String telefone;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private String descricao;
    private Long planoId;
    private String tipoPlano;
    private LocalDateTime dataInicioPlano;
    private LocalDateTime dataFimPlano;
}
