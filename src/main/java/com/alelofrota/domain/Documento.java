package com.alelofrota.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_documentos")
    @SequenceGenerator(name = "seq_documentos", sequenceName = "seq_documentos", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_documento")
    private Long idDocumento;

    @Column(name = "nome_arquivo", nullable = false, length = 255)
    private String nomeArquivo;

    @Column(name = "tipo_documento", nullable = false, length = 100)
    private String tipoDocumento;

    @Column(name = "entidade_referencia", nullable = false, length = 100)
    private String entidadeReferencia;

    @Column(name = "id_referencia", nullable = false)
    private Integer idReferencia;

    @Column(name = "caminho_arquivo", length = 500)
    private String caminhoArquivo;

    @Column(name = "url_arquivo", length = 500)
    private String urlArquivo;

    @Column(name = "hash_arquivo", length = 255)
    private String hashArquivo;

    @Column(name = "data_upload", nullable = false)
    private LocalDateTime dataUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_upload", nullable = false)
    private Usuario usuarioUpload;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
}
