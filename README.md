# Mapa do Intercambista - Backend API

API REST em Spring Boot para o Mapa do Intercambista. O backend gerencia autenticação JWT, perfis de intercambistas e agências, pacotes de intercâmbio, fóruns, respostas e avaliações.

## Tecnologias

- Java 21
- Spring Boot 4.0.4
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Bean Validation
- Flyway
- MySQL 8
- JWT com `java-jwt`
- Upload de arquivos via `MultipartFile`

## Autenticação

A API usa autenticação stateless com JWT no header:

```http
Authorization: Bearer <token>
```

Rotas públicas configuradas:

- `POST /auth/login`
- `POST /auth/register/intercambista`
- `POST /auth/register/agencia`

Todas as demais rotas exigem token válido, inclusive `GET /auth/test`.

O token é gerado no login, contém o `userId` e o e-mail do usuário como `subject`, e expira em 4 horas.

## Erros

O handler global retorna erros neste formato:

```json
{
  "status": 404,
  "error": "Não encontrado",
  "massage": "Mensagem do erro",
  "path": "/rota",
  "localDateTime": "2026-05-18T20:00:00"
}
```

Status tratados pela aplicação:

- `400 Bad Request`: arquivo ausente, vazio ou que não é imagem.
- `404 Not Found`: entidade não encontrada ou marcada como removida em consultas específicas.
- `409 Conflict`: e-mail, username, CNPJ, razão social ou avaliação duplicada.
- `401 Unauthorized`/`403 Forbidden`: falha de autenticação/autorização tratada pelo Spring Security.

## Regras gerais

- Senhas são persistidas com BCrypt.
- Usuários podem ter papel `ROLE_USER` ou `ROLE_AGENCIA`.
- Exclusões de usuário, agência, país, fórum, resposta e avaliação são lógicas em pontos do sistema, usando `deleted_at`.
- Uploads ficam disponíveis publicamente em `/uploads/**`, apontando para o diretório configurado em `APP_UPLOAD_DIR` ou `uploads`.
- Uploads aceitam apenas arquivos cujo `Content-Type` começa com `image/`.
- IDs principais são UUID, exceto países, que usam código de 2 letras.
- Países são pré-carregados pela migration e usam padrão ISO 3166-1 alfa-2, como `BR`, `US`, `CA`.

## Variáveis de ambiente

```env
URL_DATABASE=jdbc:mysql://localhost:3306/nome_do_banco
MYSQL_USER=usuario
MYSQL_PASSWORD=senha
TOKEN_SECRET=segredo_do_jwt
APP_UPLOAD_DIR=uploads
```

