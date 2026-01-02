# Plataforma de Empregos com Inteligência Artificial

Sistema SaaS desenvolvido em Spring Boot que utiliza Inteligência Artificial para realizar matching inteligente entre candidatos e vagas de emprego.

## 🎯 Funcionalidades Principais

### Para Empresas
- ✅ Cadastro e gerenciamento de empresas
- ✅ Publicação e gerenciamento de vagas
- ✅ Definição de requisitos e competências necessárias
- ✅ Visualização de candidatos recomendados pela IA
- ✅ Sistema de planos (Gratuito, Básico, Premium, Enterprise)
- ✅ Acompanhamento de candidaturas

### Para Candidatos
- ✅ Cadastro e gerenciamento de perfil profissional
- ✅ Upload de currículo
- ✅ Criação de perfil com experiências e competências
- ✅ Análise automática de perfil pela IA
- ✅ Recomendações personalizadas de vagas compatíveis
- ✅ Acompanhamento do status das candidaturas

### Inteligência Artificial
- ✅ Análise automática de compatibilidade entre candidatos e vagas
- ✅ Cálculo de score de compatibilidade (0-100%)
- ✅ Recomendações inteligentes baseadas em:
  - Competências técnicas e comportamentais
  - Nível de experiência
  - Pretensão salarial
  - Modalidade de trabalho
  - Área de atuação
- ✅ Geração de observações e feedback automatizado

## 🏗️ Arquitetura

### Tecnologias Utilizadas
- **Backend**: Spring Boot 2.1.8
- **Banco de Dados**: H2 (desenvolvimento) / PostgreSQL (produção)
- **Segurança**: Spring Security + JWT
- **Documentação**: Swagger/OpenAPI
- **Build**: Maven
- **Java**: 1.8

### Estrutura do Projeto
```
src/main/java/com/plataformaempregos/
├── config/              # Configurações (Security, Swagger)
├── controllers/         # Controllers REST
├── domain/              # Entidades JPA
├── dtos/                # Data Transfer Objects
├── enums/               # Enumeradores
├── exceptions/          # Tratamento de exceções
├── repositories/        # Repositórios JPA
└── services/            # Lógica de negócio
```

## 📦 Entidades Principais

- **Usuario**: Classe base para autenticação
- **Empresa**: Empresas cadastradas no sistema
- **Candidato**: Candidatos buscando emprego
- **Vaga**: Vagas de emprego publicadas
- **Candidatura**: Relacionamento entre candidato e vaga
- **PerfilProfissional**: Perfil detalhado do candidato
- **ExperienciaProfissional**: Histórico profissional
- **Competencia**: Competências técnicas/comportamentais
- **Plano**: Planos de assinatura SaaS

## 🚀 Como Executar

### Pré-requisitos
- Java 8 ou superior
- Maven 3.6+

### Executando a Aplicação

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### Acessando a Documentação Swagger
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v2/api-docs`

### Console H2 (Desenvolvimento)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:plataformaempregos`
- Username: `sa`
- Password: `sa`

## 📡 Endpoints Principais

### Autenticação
- `POST /api/auth/login` - Realizar login

### Empresas
- `POST /api/empresas` - Cadastrar empresa
- `GET /api/empresas` - Listar empresas
- `GET /api/empresas/{id}` - Obter empresa
- `PUT /api/empresas/{id}` - Atualizar empresa
- `PUT /api/empresas/{id}/plano/{planoId}` - Atualizar plano

### Candidatos
- `POST /api/candidatos` - Cadastrar candidato
- `GET /api/candidatos` - Listar candidatos
- `GET /api/candidatos/{id}` - Obter candidato
- `PUT /api/candidatos/{id}` - Atualizar candidato
- `PUT /api/candidatos/{id}/curriculo` - Atualizar currículo

### Vagas
- `POST /api/vagas/empresa/{empresaId}` - Criar vaga
- `GET /api/vagas` - Listar vagas abertas (paginado)
- `GET /api/vagas/buscar?termo=...` - Buscar vagas
- `GET /api/vagas/{id}` - Obter vaga
- `GET /api/vagas/empresa/{empresaId}` - Listar vagas da empresa
- `PUT /api/vagas/{id}` - Atualizar vaga
- `PUT /api/vagas/{id}/fechar` - Fechar vaga

### Candidaturas
- `POST /api/candidaturas/candidato/{candidatoId}/vaga/{vagaId}` - Criar candidatura
- `GET /api/candidaturas/candidato/{candidatoId}` - Listar candidaturas do candidato
- `GET /api/candidaturas/vaga/{vagaId}` - Listar candidaturas da vaga
- `PUT /api/candidaturas/{id}/status` - Atualizar status

### Inteligência Artificial
- `GET /api/ai/vaga/{vagaId}/candidatos-recomendados` - Candidatos recomendados para vaga
- `GET /api/ai/candidato/{candidatoId}/vagas-recomendadas` - Vagas recomendadas para candidato

### Perfis Profissionais
- `POST /api/perfis-profissionais` - Criar perfil
- `GET /api/perfis-profissionais/{id}` - Obter perfil
- `PUT /api/perfis-profissionais/{id}` - Atualizar perfil

## 🔐 Segurança

O sistema utiliza JWT (JSON Web Tokens) para autenticação. Após o login, o token deve ser enviado no header:
```
Authorization: Bearer {token}
```

## 🤖 Sistema de IA

O serviço de IA (`AIService`) realiza matching inteligente considerando:

1. **Competências** (40% do score)
   - Compara competências requeridas vs. competências do candidato

2. **Nível de Experiência** (20% do score)
   - Verifica compatibilidade do nível profissional

3. **Pretensão Salarial** (15% do score)
   - Compara pretensão com faixa salarial da vaga

4. **Modalidade de Trabalho** (15% do score)
   - Verifica compatibilidade (Remoto/Presencial/Híbrido)

5. **Área de Atuação** (10% do score)
   - Analisa correspondência de área profissional

### Extensibilidade para IA Externa

O sistema está preparado para integração com APIs de IA externas (OpenAI, etc). Configure no `application.properties`:
```properties
ai.enabled=true
ai.api.url=https://api.openai.com/v1
ai.api.key=your-api-key
```

## 💼 Modelo de Negócio SaaS

### Planos Disponíveis
- **Gratuito**: Funcionalidades básicas
- **Básico**: Recursos intermediários
- **Premium**: Análise IA avançada
- **Enterprise**: Recursos completos + suporte prioritário

## 📝 Próximos Passos

- [ ] Implementar upload de arquivos (currículos)
- [ ] Integração com APIs de IA externas (OpenAI, etc)
- [ ] Sistema de notificações
- [ ] Dashboard analítico para empresas
- [ ] API para aplicativos mobile
- [ ] Sistema de pagamento para planos
- [ ] Testes automatizados
- [ ] Deploy em produção

## 📄 Licença

Este projeto está sob a licença MIT.

## 👥 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.
