# ToDo List - Backend API

API REST desenvolvida em Java com Spring Boot para gerenciamento de tarefas (ToDo List).

## 📋 Índice

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Autenticação](#autenticação)
- [Modelos de Dados](#modelos-de-dados)
- [Validações](#validações)
- [CORS](#cors)

## 🛠 Tecnologias

- **Java 17**
- **Spring Boot 4.0.1**
- **Spring Data JPA**
- **PostgreSQL**
- **Spring Security Crypto** (BCrypt para hash de senhas)
- **Lombok**
- **Maven**

## 📦 Pré-requisitos

Antes de executar a aplicação, certifique-se de ter instalado:

- Java 17 ou superior
- Maven 3.6+ (ou use o Maven Wrapper incluído)
- PostgreSQL 12+ (ou superior)
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code, etc.)

## ⚙️ Configuração

### 1. Banco de Dados PostgreSQL

Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE todolist;
```

### 2. Configuração da Aplicação

Edite o arquivo `src/main/resources/application.properties` com suas credenciais do PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.open-in-view=false
```

**Propriedades importantes:**
- `spring.jpa.hibernate.ddl-auto=update`: Cria/atualiza automaticamente as tabelas do banco
- `spring.jpa.show-sql=true`: Exibe as queries SQL no console (útil para debug)

## 🚀 Executando a Aplicação

### Usando Maven Wrapper

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Usando Maven instalado

```bash
mvn spring-boot:run
```

### Executando o JAR

Primeiro, compile o projeto:

```bash
mvn clean package
```

Depois, execute o JAR gerado:

```bash
java -jar target/toDoList-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: `http://localhost:8080`

## 📁 Estrutura do Projeto

```
src/main/java/br/com/todolist/toDoList/
├── config/              # Configurações
│   ├── CorsConfig.java         # Configuração CORS
│   └── SecurityConfig.java     # Configuração de segurança (BCrypt)
├── controllers/         # Controladores REST
│   ├── TaskController.java     # Endpoints de tarefas
│   └── UsersController.java    # Endpoints de usuários
├── entities/           # Entidades JPA
│   ├── TaskEntity.java         # Entidade de Tarefa
│   └── UserEntity.java         # Entidade de Usuário
├── filter/             # Filtros
│   └── FilterTaskAuth.java    # Filtro de autenticação Basic Auth
├── repository/         # Repositórios JPA
│   ├── TaskRepository.java     # Repositório de Tarefas
│   └── UserRepository.java     # Repositório de Usuários
├── services/           # Lógica de negócio
│   ├── TaskService.java        # Serviço de Tarefas
│   └── UserService.java        # Serviço de Usuários
└── ToDoListApplication.java    # Classe principal
```

## 🔌 Endpoints da API

### Usuários

#### Criar Usuário
```http
POST /users/
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@example.com",
  "password": "senha123"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com"
}
```

**Validações:**
- Nome: obrigatório, não pode ser vazio
- Email: obrigatório, deve conter "@", único no sistema
- Senha: obrigatória, mínimo 6 caracteres

### Tarefas

Todas as rotas de tarefas requerem autenticação Basic Auth.

#### Criar Tarefa
```http
POST /tasks/
Authorization: Basic base64(email:senha)
Content-Type: application/json

{
  "title": "Reunião com equipe",
  "description": "Discutir próximos passos do projeto",
  "priority": "ALTA",
  "startAt": "2026-01-25T10:00:00",
  "endAt": "2026-01-25T11:00:00"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "title": "Reunião com equipe",
  "description": "Discutir próximos passos do projeto",
  "priority": "ALTA",
  "startAt": "2026-01-25T10:00:00",
  "endAt": "2026-01-25T11:00:00",
  "idUser": 1,
  "createdAt": "2026-01-24T15:30:00"
}
```

#### Listar Tarefas do Usuário
```http
GET /tasks/
Authorization: Basic base64(email:senha)
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Reunião com equipe",
    "description": "Discutir próximos passos do projeto",
    "priority": "ALTA",
    "startAt": "2026-01-25T10:00:00",
    "endAt": "2026-01-25T11:00:00",
    "idUser": 1,
    "createdAt": "2026-01-24T15:30:00"
  }
]
```

#### Atualizar Tarefa
```http
PUT /tasks/{id}
Authorization: Basic base64(email:senha)
Content-Type: application/json

{
  "title": "Reunião com equipe - Atualizada",
  "description": "Nova descrição",
  "priority": "MEDIA",
  "startAt": "2026-01-25T10:00:00",
  "endAt": "2026-01-25T11:30:00"
}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "title": "Reunião com equipe - Atualizada",
  "description": "Nova descrição",
  "priority": "MEDIA",
  "startAt": "2026-01-25T10:00:00",
  "endAt": "2026-01-25T11:30:00",
  "idUser": 1,
  "createdAt": "2026-01-24T15:30:00"
}
```

#### Excluir Tarefa
```http
DELETE /tasks/{id}
Authorization: Basic base64(email:senha)
```

**Resposta (204 No Content)**

## 🔐 Autenticação

A API utiliza **Basic Authentication** para proteger os endpoints de tarefas.

### Como funciona

1. O cliente envia as credenciais (email e senha) no header `Authorization` usando Basic Auth
2. O filtro `FilterTaskAuth` intercepta requisições para `/tasks/*`
3. As credenciais são decodificadas e validadas contra o banco de dados
4. A senha é verificada usando BCrypt
5. Se válido, o ID do usuário é adicionado como atributo da requisição
6. O usuário só pode acessar suas próprias tarefas

### Exemplo de uso

```javascript
const email = 'joao@example.com'
const password = 'senha123'
const credentials = btoa(`${email}:${password}`)

fetch('http://localhost:8080/tasks/', {
  headers: {
    'Authorization': `Basic ${credentials}`
  }
})
```

### Endpoints protegidos

- `POST /tasks/` - Criar tarefa
- `GET /tasks/` - Listar tarefas
- `PUT /tasks/{id}` - Atualizar tarefa
- `DELETE /tasks/{id}` - Excluir tarefa

### Endpoints públicos

- `POST /users/` - Criar usuário (não requer autenticação)

## 📊 Modelos de Dados

### UserEntity

| Campo     | Tipo   | Descrição                    | Validações                    |
|-----------|--------|------------------------------|-------------------------------|
| id        | Long   | ID único do usuário          | Gerado automaticamente        |
| name      | String | Nome do usuário              | Obrigatório, não vazio        |
| email     | String | Email do usuário             | Obrigatório, único, deve conter "@" |
| password  | String | Senha (hash BCrypt)          | Obrigatório, mínimo 6 caracteres |

### TaskEntity

| Campo      | Tipo           | Descrição                    | Validações                    |
|------------|----------------|------------------------------|-------------------------------|
| id         | Long           | ID único da tarefa           | Gerado automaticamente         |
| title      | String         | Título da tarefa              | Obrigatório, máximo 50 caracteres |
| description| String         | Descrição da tarefa           | Opcional                       |
| priority   | String         | Prioridade (BAIXA/MEDIA/ALTA) | Obrigatório                    |
| startAt    | LocalDateTime  | Data/hora de início           | Obrigatório, formato: yyyy-MM-dd'T'HH:mm:ss |
| endAt      | LocalDateTime  | Data/hora de término          | Obrigatório, formato: yyyy-MM-dd'T'HH:mm:ss |
| idUser     | Long           | ID do usuário proprietário    | Definido automaticamente       |
| createdAt  | LocalDateTime  | Data de criação               | Gerado automaticamente         |

## ✅ Validações

### Validações de Usuário

- **Nome**: Não pode ser nulo ou vazio
- **Email**: Não pode ser nulo, deve conter "@", deve ser único no sistema
- **Senha**: Não pode ser nula, vazia ou ter menos de 6 caracteres

### Validações de Tarefa

#### Criação:
- `startAt` e `endAt` são obrigatórios
- `startAt` não pode ser no passado
- `endAt` não pode ser no passado
- `endAt` deve ser depois de `startAt`
- `title` é obrigatório (máximo 50 caracteres)
- `priority` deve ser: BAIXA, MEDIA ou ALTA

#### Atualização:
- `startAt` e `endAt` são obrigatórios
- `startAt` não pode ser no passado
- `endAt` deve ser depois de `startAt`
- Apenas o proprietário da tarefa pode atualizá-la

#### Exclusão:
- Apenas o proprietário da tarefa pode excluí-la

## 🌐 CORS

A aplicação está configurada para aceitar requisições de qualquer origem (configurado em `CorsConfig.java`). 

**⚠️ Atenção:** Em produção, configure para aceitar apenas o domínio do seu frontend:

```java
config.addAllowedOrigin("https://seu-frontend.com");
```

## 🔒 Segurança

- Senhas são armazenadas usando **BCrypt** (hash unidirecional)
- Autenticação via **Basic Auth** para endpoints de tarefas
- Validação de propriedade: usuários só podem acessar suas próprias tarefas
- Filtro de autenticação customizado (`FilterTaskAuth`)

## 📝 Notas Importantes

1. **Formato de Data**: As datas devem ser enviadas no formato `yyyy-MM-dd'T'HH:mm:ss` (sem timezone)
2. **Prioridade**: Valores aceitos são `BAIXA`, `MEDIA` ou `ALTA`
3. **Isolamento de Dados**: Cada usuário só visualiza e gerencia suas próprias tarefas
4. **DDL Auto**: A aplicação cria/atualiza automaticamente as tabelas no banco (`ddl-auto=update`)

## 🐛 Troubleshooting

### Erro de conexão com o banco
- Verifique se o PostgreSQL está rodando
- Confirme as credenciais no `application.properties`
- Verifique se o banco `todolist` foi criado

### Erro 401 Unauthorized
- Verifique se está enviando o header `Authorization` corretamente
- Confirme que o email e senha estão corretos
- Certifique-se de que o usuário existe no banco

### Erro 400 Bad Request
- Verifique se todos os campos obrigatórios foram enviados
- Confirme o formato das datas (`yyyy-MM-dd'T'HH:mm:ss`)
- Verifique as validações de negócio (datas no passado, etc.)

## 📄 Licença

Este projeto é de uso educacional.
