-- ============================================================
-- SEQUENCES
-- ============================================================
CREATE SEQUENCE IF NOT EXISTS seq_orgaos START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_contratadas START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_usuarios START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_perfis START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_permissoes START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_tipos_responsabilidade START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_empenhos START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_ata_registro_preco START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_contratos START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_documentos START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_usuario_perfil START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_perfil_permissao START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_condicoes_pagamento START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_itens_contrato START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_instrumentos START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_garantias START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_contrato_responsaveis START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_item_empenho START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_planejamento_mensal_item START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_execucao_mensal_item START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_instrumento_itens START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_instrumento_objetos START WITH 1 INCREMENT BY 1;

-- ============================================================
-- TABLES
-- ============================================================

CREATE TABLE orgaos (
    id_orgao  BIGINT DEFAULT NEXTVAL('seq_orgaos') PRIMARY KEY,
    sigla     VARCHAR(50)  NOT NULL,
    nome      VARCHAR(255) NOT NULL,
    cnpj      VARCHAR(18),
    ativo     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE contratadas (
    id_contratada        BIGINT DEFAULT NEXTVAL('seq_contratadas') PRIMARY KEY,
    razao_social         VARCHAR(255) NOT NULL,
    nome_fantasia        VARCHAR(255),
    cnpj                 VARCHAR(18)  NOT NULL,
    inscricao_estadual   VARCHAR(50),
    inscricao_municipal  VARCHAR(50),
    endereco             VARCHAR(500),
    telefone             VARCHAR(30),
    email                VARCHAR(255),
    nome_representante   VARCHAR(255),
    cpf_representante    VARCHAR(14),
    ativo                BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE usuarios (
    id_usuario    BIGINT DEFAULT NEXTVAL('seq_usuarios') PRIMARY KEY,
    nome_usuario  VARCHAR(255) NOT NULL,
    matricula     VARCHAR(50)  NOT NULL,
    email         VARCHAR(255),
    login         VARCHAR(100) NOT NULL,
    unidade       VARCHAR(255),
    ativo         BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE perfis (
    id_perfil    BIGINT DEFAULT NEXTVAL('seq_perfis') PRIMARY KEY,
    nome_perfil  VARCHAR(100) NOT NULL,
    descricao    VARCHAR(500),
    ativo        BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE permissoes (
    id_permissao    BIGINT DEFAULT NEXTVAL('seq_permissoes') PRIMARY KEY,
    modulo          VARCHAR(100) NOT NULL,
    funcionalidade  VARCHAR(100) NOT NULL,
    descricao       VARCHAR(500)
);

CREATE TABLE tipos_responsabilidade_contrato (
    id_tipo_responsabilidade  BIGINT DEFAULT NEXTVAL('seq_tipos_responsabilidade') PRIMARY KEY,
    nome                      VARCHAR(100) NOT NULL,
    descricao                 VARCHAR(500)
);

CREATE TABLE empenhos (
    id_empenho      BIGINT DEFAULT NEXTVAL('seq_empenhos') PRIMARY KEY,
    id_orgao        BIGINT        NOT NULL,
    numero_empenho  VARCHAR(50)   NOT NULL,
    ano_empenho     INT           NOT NULL,
    data_emissao    DATE,
    valor_empenho   DECIMAL(18,2) NOT NULL,
    observacao      TEXT,
    CONSTRAINT fk_empenhos_orgao FOREIGN KEY (id_orgao) REFERENCES orgaos(id_orgao)
);

CREATE TABLE ata_registro_preco (
    id_ata_registro_preco  BIGINT DEFAULT NEXTVAL('seq_ata_registro_preco') PRIMARY KEY,
    id_orgao               BIGINT        NOT NULL,
    id_contratada          BIGINT        NOT NULL,
    numero_ata             VARCHAR(50)   NOT NULL,
    ano_ata                INT           NOT NULL,
    objeto                 TEXT          NOT NULL,
    data_assinatura        DATE,
    data_inicio_vigencia   DATE,
    data_fim_vigencia      DATE,
    valor_total            DECIMAL(18,2),
    status                 VARCHAR(50),
    observacao             TEXT,
    CONSTRAINT fk_ata_orgao      FOREIGN KEY (id_orgao)      REFERENCES orgaos(id_orgao),
    CONSTRAINT fk_ata_contratada FOREIGN KEY (id_contratada) REFERENCES contratadas(id_contratada)
);

CREATE TABLE contratos (
    id_contrato            BIGINT DEFAULT NEXTVAL('seq_contratos') PRIMARY KEY,
    id_orgao               BIGINT        NOT NULL,
    id_contratada          BIGINT        NOT NULL,
    id_ata_registro_preco  BIGINT,
    numero_contrato        VARCHAR(50)   NOT NULL,
    ano_contrato           INT           NOT NULL,
    objeto                 TEXT          NOT NULL,
    data_assinatura        DATE,
    data_inicio_vigencia   DATE,
    data_fim_vigencia      DATE,
    valor_inicial          DECIMAL(18,2),
    valor_atual            DECIMAL(18,2),
    status                 VARCHAR(50)   NOT NULL,
    observacao             TEXT,
    CONSTRAINT fk_contratos_orgao      FOREIGN KEY (id_orgao)              REFERENCES orgaos(id_orgao),
    CONSTRAINT fk_contratos_contratada FOREIGN KEY (id_contratada)         REFERENCES contratadas(id_contratada),
    CONSTRAINT fk_contratos_ata        FOREIGN KEY (id_ata_registro_preco) REFERENCES ata_registro_preco(id_ata_registro_preco)
);

CREATE TABLE documentos (
    id_documento         BIGINT DEFAULT NEXTVAL('seq_documentos') PRIMARY KEY,
    nome_arquivo         VARCHAR(255) NOT NULL,
    tipo_documento       VARCHAR(100) NOT NULL,
    entidade_referencia  VARCHAR(100) NOT NULL,
    id_referencia        INT          NOT NULL,
    caminho_arquivo      VARCHAR(500),
    url_arquivo          VARCHAR(500),
    hash_arquivo         VARCHAR(255),
    data_upload          TIMESTAMP    NOT NULL,
    id_usuario_upload    BIGINT       NOT NULL,
    descricao            TEXT,
    ativo                BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_documentos_usuario FOREIGN KEY (id_usuario_upload) REFERENCES usuarios(id_usuario)
);

CREATE TABLE usuario_perfil (
    id_usuario_perfil  BIGINT DEFAULT NEXTVAL('seq_usuario_perfil') PRIMARY KEY,
    id_usuario         BIGINT  NOT NULL,
    id_perfil          BIGINT  NOT NULL,
    data_inicio        DATE,
    data_fim           DATE,
    ativo              BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_usuario_perfil_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_usuario_perfil_perfil  FOREIGN KEY (id_perfil)  REFERENCES perfis(id_perfil)
);

CREATE TABLE perfil_permissao (
    id_perfil_permissao  BIGINT DEFAULT NEXTVAL('seq_perfil_permissao') PRIMARY KEY,
    id_perfil            BIGINT NOT NULL,
    id_permissao         BIGINT NOT NULL,
    CONSTRAINT fk_perfil_permissao_perfil    FOREIGN KEY (id_perfil)    REFERENCES perfis(id_perfil),
    CONSTRAINT fk_perfil_permissao_permissao FOREIGN KEY (id_permissao) REFERENCES permissoes(id_permissao)
);

CREATE TABLE condicoes_pagamento (
    id_condicao_pagamento  BIGINT DEFAULT NEXTVAL('seq_condicoes_pagamento') PRIMARY KEY,
    id_contrato            BIGINT       NOT NULL,
    descricao              TEXT         NOT NULL,
    prazo_pagamento_dias   INT,
    forma_pagamento        VARCHAR(100),
    criterio_medicao       VARCHAR(255),
    exige_ateste           BOOLEAN      NOT NULL DEFAULT FALSE,
    observacao             TEXT,
    ativo                  BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_cond_pagamento_contrato FOREIGN KEY (id_contrato) REFERENCES contratos(id_contrato)
);

CREATE TABLE itens_contrato (
    id_item_contrato    BIGINT DEFAULT NEXTVAL('seq_itens_contrato') PRIMARY KEY,
    id_contrato         BIGINT        NOT NULL,
    numero_item         VARCHAR(20),
    descricao           TEXT          NOT NULL,
    unidade_medida      VARCHAR(50),
    quantidade          DECIMAL(18,4),
    valor_unitario      DECIMAL(18,2),
    valor_total_inicial DECIMAL(18,2),
    valor_total_atual   DECIMAL(18,2),
    ativo               BOOLEAN       NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_itens_contrato_contrato FOREIGN KEY (id_contrato) REFERENCES contratos(id_contrato)
);

CREATE TABLE instrumentos (
    id_instrumento      BIGINT DEFAULT NEXTVAL('seq_instrumentos') PRIMARY KEY,
    id_contrato         BIGINT        NOT NULL,
    tipo_instrumento    VARCHAR(100),
    numero_instrumento  VARCHAR(50),
    ano_instrumento     INT,
    data_instrumento    DATE          NOT NULL,
    data_inicio_efeito  DATE,
    data_fim_efeito     DATE,
    valor_informado     DECIMAL(18,2),
    observacao          TEXT,
    CONSTRAINT fk_instrumentos_contrato FOREIGN KEY (id_contrato) REFERENCES contratos(id_contrato)
);

CREATE TABLE garantias (
    id_garantia       BIGINT DEFAULT NEXTVAL('seq_garantias') PRIMARY KEY,
    id_contrato       BIGINT        NOT NULL,
    tipo_garantia     VARCHAR(100)  NOT NULL,
    numero_documento  VARCHAR(100),
    valor_garantia    DECIMAL(18,2),
    data_inicio       DATE,
    data_fim          DATE,
    emissor           VARCHAR(255),
    observacao        TEXT,
    ativo             BOOLEAN       NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_garantias_contrato FOREIGN KEY (id_contrato) REFERENCES contratos(id_contrato)
);

CREATE TABLE contrato_responsaveis (
    id_contrato_responsavel   BIGINT DEFAULT NEXTVAL('seq_contrato_responsaveis') PRIMARY KEY,
    id_contrato               BIGINT  NOT NULL,
    id_usuario                BIGINT  NOT NULL,
    id_tipo_responsabilidade  BIGINT  NOT NULL,
    data_inicio               DATE    NOT NULL,
    data_fim                  DATE,
    ativo                     BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_contrato_resp_contrato FOREIGN KEY (id_contrato)              REFERENCES contratos(id_contrato),
    CONSTRAINT fk_contrato_resp_usuario  FOREIGN KEY (id_usuario)               REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_contrato_resp_tipo     FOREIGN KEY (id_tipo_responsabilidade) REFERENCES tipos_responsabilidade_contrato(id_tipo_responsabilidade)
);

CREATE TABLE item_empenho (
    id_item_empenho   BIGINT DEFAULT NEXTVAL('seq_item_empenho') PRIMARY KEY,
    id_item_contrato  BIGINT        NOT NULL,
    id_empenho        BIGINT        NOT NULL,
    valor_associado   DECIMAL(18,2),
    observacao        TEXT,
    CONSTRAINT fk_item_empenho_item    FOREIGN KEY (id_item_contrato) REFERENCES itens_contrato(id_item_contrato),
    CONSTRAINT fk_item_empenho_empenho FOREIGN KEY (id_empenho)       REFERENCES empenhos(id_empenho)
);

CREATE TABLE planejamento_mensal_item (
    id_planejamento_mensal  BIGINT DEFAULT NEXTVAL('seq_planejamento_mensal_item') PRIMARY KEY,
    id_item_contrato        BIGINT        NOT NULL,
    ano                     INT           NOT NULL,
    mes                     INT           NOT NULL,
    quantidade_planejada    DECIMAL(18,4),
    valor_planejado         DECIMAL(18,2),
    observacao              TEXT,
    CONSTRAINT fk_planejamento_item FOREIGN KEY (id_item_contrato) REFERENCES itens_contrato(id_item_contrato)
);

CREATE TABLE execucao_mensal_item (
    id_execucao_mensal    BIGINT DEFAULT NEXTVAL('seq_execucao_mensal_item') PRIMARY KEY,
    id_item_contrato      BIGINT        NOT NULL,
    ano                   INT           NOT NULL,
    mes                   INT           NOT NULL,
    quantidade_executada  DECIMAL(18,4),
    valor_executado       DECIMAL(18,2),
    origem_valor          VARCHAR(100),
    data_importacao       TIMESTAMP,
    observacao            TEXT,
    CONSTRAINT fk_execucao_item FOREIGN KEY (id_item_contrato) REFERENCES itens_contrato(id_item_contrato)
);

CREATE TABLE instrumento_itens (
    id_instrumento_item  BIGINT DEFAULT NEXTVAL('seq_instrumento_itens') PRIMARY KEY,
    id_instrumento       BIGINT        NOT NULL,
    id_item_contrato     BIGINT        NOT NULL,
    novo_valor_unitario  DECIMAL(18,2),
    nova_quantidade      DECIMAL(18,4),
    novo_valor_total     DECIMAL(18,2),
    observacao           TEXT,
    CONSTRAINT fk_instrumento_itens_instrumento FOREIGN KEY (id_instrumento)   REFERENCES instrumentos(id_instrumento),
    CONSTRAINT fk_instrumento_itens_item        FOREIGN KEY (id_item_contrato) REFERENCES itens_contrato(id_item_contrato)
);

CREATE TABLE instrumento_objetos (
    id_instrumento_objeto  BIGINT DEFAULT NEXTVAL('seq_instrumento_objetos') PRIMARY KEY,
    id_instrumento         BIGINT       NOT NULL,
    tipo_objeto            VARCHAR(100) NOT NULL,
    descricao_objeto       TEXT,
    afeta_valor            BOOLEAN      NOT NULL DEFAULT FALSE,
    afeta_vigencia         BOOLEAN      NOT NULL DEFAULT FALSE,
    afeta_item             BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_instrumento_objetos_instrumento FOREIGN KEY (id_instrumento) REFERENCES instrumentos(id_instrumento)
);

-- ============================================================
-- INDEXES
-- ============================================================

CREATE INDEX idx_empenhos_orgao              ON empenhos(id_orgao);
CREATE INDEX idx_ata_orgao                   ON ata_registro_preco(id_orgao);
CREATE INDEX idx_ata_contratada              ON ata_registro_preco(id_contratada);
CREATE INDEX idx_contratos_orgao             ON contratos(id_orgao);
CREATE INDEX idx_contratos_contratada        ON contratos(id_contratada);
CREATE INDEX idx_contratos_ata               ON contratos(id_ata_registro_preco);
CREATE INDEX idx_contratos_status            ON contratos(status);
CREATE INDEX idx_documentos_usuario          ON documentos(id_usuario_upload);
CREATE INDEX idx_documentos_referencia       ON documentos(entidade_referencia, id_referencia);
CREATE INDEX idx_usuario_perfil_usuario      ON usuario_perfil(id_usuario);
CREATE INDEX idx_usuario_perfil_perfil       ON usuario_perfil(id_perfil);
CREATE INDEX idx_perfil_permissao_perfil     ON perfil_permissao(id_perfil);
CREATE INDEX idx_perfil_permissao_permissao  ON perfil_permissao(id_permissao);
CREATE INDEX idx_cond_pagamento_contrato     ON condicoes_pagamento(id_contrato);
CREATE INDEX idx_itens_contrato_contrato     ON itens_contrato(id_contrato);
CREATE INDEX idx_instrumentos_contrato       ON instrumentos(id_contrato);
CREATE INDEX idx_garantias_contrato          ON garantias(id_contrato);
CREATE INDEX idx_contrato_resp_contrato      ON contrato_responsaveis(id_contrato);
CREATE INDEX idx_contrato_resp_usuario       ON contrato_responsaveis(id_usuario);
CREATE INDEX idx_item_empenho_item           ON item_empenho(id_item_contrato);
CREATE INDEX idx_item_empenho_empenho        ON item_empenho(id_empenho);
CREATE INDEX idx_planejamento_item           ON planejamento_mensal_item(id_item_contrato);
CREATE INDEX idx_planejamento_ano_mes        ON planejamento_mensal_item(ano, mes);
CREATE INDEX idx_execucao_item               ON execucao_mensal_item(id_item_contrato);
CREATE INDEX idx_execucao_ano_mes            ON execucao_mensal_item(ano, mes);
CREATE INDEX idx_instrumento_itens_instr     ON instrumento_itens(id_instrumento);
CREATE INDEX idx_instrumento_itens_item      ON instrumento_itens(id_item_contrato);
CREATE INDEX idx_instrumento_objetos_instr   ON instrumento_objetos(id_instrumento);
