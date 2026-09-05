package com.plataformaempregos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.dtos.DeviceIPAnalysisDTO;
import com.plataformaempregos.services.CandidatoService;
import com.plataformaempregos.services.DiditService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Controller para gerenciar integração com Didit Device & IP Analysis
 * Endpoints para:
 * - Criar sessão de análise
 * - Obter decisão e dados de análise
 * - Gerenciar histórico de análises
 * - Callback de atualização de sessão
 */
@RestController
@RequestMapping("/api/didit")
@Api(tags = "Didit Device & IP Analysis")
public class DiditController {

    @Autowired
    private DiditService diditService;

    @Autowired
    private CandidatoService candidatoService;

    /**
     * Cria uma nova sessão Didit para um candidato
     * O candidato deve acessar a URL retornada para coletar o fingerprint do dispositivo
     *
     * @param candidatoId ID do candidato
     * @return Dados da sessão incluindo URL para coleta de fingerprint
     */
    @PostMapping("/candidatos/{candidatoId}/sessao")
    @ApiOperation("Criar nova sessão Didit para candidato")
    public ResponseEntity<?> criarSessaoCandidato(
            @ApiParam(value = "ID do candidato") @PathVariable Long candidatoId) {
        try {
            Candidato candidato = candidatoService.obterPorId(candidatoId)
                    .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

            DeviceIPAnalysisDTO analysis = diditService.criarSessaoDidit(candidato);

            return ResponseEntity.status(HttpStatus.CREATED).body(analysis);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Obtém a decisão e dados de análise de uma sessão Didit
     * Deve ser chamado após o usuário completar a coleta de dados na sessão
     *
     * @param sessionId ID da sessão Didit
     * @return Dados completos da análise incluindo decisão
     */
    @GetMapping("/sessao/{sessionId}/decisao")
    @ApiOperation("Obter decisão e dados de análise de uma sessão Didit")
    public ResponseEntity<?> obterDecisao(
            @ApiParam(value = "ID da sessão Didit") @PathVariable String sessionId) {
        try {
            DeviceIPAnalysisDTO analysis = diditService.obterDecisaoDidit(sessionId);
            return ResponseEntity.ok(analysis);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Lista todas as análises de um candidato
     *
     * @param candidatoId ID do candidato
     * @return Lista de análises
     */
    @GetMapping("/candidatos/{candidatoId}/analises")
    @ApiOperation("Listar análises de um candidato")
    public ResponseEntity<?> listarAnalisesCandidato(
            @ApiParam(value = "ID do candidato") @PathVariable Long candidatoId) {
        try {
            List<DeviceIPAnalysisDTO> analyses = diditService.listarAnalisesCandidato(candidatoId);
            return ResponseEntity.ok(analyses);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Obtém a análise mais recente não processada de um candidato
     * Útil para fluxos onde você quer verificar se há análise pendente
     *
     * @param candidatoId ID do candidato
     * @return Análise não processada ou 404 se não houver
     */
    @GetMapping("/candidatos/{candidatoId}/analise-pendente")
    @ApiOperation("Obter análise não processada mais recente de um candidato")
    public ResponseEntity<?> obterAnalisePendente(
            @ApiParam(value = "ID do candidato") @PathVariable Long candidatoId) {
        try {
            return diditService.obterAnaliseNaoProcessadaCandidato(candidatoId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Avalia o risco de fraude de uma análise conforme a política configurada
     * Retorna a ação recomendada: APPROVED, MANUAL_REVIEW, DECLINED
     *
     * @param sessionId ID da sessão Didit
     * @return Objeto contendo a ação recomendada e detalhes dos warnings
     */
    @GetMapping("/sessao/{sessionId}/avaliar-risco")
    @ApiOperation("Avaliar risco de fraude de uma análise")
    public ResponseEntity<?> avaliarRiscoFraude(
            @ApiParam(value = "ID da sessão Didit") @PathVariable String sessionId) {
        try {
            return diditService.obterPorSessionId(sessionId)
                    .map(analysis -> {
                        String action = diditService.avaliarRiscoDeFraude(analysis);
                        return ResponseEntity.ok(new RiscoFraudeResponse(action, analysis.getWarnings()));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Callback endpoint que receberá notificações do Didit quando a sessão for atualizada
     * Pode ser chamado para atualizar o status da análise
     *
     * @param sessionId ID da sessão
     * @param status Status atualizado
     * @return Status da operação
     */
    @PostMapping("/callback")
    @ApiOperation("Callback do Didit para atualização de sessão")
    public ResponseEntity<?> handleCallback(
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) String status) {
        try {
            if (sessionId != null) {
                // Aqui você pode processar a atualização de status se necessário
                // Por exemplo, buscar a decisão automaticamente
                return ResponseEntity.ok(new CallbackResponse("Callback recebido", sessionId));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("sessionId é obrigatório"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Verifica se um candidato possui um dispositivo duplicado
     * Útil para detectar múltiplas contas do mesmo dispositivo
     *
     * @param candidatoId ID do candidato
     * @param deviceFingerprintId ID do fingerprint do dispositivo
     * @return Dados da análise anterior ou 404
     */
    @GetMapping("/candidatos/{candidatoId}/dispositivo/{deviceFingerprintId}/duplicado")
    @ApiOperation("Verificar se dispositivo já foi registrado")
    public ResponseEntity<?> verificarDispositivoDuplicado(
            @ApiParam(value = "ID do candidato") @PathVariable Long candidatoId,
            @ApiParam(value = "ID do fingerprint do dispositivo") @PathVariable String deviceFingerprintId) {
        try {
            return diditService.verificarDispositivoDuplicado(candidatoId, deviceFingerprintId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // ========== INNER CLASSES PARA RESPOSTAS ==========

    public static class ErrorResponse {
        public String error;
        public long timestamp;

        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }

        public String getError() {
            return error;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class CallbackResponse {
        public String message;
        public String sessionId;

        public CallbackResponse(String message, String sessionId) {
            this.message = message;
            this.sessionId = sessionId;
        }

        public String getMessage() {
            return message;
        }

        public String getSessionId() {
            return sessionId;
        }
    }

    public static class RiscoFraudeResponse {
        public String action;
        public Object warnings;

        public RiscoFraudeResponse(String action, Object warnings) {
            this.action = action;
            this.warnings = warnings;
        }

        public String getAction() {
            return action;
        }

        public Object getWarnings() {
            return warnings;
        }
    }
}
