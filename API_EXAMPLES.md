# Exemplos de Uso da API

## Autenticação

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "empresa@exemplo.com",
  "senha": "senha123"
}

Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tipo": "Bearer",
  "usuarioId": 1,
  "tipoUsuario": "EMPRESA"
}
```

## Empresas

### Cadastrar Empresa
```bash
POST /api/empresas
Content-Type: application/json

{
  "email": "empresa@exemplo.com",
  "senha": "senha123",
  "nome": "Empresa Exemplo",
  "cnpj": "12.345.678/0001-90",
  "razaoSocial": "Empresa Exemplo LTDA",
  "nomeFantasia": "Exemplo Tech",
  "telefone": "(11) 99999-9999",
  "endereco": "Rua Exemplo, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "descricao": "Empresa de tecnologia"
}
```

## Candidatos

### Cadastrar Candidato
```bash
POST /api/candidatos
Content-Type: application/json

{
  "email": "candidato@exemplo.com",
  "senha": "senha123",
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "dataNascimento": "1990-01-15",
  "telefone": "(11) 98888-8888",
  "endereco": "Av. Candidato, 456",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567"
}
```

### Criar Perfil Profissional
```bash
POST /api/perfis-profissionais
Content-Type: application/json

{
  "resumoProfissional": "Desenvolvedor Java com 5 anos de experiência",
  "areaAtuacao": "Desenvolvimento de Software",
  "pretensaoSalarial": 8000.00,
  "disponibilidade": "REMOTO",
  "competenciasIds": [1, 2, 3],
  "experiencias": [
    {
      "empresa": "Tech Corp",
      "cargo": "Desenvolvedor Java",
      "nivelExperiencia": "PLENO",
      "dataInicio": "2019-01-01",
      "dataFim": null,
      "empregoAtual": true,
      "descricao": "Desenvolvimento de aplicações Spring Boot"
    }
  ]
}
```

## Vagas

### Criar Vaga
```bash
POST /api/vagas/empresa/1
Content-Type: application/json

{
  "titulo": "Desenvolvedor Java Senior",
  "descricao": "Vaga para desenvolvedor Java com experiência em Spring Boot",
  "requisitos": "5+ anos de experiência, conhecimento em Spring Boot, REST APIs",
  "beneficios": "Vale refeição, plano de saúde, home office",
  "salarioMinimo": 8000.00,
  "salarioMaximo": 12000.00,
  "nivelExperiencia": "SENIOR",
  "tipoContratacao": "CLT",
  "modalidadeTrabalho": "REMOTO",
  "localizacao": "São Paulo - SP",
  "competenciasRequeridasIds": [1, 2, 3, 4]
}
```

### Buscar Vagas
```bash
GET /api/vagas/buscar?termo=java&page=0&size=10
```

## Candidaturas

### Candidatar-se a uma Vaga
```bash
POST /api/candidaturas/candidato/1/vaga/1

Response:
{
  "id": 1,
  "candidatoId": 1,
  "candidatoNome": "João Silva",
  "vagaId": 1,
  "vagaTitulo": "Desenvolvedor Java Senior",
  "status": "PENDENTE",
  "scoreCompatibilidade": 85.5,
  "observacoesIA": "Score de compatibilidade: 85.50%\n\nCompetências compatíveis: Java, Spring Boot\n...",
  "dataCandidatura": "2024-01-15T10:30:00"
}
```

### Listar Candidaturas do Candidato
```bash
GET /api/candidaturas/candidato/1
```

### Listar Candidaturas da Vaga
```bash
GET /api/candidaturas/vaga/1
```

### Atualizar Status da Candidatura
```bash
PUT /api/candidaturas/1/status
Content-Type: application/json

{
  "status": "APROVADA",
  "feedback": "Candidato aprovado para próxima etapa"
}
```

## Inteligência Artificial

### Buscar Candidatos Recomendados para uma Vaga
```bash
GET /api/ai/vaga/1/candidatos-recomendados?limite=10

Response:
[
  {
    "candidatoId": 1,
    "candidatoNome": "João Silva",
    "candidatoEmail": "joao@exemplo.com",
    "scoreCompatibilidade": 92.5,
    "observacoesIA": "Score de compatibilidade: 92.50%\n\nCompetências compatíveis: Java, Spring Boot, REST APIs\n..."
  },
  ...
]
```

### Buscar Vagas Recomendadas para um Candidato
```bash
GET /api/ai/candidato/1/vagas-recomendadas?limite=10

Response:
[
  {
    "vagaId": 1,
    "vagaTitulo": "Desenvolvedor Java Senior",
    "empresaNome": "Exemplo Tech",
    "scoreCompatibilidade": 92.5,
    "observacoesIA": "Score de compatibilidade: 92.50%\n\n..."
  },
  ...
]
```

## Competências

### Criar Competência (exemplo via código)
```java
Competencia competencia = Competencia.builder()
    .nome("Java")
    .categoria("TECNICA")
    .descricao("Linguagem de programação Java")
    .build();
```

## Planos

### Listar Planos Disponíveis
```bash
GET /api/planos
```

### Atualizar Plano da Empresa
```bash
PUT /api/empresas/1/plano/2
```
