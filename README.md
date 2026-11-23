# CRM Backend Project

Enterprise Customer Relationship Management (CRM) Backend built with Java 21 and Spring Boot 3.2.2, modeled after the proven architecture and standards of the Loan Management System (LMS) backend.

---

## 1. LMS Reference Architecture & Setup Analysis

The CRM backend setup was created by analyzing the existing production setup from LMS (`C:\Users\User\Desktop\LMS\LMS-Backend`):

| Setup Element | LMS Project Setup | CRM Backend Setup | Purpose / Benefit |
| :--- | :--- | :--- | :--- |
| **Java Version** | Java 21 (LTS) | Java 21 (LTS) | Modern Java records, virtual threads capability, switch expressions, pattern matching |
| **Framework** | Spring Boot 3.2.2 | Spring Boot 3.2.2 | Jakarta EE 10 baseline, robust ecosystem, production-grade security & metrics |
| **Build Tool** | Apache Maven with `mvnw` wrapper | Apache Maven with `mvnw` wrapper | Reproducible builds across team members without local Maven configuration issues |
| **Packaging & Group ID**| `com.its.lms` | `com.its.crm` | Consistent corporate naming standard (`com.its.*`) |
| **Database & Dialect** | PostgreSQL 15/16 with `PostgreSQLDialect` | PostgreSQL with `PostgreSQLDialect` | Enterprise ACID storage, UUID generation, index support |
| **JSONB & Custom Types** | `io.hypersistence:hypersistence-utils-hibernate-63` | `io.hypersistence:hypersistence-utils-hibernate-63` | Native PostgreSQL JSONB mappings for dynamic pipeline steps and form fields |
| **Database Migrations** | Liquibase (`db.changelog-master.yaml`) | Liquibase (`db.changelog-master.yaml`) | Automated version-controlled database schema changes per environment |
| **Code Generation** | Lombok + MapStruct | Lombok + MapStruct | Zero-boilerplate models + fast, compile-time type-safe DTO/Entity mappings |
| **Security & Auth** | Spring Security 6 + JWT + CORS | Spring Security 6 + JJWT + CORS | Stateless REST authentication with BCrypt hashing and Bearer tokens |
| **Logging & Tracing** | Logback rolling file + MDC `requestId` | Logback rolling file + MDC `requestId` | End-to-end request tracing via `X-Request-Id` and `RequestIdFilter` |
| **API Documentation** | Springdoc OpenAPI (Swagger UI) | Springdoc OpenAPI (Swagger UI) | Interactive Swagger UI (`/api/swagger-ui/index.html`) with JWT Bearer support |
| **Error Handling** | `@RestControllerAdvice` + Standard DTO | `@RestControllerAdvice` + Standard DTO | Predictable JSON error responses (`ErrorResponseDTO`) for frontend consumers |

---

## 2. Project Directory Structure

```
LMS_CRM_Project/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
├── .gitignore
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── its/
    │   │           └── crm/
    │   │               ├── CrmApplication.java
    │   │               ├── common/
    │   │               │   ├── decorator/MDCTaskDecorator.java
    │   │               │   ├── dto/ApiResponse.java
    │   │               │   ├── dto/ErrorResponseDTO.java
    │   │               │   ├── dto/PageResponse.java
    │   │               │   ├── enums/MDCConstants.java
    │   │               │   └── filter/RequestIdFilter.java
    │   │               ├── config/
    │   │               │   ├── AsyncConfig.java
    │   │               │   ├── AuditorAwareImpl.java
    │   │               │   ├── CorsConfig.java
    │   │               │   ├── JacksonConfig.java
    │   │               │   ├── SecurityConfig.java
    │   │               │   ├── SwaggerConfig.java
    │   │               │   └── WhitelistProperties.java
    │   │               ├── controller/
    │   │               │   ├── AuthController.java
    │   │               │   ├── GlobalTaskController.java
    │   │               │   ├── NoteController.java
    │   │               │   ├── PipelineController.java
    │   │               │   ├── TaskController.java
    │   │               │   └── UserController.java
    │   │               ├── dto/
    │   │               │   ├── auth/ (LoginRequest, RegisterRequest, AuthResponse)
    │   │               │   ├── globaltask/ (CreateGlobalTaskRequest, GlobalTaskDTO, UpdateGlobalTaskRequest)
    │   │               │   ├── note/ (CreateNoteRequest, NoteDTO, UpdateNoteRequest)
    │   │               │   ├── pipeline/ (CreatePipelineRequest, PipelineDTO, UpdatePipelineRequest)
    │   │               │   ├── task/ (CreateTaskRequest, TaskDTO, UpdateTaskRequest)
    │   │               │   └── user/ (UpdateUserRequest, UserDTO)
    │   │               ├── entity/
    │   │               │   ├── Auditable.java
    │   │               │   ├── GlobalTaskEntity.java
    │   │               │   ├── NoteEntity.java
    │   │               │   ├── PipelineEntity.java
    │   │               │   ├── TaskEntity.java
    │   │               │   └── UserEntity.java
    │   │               ├── enums/
    │   │               │   ├── RelatedEntityType.java
    │   │               │   ├── TaskPriority.java
    │   │               │   ├── TaskStatus.java
    │   │               │   └── UserRole.java
    │   │               ├── exception/
    │   │               │   ├── BadRequestException.java
    │   │               │   ├── ConflictException.java
    │   │               │   ├── CRMException.java
    │   │               │   ├── ResourceNotFoundException.java
    │   │               │   └── handler/CRMExceptionHandler.java
    │   │               ├── mapper/
    │   │               │   ├── GlobalTaskMapper.java
    │   │               │   ├── NoteMapper.java
    │   │               │   ├── PipelineMapper.java
    │   │               │   ├── TaskMapper.java
    │   │               │   └── UserMapper.java
    │   │               ├── repository/
    │   │               │   ├── GlobalTaskRepository.java
    │   │               │   ├── NoteRepository.java
    │   │               │   ├── PipelineRepository.java
    │   │               │   ├── TaskRepository.java
    │   │               │   └── UserRepository.java
    │   │               ├── security/
    │   │               │   ├── CustomUserDetailsService.java
    │   │               │   ├── JwtAuthenticationFilter.java
    │   │               │   ├── JwtTokenProvider.java
    │   │               │   ├── SecurityUtils.java
    │   │               │   └── UserPrincipal.java
    │   │               └── service/
    │   │                   ├── AuthService.java & impl/AuthServiceImpl.java
    │   │                   ├── GlobalTaskService.java & impl/GlobalTaskServiceImpl.java
    │   │                   ├── NoteService.java & impl/NoteServiceImpl.java
    │   │                   ├── PipelineService.java & impl/PipelineServiceImpl.java
    │   │                   ├── TaskService.java & impl/TaskServiceImpl.java
    │   │                   └── UserService.java & impl/UserServiceImpl.java
    │   └── resources/
    │       ├── application.properties
    │       ├── application-local.properties
    │       ├── application-dev.properties
    │       └── db/
    │           └── changelog/
    │               ├── db.changelog-master.yaml
    │               └── migrations/
    │                   └── 001_initial_schema.sql
    └── test/
        └── java/com/its/crm/CrmApplicationTests.java
```

