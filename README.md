EVENTS API — Serviço de Eventos, Inscrições e Presença — Java + Spring Boot + PostgreSQL + JWT

Este microserviço gerencia a regra de negócio dos eventos: eventos, inscrições, check-in/presença.
Ele não gera token: apenas valida JWT emitido pela Auth API e protege rotas com Spring Security.

1) Objetivo

Gerenciar os fluxos principais do Sistema de Eventos:

Listagem pública de eventos

CRUD de eventos (admin)

Inscrição / cancelamento de inscrição

Registro de check-in / presença

Integração com Auth API (JWT)

(Opcional no seu projeto) disparo de notificações via Email API

2) Tecnologias Utilizadas

Java (JDK 21+ / conforme seu ambiente)

Spring Boot (REST)

Spring Security (autorização)

Spring Web

Spring Data JPA / Hibernate (ORM)

PostgreSQL (banco)

JWT (jjwt) (validação do token)

Lombok

Swagger / springdoc-openapi

(Opcional) CORS Config para consumo do Portal Web

3) Estrutura do Projeto

src/main/java/com/sistema/eventsapi/

controller/

EventoController

InscricaoController

CheckinController

(se existir no seu projeto) PresencaController

dto/

EventoCriarRequisicao, EventoAtualizarRequisicao, EventoResposta

InscricaoRequisicao, InscricaoResposta

CheckinRequest, CheckinResponse

entity/

Evento

Inscricao

Presenca (ou equivalente)

exception/

ApiException, ErroResposta, GlobalExceptionHandler

repository/

EventoRepository

InscricaoRepository

PresencaRepository (se existir)

security/

SegurancaConfig

JwtFiltro

JwtServico

service/

EventoService, InscricaoService, CheckinService

impl/ (implementações)

EventsApiJavaApplication

4) Autenticação (JWT)

A Events API valida o token JWT emitido pela Auth API.

Header padrão nas rotas protegidas:
Authorization: Bearer <TOKEN>

Rotas públicas (típico no MVP)

GET /eventos

GET /eventos/{id}

Todas as demais rotas (inscrições, check-in, CRUD admin) exigem JWT.

5) Endpoints

Observação: os paths exatos podem variar conforme seu controller, mas o README abaixo está no padrão que você descreveu.

5.1 Listar eventos (PÚBLICO)

GET /eventos

Resposta (exemplo):

[
{
"id": 1,
"titulo": "Workshop de Microsserviços",
"descricao": "Evento de estudo",
"dataEvento": "2025-01-10"
}
]

5.2 Buscar evento por ID (PÚBLICO)

GET /eventos/{id}

5.3 Criar evento (PROTEGIDO)

POST /eventos
Header: Authorization: Bearer <TOKEN>

Body:

{
"titulo": "Evento Front-End",
"descricao": "Workshop",
"dataEvento": "2025-01-20"
}

5.4 Atualizar evento (PROTEGIDO)

PUT /eventos/{id}
Header: Authorization: Bearer <TOKEN>

5.5 Excluir evento (PROTEGIDO)

DELETE /eventos/{id}
Header: Authorization: Bearer <TOKEN>

5.6 Inscrever em evento (PROTEGIDO)

POST /inscricoes
Header: Authorization: Bearer <TOKEN>

Body (exemplo):

{ "eventoId": 1 }


A API normalmente identifica o usuário pelo token (sub/email/id) e cria a inscrição no banco.

5.7 Cancelar inscrição (PROTEGIDO)

DELETE /inscricoes/{id}
Header: Authorization: Bearer <TOKEN>

5.8 Listar inscrições de um evento (PROTEGIDO)

GET /inscricoes/evento/{idEvento}
Header: Authorization: Bearer <TOKEN>

5.9 Registrar check-in/presença (PROTEGIDO)

POST /checkin
Header: Authorization: Bearer <TOKEN>

Body (exemplo):

{
"eventoId": 1,
"inscricaoId": 12,
"offline": false
}


Como vocês removeram a API offline/gate, o fluxo é online: check-in/presença vai direto para a Events API.

6) Segurança (Spring Security)

Stateless (SessionCreationPolicy.STATELESS)

Sem cookies / sem sessão de servidor

JWT validado via JwtFiltro

Usuário autenticado é extraído do token e usado nas regras de inscrição/check-in

7) Banco de Dados (PostgreSQL)

Banco típico: eventsdb

Tabelas (modelo base do MVP)

eventos

id (PK)

titulo

descricao

data_evento (ou dataEvento mapeado)

inscricoes

id (PK)

evento_id (FK → eventos.id)

usuario_id ou usuario_email (depende da sua implementação)

status (ex.: INSCRITO, CANCELADO, PRESENTE)

presencas (se existir separado)

id (PK)

evento_id

usuario_id (ou email)

data_hora

offline (no seu caso pode existir ainda no schema, mas o fluxo agora é online)

Se no seu projeto “presença” e “check-in” estão na mesma tabela, basta refletir isso aqui.

8) application.properties (exemplo)
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

9) Swagger / OpenAPI

Swagger UI:

http://localhost:8082/swagger-ui/index.html

OpenAPI JSON:

http://localhost:8082/v3/api-docs

10) Como executar localmente

Criar banco:

CREATE DATABASE eventsdb;


Ajustar credenciais no application.properties

Executar:

Rodar EventsApiJavaApplication

11) Fluxo recomendado de teste (Postman)

Fazer login na Auth API:

POST http://localhost:8081/auth/login

Copiar token retornado:

Authorization: Bearer <TOKEN>

Testar:

Inscrição: POST /inscricoes

Check-in: POST /checkin

Listagens/consultas conforme controllers

12) Status do Microserviço (MVP)

Listagem pública: ✅ OK

CRUD eventos: ✅ OK

Inscrições / cancelamento: ✅ OK

Check-in / presença: ✅ OK

JWT integrado com Auth: ✅ OK

Swagger: ✅ OK

Fluxo online (sem Gate): ✅ OK

13) Futuras melhorias

Limite de vagas por evento

Regras por perfil (ORGANIZADOR / ADMIN / PORTEIRO / USER)

Auditoria de check-in

Exportação (CSV/PDF)

Notificações mais ricas via Email API

Rate limiting

14) Desenvolvido por

Matheus Vargas — Events API (Java Spring Boot + PostgreSQL + JWT)