EVENTS API — Serviço de Eventos (Spring Boot + PostgreSQL + JWT)

Este microserviço é responsável por gerenciar eventos, inscrições e check-ins.
Faz parte do ecossistema de microserviços do Sistema de Eventos, integrando-se com a AUTH API para autenticação via JWT.

1. Objetivo

Gerenciar os fluxos principais do sistema de eventos:

Listagem pública de eventos

Inscrição de usuários

Cancelamento de inscrições

Registro de presença via Check-in

Integração com AUTH API

Segurança via JWT

2. Tecnologias Utilizadas
   Tecnologia	Finalidade
   Java 21	Linguagem
   Spring Boot	Backend REST
   Spring Security	Autenticação/Autorização
   Hibernate JPA	ORM
   PostgreSQL	Banco de dados
   JWT (jjwt)	Validação de token
   Lombok	Reduz boilerplate
   Swagger	Documentação
3. Estrutura do Projeto
   src/main/java/com/sistema/eventsapi
   ├─ controller
   │   ├─ EventoController
   │   ├─ InscricaoController
   │   └─ CheckinController
   │
   ├─ dto
   │   ├─ EventoCriarRequisicao
   │   ├─ EventoAtualizarRequisicao
   │   ├─ EventoResposta
   │   ├─ InscricaoRequisicao
   │   ├─ InscricaoResposta
   │   ├─ CheckinRequest
   │   └─ CheckinResponse
   │
   ├─ entity
   │   ├─ Evento
   │   ├─ Inscricao
   │   └─ Presenca
   │
   ├─ exception
   │   ├─ ApiException
   │   ├─ ErroResposta
   │   └─ GlobalExceptionHandler
   │
   ├─ repository
   │   ├─ EventoRepository
   │   ├─ InscricaoRepository
   │   └─ PresencaRepository
   │
   ├─ security
   │   ├─ JwtFiltro
   │   ├─ JwtServico
   │   └─ SegurancaConfig
   │
   ├─ service
   │   ├─ EventoService
   │   ├─ InscricaoService
   │   ├─ CheckinService
   │   └─ impl
   │       ├─ EventoServiceImpl
   │       ├─ InscricaoServiceImpl
   │       └─ CheckinServiceImpl
   │
   └─ EventsApiJavaApplication

4. Autenticação

A EVENTS API não gera tokens.
Ela valida tokens JWT emitidos pela AUTH API.

Exemplo de header:

Authorization: Bearer <TOKEN>


📌 Rotas públicas

GET /eventos

GET /eventos/{id}

📌 Todas as outras rotas exigem JWT.

5. Endpoints
   📌 5.1 Listar eventos (PÚBLICO)

GET /eventos

Resposta:

[
{
"id": 1,
"titulo": "Evento Teste",
"descricao": "Sistema de eventos",
"dataEvento": "2025-01-10"
}
]

📌 5.2 Buscar evento (PÚBLICO)

GET /eventos/{id}

📌 5.3 Criar evento (PROTEGIDO)

POST /eventos

Authorization: Bearer <TOKEN>


Body:

{
"titulo": "Evento Front-End",
"descricao": "Workshop React",
"dataEvento": "2025-01-20"
}

📌 5.4 Atualizar evento (PROTEGIDO)

PUT /eventos/{id}

📌 5.5 Excluir evento (PROTEGIDO)

DELETE /eventos/{id}

📌 5.6 Inscrever (PROTEGIDO)

POST /inscricoes

Body:

{
"eventoId": 1
}

📌 5.7 Cancelar Inscrição (PROTEGIDO)

DELETE /inscricoes/{id}

📌 5.8 Listar inscrições por evento (PROTEGIDO)

GET /inscricoes/evento/{idEvento}

📌 5.9 Registrar check-in (PROTEGIDO)

POST /checkin

{
"eventoId": 1,
"inscricaoId": 12,
"offline": false
}

6. Segurança

Stateless

Sessões desativadas

Cookies não utilizados

JWT validado via JwtFiltro

Usuário autenticado extraído do token

7. Banco de Dados
   📌 Tabelas
   eventos
   id PK
   titulo
   descricao
   data_evento


<small>Eventos são inseridos direto no banco no MVP</small>

inscricoes
id PK
evento_id FK
usuario_id
status (INSCRITO / CANCELADO / PRESENTE)

presencas
id PK
evento_id
usuario_id
offline boolean

8. application.properties
   spring.application.name=events-api

spring.datasource.url=jdbc:postgresql://localhost:5432/eventsdb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8082

jwt.secret=super_secret_api_auth_2025_1234567890
jwt.expiration=3600000

springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs

9. Swagger — Documentação da API

👉 Interface

http://localhost:8082/swagger-ui/index.html


👉 OpenAPI JSON

http://localhost:8082/v3/api-docs

10. Como executar localmente

Criar banco:

CREATE DATABASE eventsdb;


Ajustar usuário/senha no application.properties

Executar:

EventsApiJavaApplication

11. Fluxo recomendado de teste (Postman)

1️⃣ Login via AUTH

POST /auth/login


Receber token.

2️⃣ Copiar token
Authorization: Bearer <...>

3️⃣ Testar rotas protegidas (ex.: inscrições / checkin)

12. Status do Microserviço

✔ Listagem aberta
✔ CRUD eventos
✔ Inscrições
✔ Cancelamento
✔ Check-in
✔ JWT integrado com AUTH
✔ Swagger funcionando

13. Futuras melhorias

Limite de vagas por evento

Perfis (ORGANIZADOR / USUARIO / PORTEIRO)

Exportação CSV / PDF

Rankings de participação

Notificações

14. Desenvolvido por

Enzo Zambiasi — Backend Java Spring Boot