No `docker-compose.yml`, a API usa:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/${DATABASE_NAME}
SPRING_DATASOURCE_USERNAME=${MYSQL_USER}
SPRING_DATASOURCE_PASSWORD=${MYSQL_PASSWORD}
TOKEN_SECRET=${TOKEN_SECRET}
APP_UPLOAD_DIR=/app/uploads
```

## Executando

Com Maven local:

```bash
./mvnw spring-boot:run
```

Com Docker Compose:

```bash
docker compose up --build
```

## Rotas de autenticação

### `GET /auth/test`

Retorna texto simples:

```text
Testando
```

Exige autenticação.

### `POST /auth/login`

Autentica um usuário por e-mail e senha.

Request:

```json
{
  "email": "usuario@email.com",
  "password": "senha"
}
```

Validações:

- `email`: obrigatório e em formato de e-mail.
- `password`: obrigatório.

Response `200 OK`:

```json
{
  "token": "jwt"
}
```

### `POST /auth/register/intercambista`

Cadastra usuário com perfil de intercambista.

Request:

```json
{
  "nome": "Maria",
  "sobrenome": "Silva",
  "email": "maria@email.com",
  "password": "senha",
  "username": "maria",
  "idade": 21
}
```

Validações e regras:

- `nome`, `sobrenome`, `email`, `password` e `username`: obrigatórios.
- `email`: formato válido e único.
- `idade`: mínimo `0`.
- `username`: único entre intercambistas.
- O perfil recebe foto padrão em `/uploads/intercambista/default-profile/png`.

Response `201 Created`:

```json
{
  "email": "maria@email.com",
  "username": "maria"
}
```

### `POST /auth/register/agencia`

Cadastra usuário com perfil de agência.

Request:

```json
{
  "email": "contato@agencia.com",
  "password": "senha",
  "razaoSocial": "Agência LTDA",
  "nomeFantasia": "Agência Intercâmbio",
  "cnpj": "00.000.000/0001-00",
  "username": "agencia"
}
```

Validações e regras:

- Todos os campos são obrigatórios.
- `email`: formato válido e único.
- `cnpj`, `razaoSocial` e `username`: não podem estar em uso por outra agência.

Response `201 Created`:

```json
{
  "email": "contato@agencia.com",
  "nomeFantasia": "Agência Intercâmbio",
  "cnpj": "00.000.000/0001-00",
  "username": "agencia",
  "razaoSocial": "Agência LTDA",
  "urlFotoPerfil": null
}
```

## Rotas de intercambista

### `GET /intercambista/{username}`

Busca intercambista por username.

Response `200 OK`:

```json
{
  "username": "maria",
  "nome": "Maria",
  "idade": 21,
  "urlFotoPerfil": "/uploads/intercambista/default-profile/png"
}
```

### `POST /intercambista/{username}/foto`

Atualiza a foto do perfil do intercambista.

Content-Type:

```http
multipart/form-data
```

Form data:

- `image`: arquivo de imagem obrigatório.

Regras:

- O intercambista precisa existir.
- O arquivo não pode estar vazio.
- O `Content-Type` precisa começar com `image/`.
- A imagem é salva em `/uploads/intercambista/{uuid}.{extensao}`.

Response `200 OK`: `IntercambistaResponse`.

### `PUT /intercambista`

Atualiza o username de um intercambista.

Request:

```json
{
  "username": "maria",
  "nUsername": "maria-silva"
}
```

Regras:

- `username` e `nUsername` são obrigatórios.
- O intercambista atual precisa existir.
- O novo username não pode estar em uso.

Response `200 OK`: `IntercambistaResponse`.

### `DELETE /intercambista/{username}`

Remove logicamente o usuário vinculado ao intercambista, preenchendo `deletedAt` no usuário.

Response:

```http
204 No Content
```

## Rotas de agência

### `GET /agencia/{username}`

Busca agência por username.

Response `200 OK`:

```json
{
  "email": "contato@agencia.com",
  "nomeFantasia": "Agência Intercâmbio",
  "cnpj": "00.000.000/0001-00",
  "username": "agencia",
  "razaoSocial": "Agência LTDA",
  "urlFotoPerfil": "/uploads/agencias/foto.png"
}
```

Regras:

- A agência precisa existir.
- Agências com `deletedAt` preenchido retornam `404`.

### `POST /agencia/{username}/foto`

Atualiza a foto de perfil da agência.

Content-Type:

```http
multipart/form-data
```

Form data:

- `image`: arquivo de imagem obrigatório.

Regras:

- A agência precisa existir.
- O arquivo não pode estar vazio.
- O `Content-Type` precisa começar com `image/`.
- A imagem é salva em `/uploads/agencias/{uuid}.{extensao}`.

Response `200 OK`: `AgenciaResponse`.

### `PUT /agencia`

Atualiza dados de agência.

Request:

```json
{
  "username": "agencia",
  "newUsername": "nova-agencia",
  "nomeFantasia": "Nova Agência",
  "razaoSocial": "Nova Agência LTDA",
  "cnpj": "11.111.111/0001-11"
}
```

Regras:

- Todos os campos são obrigatórios.
- A agência atual precisa existir.
- `newUsername`, `razaoSocial` ou `cnpj` não podem estar em uso.

Response `200 OK`: `AgenciaResponse`.

### `DELETE /agencia/{username}`

Remove logicamente o usuário vinculado à agência, preenchendo `deletedAt` no usuário.

Response:

```http
204 No Content
```

## Rotas de pacotes

Pacotes são representados pela entidade `Destino`.

### `POST /pacotes`

Cria um pacote de intercâmbio.

Content-Type:

```http
multipart/form-data
```

Form data:

- `usernameAgencia`: username da agência.
- `tipoDuracao`: tipo de duração.
- `tipoIntercambio`: tipo do programa.
- `idPais`: código do país.
- `duracao`: número positivo.
- `preco`: valor positivo.
- `cidade`: cidade.
- `universidade`: universidade.
- `descricao`: descrição.
- `image`: imagem obrigatória.

Validações e regras:

- Campos textuais usam `@NotBlank`.
- `duracao` e `preco` precisam ser positivos.
- `preco` e `image` são obrigatórios.
- A agência e o país precisam existir.
- A imagem precisa ser válida e é salva em `/uploads/destinos/{uuid}.{extensao}`.

Observação sobre implementação atual:

- O service chama `setTipoDuracao(req.tipoIntercambio())` depois de `setTipoDuracao(req.tipoDuracao())`; por isso o campo `tipoDuracao` pode receber o valor de `tipoIntercambio`, enquanto `tipoPrograma` pode sair nulo na resposta.

Response `201 Created`:

```json
{
  "id": "uuid",
  "cidade": "Toronto",
  "universidade": "University of Toronto",
  "preco": 12000.00,
  "urlFoto": "/uploads/destinos/foto.png",
  "duracao": 6,
  "tipoPrograma": "Idiomas",
  "tipoDuracao": "Meses",
  "agenciaResponse": {},
  "paisResponse": {},
  "descricao": "Descrição do pacote",
  "deleteAt": null
}
```

### `GET /pacotes`

Lista todos os pacotes.

Response `200 OK`:

```json
[
  {
    "id": "uuid",
    "cidade": "Toronto",
    "universidade": "University of Toronto",
    "preco": 12000.00,
    "urlFoto": "/uploads/destinos/foto.png",
    "duracao": 6,
    "tipoPrograma": "Idiomas",
    "tipoDuracao": "Meses",
    "agenciaResponse": {},
    "paisResponse": {},
    "descricao": "Descrição do pacote",
    "deleteAt": null
  }
]
```

### `GET /pacotes/{id}`

Busca pacote por UUID.

Regras:

- Retorna `404` se o destino não existir.

Response `200 OK`: `DestinoResponse`.

### `GET /pacotes/pais/{idPais}`

Lista pacotes por país.

Exemplo:

```http
GET /pacotes/pais/CA
```

Response `200 OK`: lista de `DestinoResponse`.

### `GET /pacotes/agencia/{agenciaUsername}`

Lista pacotes por username da agência.

Response `200 OK`: lista de `DestinoResponse`.

### `GET /pacotes/cidade/{cidade}`

Lista pacotes por cidade.

Response `200 OK`: lista de `DestinoResponse`.

## Rotas de fóruns

### `POST /foruns`

Cria uma publicação no fórum.

Content-Type:

```http
multipart/form-data
```

Form data:

- `username`: username do intercambista.
- `titulo`: título.
- `comentario`: comentário.
- `image`: imagem obrigatória.

Regras:

- Todos os campos são obrigatórios.
- O intercambista precisa existir.
- A imagem precisa ser válida e é salva em `/uploads/foruns/{uuid}.{extensao}`.
- `likes` e `dislikes` começam em `0`.
- `createdAt` é preenchido no momento da criação.

Response `201 Created`: `ForumResponse`.

### `GET /foruns`

Lista fóruns ativos, ordenados por `createdAt` decrescente.

Response `200 OK`:

```json
[
  {
    "id": "uuid",
    "intercambista": {},
    "comentario": "Comentário",
    "titulo": "Título",
    "urlFotoDestino": "/uploads/foruns/foto.png",
    "likes": 0,
    "dislikes": 0,
    "createdAt": "2026-05-18T20:00:00"
  }
]
```

### `GET /foruns/{id}`

Busca fórum ativo por UUID.

Regras:

- Fóruns com `deletedAt` preenchido retornam `404`.

Response `200 OK`: `ForumResponse`.

### `PUT /foruns/{id}/like`

Incrementa `likes` do fórum em `1`.

Response `200 OK`: `ForumResponse`.

### `PUT /foruns/{id}/deslike`

Incrementa `dislikes` do fórum em `1`.

Response `200 OK`: `ForumResponse`.

### `DELETE /foruns/{id}`

Remove logicamente o fórum, preenchendo `deletedAt`.

Response:

```http
204 No Content
```

## Rotas de respostas

### `POST /foruns/{idForum}/respostas`

Cria uma resposta em um fórum ativo.

Request:

```json
{
  "username": "maria",
  "comentario": "Minha resposta"
}
```

Validações e regras:

- `username` e `comentario` são obrigatórios.
- O fórum precisa existir e estar ativo.
- O intercambista precisa existir.
- `likes` e `dislikes` começam em `0`.
- `createdAt` é preenchido no momento da criação.

Response `201 Created`: `RespostaResponse`.

### `GET /foruns/{idForum}/respostas`

Lista respostas ativas de um fórum em ordem crescente de criação.

Response `200 OK`: lista de `RespostaResponse`.

### `GET /respostas/{id}`

Busca resposta ativa por UUID.

Response `200 OK`: `RespostaResponse`.

### `PUT /respostas/{id}/like`

Incrementa `likes` da resposta em `1`.

Response `200 OK`: `RespostaResponse`.

### `PUT /respostas/{id}/deslike`

Incrementa `dislikes` da resposta em `1`.

Response `200 OK`: `RespostaResponse`.

### `DELETE /respostas/{id}`

Remove logicamente a resposta, preenchendo `deletedAt`.

Response:

```http
204 No Content
```

## Rotas de avaliações

### `POST /avaliacoes`

Cria uma avaliação para um pacote.

Request:

```json
{
  "idDestino": "uuid",
  "username": "maria",
  "comentario": "Experiência excelente",
  "nota": 4.5
}
```

Validações e regras:

- `idDestino`: obrigatório.
- `username`: obrigatório.
- `comentario`: obrigatório.
- `nota`: obrigatória, mínimo `0.0` e máximo `5.0`.
- O destino precisa existir.
- O intercambista precisa existir.
- Um intercambista só pode ter uma avaliação ativa por destino.
- A tabela também possui constraint única para `id_intercambista` + `id_destino`.

Response `201 Created`:

```json
{
  "id": "uuid",
  "destino": {},
  "intercambistaResponse": {},
  "comentario": "Experiência excelente",
  "nota": 4.5,
  "createdAt": null
}
```

Observação sobre implementação atual:

- O DTO possui `createdAt`, mas o service de avaliações não preenche esse campo explicitamente.

### `GET /destinos/{idDestino}/avaliacoes`

Lista avaliações ativas de um destino.

Response `200 OK`: lista de `AvaliacaoResponse`.

### `GET /destinos/{idDestino}/avaliacoes/media`

Retorna a média das avaliações ativas de um destino.

Regras:

- Se não houver avaliações ativas, retorna `0.0`.

Response `200 OK`:

```json
4.5
```

### `DELETE /avaliacoes/{id}`

Remove logicamente a avaliação, preenchendo `deletedAt`.

Response:

```http
204 No Content
```

## DTOs de resposta

### `AgenciaResponse`

```json
{
  "email": "string",
  "nomeFantasia": "string",
  "cnpj": "string",
  "username": "string",
  "razaoSocial": "string",
  "urlFotoPerfil": "string"
}
```

### `IntercambistaResponse`

```json
{
  "username": "string",
  "nome": "string",
  "idade": 0,
  "urlFotoPerfil": "string"
}
```

### `PaisResponse`

```json
{
  "id": "BR",
  "nome": "Brasil",
  "idiomaPrincipal": "Português",
  "moeda": "Real"
}
```

### `DestinoResponse`

```json
{
  "id": "uuid",
  "cidade": "string",
  "universidade": "string",
  "preco": 0.00,
  "urlFoto": "string",
  "duracao": 0,
  "tipoPrograma": "string",
  "tipoDuracao": "string",
  "agenciaResponse": {},
  "paisResponse": {},
  "descricao": "string",
  "deleteAt": null
}
```

### `ForumResponse`

```json
{
  "id": "uuid",
  "intercambista": {},
  "comentario": "string",
  "titulo": "string",
  "urlFotoDestino": "string",
  "likes": 0,
  "dislikes": 0,
  "createdAt": "2026-05-18T20:00:00"
}
```

### `RespostaResponse`

```json
{
  "id": "uuid",
  "forum": {},
  "intercambistaResponse": {},
  "comentario": "string",
  "likes": 0,
  "dislikes": 0,
  "createdAt": "2026-05-18T20:00:00"
}
```

### `AvaliacaoResponse`

```json
{
  "id": "uuid",
  "destino": {},
  "intercambistaResponse": {},
  "comentario": "string",
  "nota": 5.0,
  "createdAt": null
}
```

## Observações importantes do estado atual

- Não existe controller público para CRUD de países, embora exista `PaisService` e `PaisRequest`.
- As rotas de pacote por país, agência e cidade usam prefixos específicos: `/pacotes/pais/{idPais}`, `/pacotes/agencia/{agenciaUsername}` e `/pacotes/cidade/{cidade}`.
- A aplicação não aplica checagem de papel por rota no `SecurityConfig`; qualquer usuário autenticado acessa as rotas protegidas.
- Alguns campos JSON seguem os nomes dos records atuais, como `massage`, `deleteAt`, `urlFotoDestino` e `intercambistaResponse`.
