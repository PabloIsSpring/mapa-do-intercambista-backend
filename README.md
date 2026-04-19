# 🌍 Mapa do Intercambista - Backend API

API robusta desenvolvida em **Spring Boot** para o ecossistema "Mapa do Intercambista". O sistema é responsável pela autenticação centralizada, gestão de perfis de utilizadores (Intercambistas) e agências de intercâmbio.

## 🛠️ Tecnologias e Configuração
* **Linguagem:** Java 17
* **Framework:** Spring Boot 3
* **Segurança:** Spring Security com Autenticação via Token JWT
* **Validação:** Jakarta Validation (Bean Validation)
* **Arquitetura:** REST com padrão DTO (Data Transfer Objects)

---

## 🔐 Endpoints de Autenticação (`/auth`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/auth/test` | Endpoint de verificação (Health Check). Retorna "Testando". |
| `POST` | `/auth/login` | Realiza a autenticação e retorna um token JWT para acesso às rotas protegidas. |
| `POST` | `/auth/register/intercambista` | Regista um novo utilizador com perfil de Intercambista. |
| `POST` | `/auth/register/agencia` | Regista um novo perfil de Agência. |

### Detalhes de Entrada e Saída (Auth)

**1. Login (`POST /auth/login`):**
* **Request (`LoginRequest`):** `email` (formato válido), `password` (não vazio).
* **Response (`LoginResponse`):** Retorna o `token` JWT gerado.

**2. Registo (`POST /auth/register/intercambista` ou `/agencia`):**
* **Request (`RegisterUserRequest`):** `nome`, `sobrenome`, `email`, `password`, `username`, `idade` (mínimo 0).
* **Response (`RegisterUserResponse`):** Retorna o `email` e `username` registados.

---

## 👤 Gerenciamento de Intercambistas (`/intercambista`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/intercambista/{username}` | Procura e retorna os dados de um intercambista pelo seu username. |
| `PUT` | `/intercambista` | Atualiza o nome de utilizador (username) do perfil. |
| `DELETE` | `/intercambista/{username}` | Remove permanentemente o perfil associado ao username informado. |

### Detalhes de Entrada e Saída (Intercambista)

**Consultar Perfil:**
* **Path Variable:** `username` (Obrigatório).
* **Response (`IntercambistaResponse`):** `username`, `nome`, `idade`.
* **Erros:** Retorna `404 Not Found` caso o username não exista.

**Atualizar Username:**
* **Request (`IntercambistaUpdtRequest`):** `username` (atual), `nUsername` (novo username desejado).
* **Response:** Objeto `IntercambistaResponse` atualizado.

---

## 📊 Estrutura dos Dados (DTOs)

### **Entradas (Request Models)**
- **LoginRequest:** `{ "email": "", "password": "" }`
- **RegisterUserRequest:** `{ "nome": "", "sobrenome": "", "email": "", "password": "", "username": "", "idade": 0 }`
- **IntercambistaUpdtRequest:** `{ "username": "", "nUsername": "" }`

### **Saídas (Response Models)**
- **LoginResponse:** `{ "token": "string" }`
- **RegisterUserResponse:** `{ "email": "string", "username": "string" }`
- **IntercambistaResponse:** `{ "username": "string", "nome": "string", "idade": int }`

---

## ⚙️ Como Executar

1. **Pré-requisitos:** Ter o **JDK 17** e o **Maven** instalados.
2. **Clonar o repositório:**
   ```bash
   git clone [https://github.com/PabloIsSpring/mapa-do-intercambista-backend.git](https://github.com/PabloIsSpring/mapa-do-intercambista-backend.git)