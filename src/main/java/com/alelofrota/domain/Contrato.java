package com.alelofrota.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contrato")
public class Contrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.NONE)
	@Column(name = "id")
	private Long id;

	@Column(name = "numero")
	private String numero;

	@Column(name = "ano")
	private Integer ano;

	@Column(name = "local")
	private String local;

	@Column(name = "objeto")
	private String objeto;

	@Column(name = "dotacaoorcamentaria")
	private String dotacaoOrcamentaria;

	@Column(name = "ordeminicio")
	private String ordemInicio;

	@Column(name = "descprazoexec")
	private String descPrazoExec;

	@Column(name = "prazoexecucao")
	private Integer prazoExecucao;

	@Column(name = "dataassinatura")
	private LocalDate dataAssinatura;

	@Column(name = "iniciovigencia")
	private LocalDate inicioVigencia;

	@Column(name = "fimvigencia")
	private LocalDate fimVigencia;

	@Column(name = "datapublicacao")
	private LocalDate dataPublicacao;

	@Column(name = "databaseproposta")
	private LocalDate dataBaseProposta;

	@Column(name = "dou")
	private String dou;

	@Column(name = "pagdou")
	private Integer pagDou;

	@Column(name = "status")
	private String status;

	@Column(name = "dataencerramento")
	private LocalDate dataEncerramento;

	@Column(name = "motivoencerramento")
	private String motivoEncerramento;

	@Column(name = "docrefencerramento")
	private String docRefEncerramento;

	@Column(name = "cnpjfornecedor")
	private String cnpjFornecedor;

	@Column(name = "excluido")
	private Boolean excluido;

	@Column(name = "tipoencerramento")
	private String tipoEncerramento;

	@Column(name = "valorinicial", precision = 19, scale = 2)
	private BigDecimal valorInicial;

	@Column(name = "saldo", precision = 19, scale = 2)
	private BigDecimal saldo;

	@Column(name = "secaodou")
	private String secaoDou;

	@Column(name = "login")
	private String login;

	@Column(name = "fimvigenciaoriginal")
	private LocalDate fimVigenciaOriginal;

	@Column(name = "valor", precision = 19, scale = 2)
	private BigDecimal valor;

	@Column(name = "empenho_contrato")
	private String empenhoContrato;

	@Column(name = "periodicidadelimitecont")
	private String periodicidadeLimiteCont;

	@Column(name = "prazolimitecont")
	private Integer prazoLimiteCont;

	@Column(name = "regrapagamento")
	private String regraPagamento;

	@Column(name = "permitirprorrogacao")
	private Boolean permitirProrrogacao;

	@Column(name = "percentualacrescimo", precision = 19, scale = 4)
	private BigDecimal percentualAcrescimo;

	@Column(name = "valoratualizado", precision = 19, scale = 2)
	private BigDecimal valorAtualizado;

	@Column(name = "descricaoregrapagamentooutros")
	private String descricaoRegraPagamentoOutros;

	@Column(name = "justperiodovigencia")
	private String justPeriodoVigencia;

	@Column(name = "validadecontrato")
	private LocalDate validadeContrato;

	@Column(name = "contrato_favorito")
	private Boolean contratoFavorito;

	@Column(name = "alerta_email")
	private Boolean alertaEmail;

	@Column(name = "interromper_alerta")
	private Boolean interromperAlerta;

	@Column(name = "titulo")
	private String titulo;

	@Column(name = "iniciosuspensao")
	private LocalDate inicioSuspensao;

	@Column(name = "fimsuspensao")
	private LocalDate fimSuspensao;

	@Column(name = "statuscontrato")
	private String statusContrato;

	@Column(name = "dataativacao")
	private LocalDate dataAtivacao;

	@Column(name = "prazoaditivosuspensao")
	private Integer prazoAditivoSuspensao;

	@Column(name = "descprazoaditivosuspensao")
	private String descPrazoAditivoSuspensao;

	@Column(name = "idunidadesga")
	private Long idUnidadeSga;
}

