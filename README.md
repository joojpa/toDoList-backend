# ToDo List – Backend API

REST API developed in **Java with Spring Boot** for task management (ToDo List).

##  Index

- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Data Models](#data-models)
- [Validations](#validations)
- [CORS](#cors)

## Technologies

* **Java 17**
* **Spring Boot 4.0.1**
* **Spring Data JPA**
* **PostgreSQL**
* **Spring Security Crypto** (BCrypt for password hashing)
* **Lombok**
* **Maven**

##  Prerequisites

Before running the application, make sure you have installed:

* Java 17 or higher
* Maven 3.6+ (or use the included Maven Wrapper)
* PostgreSQL 12+
* An IDE of your choice (IntelliJ IDEA, Eclipse, VS Code, etc.)

##  Configuration

### 1. PostgreSQL Database

```sql
CREATE DATABASE todolist;
```

### 2. Application Configuration

Edit the file `src/main/resources/application.properties` with your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=admin
spring.datasource.password=admin

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.open-in-view=false
```

##  Running the Application

### Using Maven Wrapper

**Windows:**

```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**

```bash
./mvnw spring-boot:run
```

### Using Installed Maven

```bash
mvn spring-boot:run
```

### Running the JAR

```bash
mvn clean package
java -jar target/toDoList-0.0.1-SNAPSHOT.jar
```

The application will be available at:

```
http://localhost:8080
```

##  Project Structure

```
src/main/java/br/com/todolist/toDoList/
├── config/
│   └── SecurityConfig.java
├── controllers/
│   ├── TaskController.java
│   └── UsersController.java
├── entities/
│   ├── TaskEntity.java
│   └── UserEntity.java
├── filter/
│   └── FilterTaskAuth.java
├── repository/
│   ├── TaskRepository.java
│   └── UserRepository.java
├── services/
│   ├── TaskService.java
│   └── UserService.java
└── ToDoListApplication.java
```

##  API Endpoints

### Users

#### Create User

```http
POST /users/
Content-Type: application/json
```

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

### Tasks (Protected with Basic Auth)

#### Create Task

```http
POST /tasks/
Authorization: Basic base64(email:password)
Content-Type: application/json
```

#### List Tasks

```http
GET /tasks/
Authorization: Basic base64(email:password)
```

#### Update Task

```http
PUT /tasks/{id}
Authorization: Basic base64(email:password)
```

#### Delete Task

```http
DELETE /tasks/{id}
Authorization: Basic base64(email:password)
```

##  Authentication

This API uses **Basic Authentication** for task-related endpoints.

* Credentials are sent via the `Authorization` header
* Passwords are validated using BCrypt
* Users can only access their own tasks

##  Data Models

### UserEntity

| Field    | Type   | Description  |
| -------- | ------ | ------------ |
| id       | Long   | User ID      |
| name     | String | User name    |
| email    | String | Unique email |
| password | String | BCrypt hash  |

### TaskEntity

| Field       | Type          | Description         |
| ----------- | ------------- | ------------------- |
| id          | Long          | Task ID             |
| title       | String        | Task title          |
| description | String        | Task description    |
| priority    | String        | LOW / MEDIUM / HIGH |
| startAt     | LocalDateTime | Start time          |
| endAt       | LocalDateTime | End time            |
| idUser      | Long          | Owner user ID       |
| createdAt   | LocalDateTime | Creation date       |

##  Validations

* Dates cannot be in the past
* `endAt` must be after `startAt`
* Only the task owner can update or delete tasks
* Priority must be `LOW`, `MEDIUM` or `HIGH`

##  CORS

The authentication filter allows **OPTIONS** requests for CORS preflight.

For production environments, it is recommended to configure a dedicated CORS policy.

## 🔒 Security

* Password hashing with **BCrypt**
* Custom authentication filter
* Task ownership validation

## 📄 License

This project is intended for **educational purposes**.
