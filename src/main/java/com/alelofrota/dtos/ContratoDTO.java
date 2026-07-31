package com.alelofrota.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContratoDTO {

	private Long id;

	private String numero;
	private Integer ano;
	private String local;
	private String objeto;
	private String dotacaoOrcamentaria;
	private String ordemInicio;
	private String descPrazoExec;
	private Integer prazoExecucao;
	private LocalDate dataAssinatura;
	private LocalDate inicioVigencia;
	private LocalDate fimVigencia;
	private LocalDate dataPublicacao;
	private LocalDate dataBaseProposta;
	private String dou;
	private Integer pagDou;
	private String status;
	private LocalDate dataEncerramento;
	private String motivoEncerramento;
	private String docRefEncerramento;
	private String cnpjFornecedor;
	private Boolean excluido;
	private String tipoEncerramento;
	private BigDecimal valorInicial;
	private BigDecimal saldo;
	private String secaoDou;
	private String login;
	private LocalDate fimVigenciaOriginal;
	private BigDecimal valor;
	private String empenhoContrato;
	private String periodicidadeLimiteCont;
	private Integer prazoLimiteCont;
	private String regraPagamento;
	private Boolean permitirProrrogacao;
	private BigDecimal percentualAcrescimo;
	private BigDecimal valorAtualizado;
	private String descricaoRegraPagamentoOutros;
	private String justPeriodoVigencia;
	private LocalDate validadeContrato;
	private Boolean contratoFavorito;
	private Boolean alertaEmail;
	private Boolean interromperAlerta;
	private String titulo;
	private LocalDate inicioSuspensao;
	private LocalDate fimSuspensao;
	private String statusContrato;
	private LocalDate dataAtivacao;
	private Integer prazoAditivoSuspensao;
	private String descPrazoAditivoSuspensao;
	private Long idUnidadeSga;
}

