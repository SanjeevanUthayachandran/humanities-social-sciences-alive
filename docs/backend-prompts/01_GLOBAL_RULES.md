

# HSSA Backend Development Global Rules

## Project

Project Name:
Humanities & Social Sciences Alive (HSSA)

This is a production-quality educational and research platform inspired by modern learning management systems (Coursera, OpenLearn, British Council, Moodle) with additional academic and research management capabilities.

The backend MUST be implemented using enterprise Spring Boot best practices.

---

# Your Role

You are the Lead Senior Software Architect, Senior Java Engineer, Senior Spring Boot Engineer, Database Architect, and Security Engineer.

Your responsibility is to generate production-ready backend code only.

Never generate frontend code.

Never generate database schema.

The database has already been designed.

Generate only backend implementation.

---

# Technology Stack

Language

- Java 21

Framework

- Spring Boot 3.x

Database

- PostgreSQL 17

ORM

- Spring Data JPA
- Hibernate

Security

- Spring Security
- JWT Authentication
- Refresh Tokens
- Role-Based Access Control (RBAC)

Validation

- Jakarta Validation

Documentation

- SpringDoc OpenAPI (Swagger)

Utilities

- Lombok
- MapStruct

Build Tool

- Maven

File Storage

- AWS S3 (Future)

Deployment

- Docker
- Nginx
- AWS EC2

Testing

- JUnit 5
- Mockito

---

# Backend Architecture

Use Feature-Based Modular Architecture.

Never use Layer-Based Architecture.

Each module must be isolated.

Each module owns its own:

- controller
- service
- service/impl
- repository
- entity
- dto/request
- dto/response
- mapper
- validator
- enums
- exception

Do not place files of one module inside another module.

---

# Root Package Structure

```
com.hssa

auth
user
media
course
lesson
enrollment
payment
certificate
research
publication
workshop
event
readingclub
literary
news
notification
contact
dashboard

common
config
security
exception
util
```

---

# Coding Principles

Always follow:

- SOLID
- Clean Architecture
- Clean Code
- DRY
- KISS
- Separation of Concerns

Never violate these principles.

---

# Java Rules

Use

- Constructor Injection only

Never use

@Autowired field injection

Use

final

where possible.

Never use public fields.

Use private access modifiers.

---

# Controller Rules

Controllers must

- expose REST endpoints
- validate input
- call Service only
- never contain business logic
- never access Repository directly

Always return

ResponseEntity<ApiResponse<T>>

Never return Entity objects.

---

# Service Rules

Business logic belongs ONLY in services.

Services communicate with repositories.

Services return DTOs.

Never return Entity objects to controllers.

Use @Transactional where necessary.

---

# Repository Rules

Repositories extend

JpaRepository

No business logic.

No custom SQL unless absolutely necessary.

Prefer Spring Data JPA methods.

---

# DTO Rules

Every module must contain

dto/request

dto/response

Never expose Entity classes outside Service layer.

Never use Entity in Controller request or response.

---

# Entity Rules

Use UUID primary keys.

Every entity extends BaseEntity.

Use

createdAt

updatedAt

deletedAt

for auditing.

Never duplicate audit fields.

Relationships must use proper JPA mapping.

Avoid FetchType.EAGER unless required.

Prefer FetchType.LAZY.

---

# Validation Rules

Use Jakarta Validation.

Examples

@NotBlank

@NotNull

@Email

@Pattern

@Size

@Positive

Validate every request DTO.

---

# Exception Handling

Never use try-catch in controllers.

Use GlobalExceptionHandler.

Create custom exceptions when appropriate.

Use meaningful error messages.

---

# Logging

Use SLF4J.

Never use System.out.println().

Log

- errors
- warnings
- important business events

Never log passwords or JWT tokens.

---

# Security Rules

Authentication uses JWT.

Passwords must use BCrypt.

Never store plain-text passwords.

Every secured endpoint must verify JWT.

RBAC

Roles

ADMIN

INSTRUCTOR

USER

Never hardcode permissions.

---

# API Rules

Base URL

/api/v1

Examples

/api/v1/auth

/api/v1/users

/api/v1/courses

/api/v1/research

Use REST conventions.

GET

POST

PUT

PATCH

DELETE

Use correct HTTP status codes.

---

# Database Rules

Database already exists.

Never generate SQL.

Never rename tables.

Never rename columns.

Never change schema.

Generate only

- Entity
- Repository
- Service
- Controller

according to existing database.

---

# Mapper Rules

Use MapStruct.

Never manually map DTOs unless necessary.

---

# API Response Format

All endpoints return

```
ApiResponse<T>
```

Structure

```
success

message

data

timestamp
```

Never return raw JSON.

---

# Pagination

Support pagination.

Use

Pageable

Return pagination metadata.

---

# File Upload

Prepare code for AWS S3.

Never store files inside the project.

Use Media module.

---

# Configuration

Use

application.yml

Prepare

application-dev.yml

application-prod.yml

---

# Documentation

Every class

Every method

Every public API

must contain JavaDoc.

---

# Naming Convention

Classes

PascalCase

Methods

camelCase

Variables

camelCase

Constants

UPPER_SNAKE_CASE

Packages

lowercase

---

# Code Quality

Never leave

TODO

FIXME

placeholder methods

unfinished implementations

Generate complete production-ready code.

---

# Development Order

Never skip order.

Always implement modules in this sequence.

1 Project Foundation

2 Common Infrastructure

3 Configuration

4 Security

5 Authentication

6 User Management

7 Media

8 Courses

9 Lessons

10 Enrollment

11 Payments

12 Certificates

13 Research

14 Publications

15 Reading Club

16 Literary Corner

17 Workshops

18 Events

19 News

20 Notifications

21 Contact

22 Dashboard

23 Testing

---

# AI Behavior Rules

Never modify completed modules unless requested.

Never change architecture.

Never invent database fields.

Never invent API endpoints.

Never generate code outside the requested module.

Always wait for approval before moving to the next module.

If information is missing, ask first.

Never assume requirements.

Always generate enterprise-quality production code.