---

## 3. Database Schema Overview

The database is managed via Liquibase migrations with 5 core tables:

1. **`users`**: UUID primary key, name, email (unique index), password (BCrypt), avatar, role (`admin`, `manager`, `member`).
2. **`pipelines`**: UUID primary key, name, description, `steps` (JSONB for dynamic stages and custom form fields), `deleted_steps` (JSONB for versioning).
3. **`tasks`**: UUID primary key, `pipeline_id` (foreign key with `ON DELETE CASCADE`), `step_id`, `form_data` (JSONB for dynamic submission data), `form_fields_snapshot` (JSONB schema snapshot), `status` (`pending`, `in_progress`, `completed`).
4. **`notes`**: UUID primary key, title, content, `created_by` (foreign key to users), `tags` (JSONB list of strings), `related_entity_id`, `related_entity_type` (`task`, `pipeline`, `global`).
5. **`global_tasks`**: UUID primary key, title, description, `assigned_to` (User), `created_by` (User), `status` (`pending`, `in_progress`, `completed`), `priority` (`low`, `medium`, `high`), `due_date`, related entity linkages.

---

## 4. API Endpoints

All APIs are prefixed with `/api` (configured via `server.servlet.context-path=/api`).

### Authentication (`/api/auth`)
- `POST /api/auth/register` - Register a new user and receive JWT token
- `POST /api/auth/login` - Authenticate with email/password and receive JWT token

### Users (`/api/users`)
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user details by UUID
- `PUT /api/users/{id}` - Update user details/role/avatar
- `DELETE /api/users/{id}` - Delete user

### Pipelines (`/api/pipelines`)
- `GET /api/pipelines` - List all pipelines with embedded JSONB steps
- `GET /api/pipelines/{id}` - Get pipeline by UUID
- `POST /api/pipelines` - Create a new pipeline with steps & form schema
- `PUT /api/pipelines/{id}` - Update pipeline structure
- `DELETE /api/pipelines/{id}` - Delete pipeline (cascades to tasks)

### Tasks (`/api/tasks` & `/api/pipelines/{pipelineId}/tasks`)
- `GET /api/pipelines/{pipelineId}/tasks` - List tasks under a specific pipeline
- `POST /api/pipelines/{pipelineId}/tasks` - Create a task in a pipeline step
- `GET /api/tasks/{id}` - Get task details with formData & snapshot
- `PUT /api/tasks/{id}` - Update task step/formData/status
- `DELETE /api/tasks/{id}` - Delete task

### Notes (`/api/notes`)
- `GET /api/notes` - List notes (with optional `relatedEntityId` & `relatedEntityType` filters)
- `GET /api/notes/{id}` - Get note by UUID
- `POST /api/notes` - Create note attached to task, pipeline, or global
- `PUT /api/notes/{id}` - Update note content/tags
- `DELETE /api/notes/{id}` - Delete note

### Global Tasks (`/api/global-tasks`)
- `GET /api/global-tasks` - List global tasks (filterable by `status`, `priority`, `assignedTo`)
- `GET /api/global-tasks/{id}` - Get global task by UUID
- `POST /api/global-tasks` - Create a global task
- `PUT /api/global-tasks/{id}` - Update global task
- `DELETE /api/global-tasks/{id}` - Delete global task

---

## 5. How to Run

### Prerequisites
- **JDK 21** installed (e.g. `C:\Users\User\.jdks\ms-21.0.11`)
- **PostgreSQL** running on port 5432 (or update `src/main/resources/application-local.properties`)
  - Create database: `CREATE DATABASE crm_db;`

### Build
Using Maven or the included Maven wrapper:
```bash
./mvnw clean compile
```

### Run the Application
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```
Or directly from your IDE by running `com.its.crm.CrmApplication`.

### Swagger Documentation
Once the server is running on port 8081:
- Swagger UI: `http://localhost:8081/api/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/api/api-docs`
