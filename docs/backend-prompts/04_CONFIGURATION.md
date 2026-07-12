# 04_CONFIGURATION.md

# HSSA Backend - Configuration Module

Follow ALL rules defined in

- 01_GLOBAL_RULES.md
- 02_PROJECT_SETUP.md
- 03_COMMON_INFRASTRUCTURE.md

Do not repeat those rules.

Implement ONLY the Configuration module.

Do NOT implement Security.

Do NOT implement Authentication.

Do NOT implement business modules.

Stop after completing this module.

Wait for approval.

---

# Objective

Configure the Spring Boot application for enterprise development.

Only create configuration classes.

Do not implement business logic.

---

# Verify Existing Configuration

Review the existing project configuration.

Verify

- Java 21
- Spring Boot 3.x
- application.yml
- application-dev.yml
- application-prod.yml

Do not overwrite existing configuration unless necessary.

---

# Package Structure

Create

config/

```
config
│
├── OpenApiConfig
├── CorsConfig
├── JacksonConfig
├── AsyncConfig
├── SchedulerConfig
├── CacheConfig
├── ModelMapperConfig (only if required)
├── TimeZoneConfig
├── LocaleConfig
└── WebMvcConfig
```

Do not create SecurityConfig.

That belongs to Security Module.

---

# application.yml

Review configuration.

Organize configuration into sections.

```
spring

server

logging

management

application

cors

swagger

file

async

cache
```

Do not configure JWT.

Do not configure authentication.

---

# application-dev.yml

Prepare

Development configuration.

Examples

- localhost database

- SQL logging enabled

- debug logging

- Swagger enabled

- H2 disabled

Do not include production values.

---

# application-prod.yml

Prepare production template.

Examples

- environment variables

- SQL logging disabled

- Swagger disabled

- optimized logging

- secure defaults

Do not hardcode secrets.

---

# OpenAPI Configuration

Create

OpenApiConfig

Configure

- API Title

- Description

- Version

- Contact

- License

- Server information

Swagger UI should be available.

Do not add JWT security.

JWT integration belongs to Security.

---

# CORS Configuration

Create

CorsConfig

Allow configuration from application.yml.

Support

Allowed Origins

Allowed Headers

Allowed Methods

Credentials

Max Age

Do not hardcode localhost.

Read from configuration.

---

# Jackson Configuration

Create

JacksonConfig

Configure

JavaTimeModule

LocalDate

LocalDateTime

OffsetDateTime

TimeZone

Serialization

Deserialization

Ignore unknown properties.

Do not configure custom serializers unless required.

---

# Async Configuration

Create

AsyncConfig

Configure

ThreadPoolTaskExecutor

Core Pool Size

Maximum Pool Size

Queue Capacity

Thread Prefix

Enable

@Async

---

# Scheduler Configuration

Create

SchedulerConfig

Enable

@EnableScheduling

Do not create scheduled jobs.

---

# Cache Configuration

Prepare cache configuration.

Do not implement Redis.

Use Spring Cache abstraction.

Future ready.

---

# Time Zone

Create

TimeZoneConfig

Default

UTC

Allow configuration from properties.

---

# Locale

Create

LocaleConfig

Default

English

Future ready for multiple languages.

---

# Web MVC

Create

WebMvcConfig

Configure

Message converters

Formatters

Interceptors placeholder

No authentication interceptors.

---

# Logging

Improve logging configuration.

Review

logback-spring.xml

Support

Console logging

Rolling file logging

Log levels

Development profile

Production profile

---

# Banner

Improve

banner.txt

Include

Project Name

Version

Spring Boot

Java Version

---

# Build Verification

Verify

Application starts successfully.

Swagger UI works.

Configuration classes load correctly.

No bean conflicts.

---

# Deliverables

Generate

✔ OpenApiConfig

✔ CorsConfig

✔ JacksonConfig

✔ AsyncConfig

✔ SchedulerConfig

✔ CacheConfig

✔ TimeZoneConfig

✔ LocaleConfig

✔ WebMvcConfig

✔ Updated YAML configuration

✔ Improved logging

✔ Updated banner

Nothing else.

---

# Completion Checklist

Verify

☐ OpenAPI configured

☐ CORS configured

☐ Jackson configured

☐ Async enabled

☐ Scheduler enabled

☐ Cache prepared

☐ TimeZone configured

☐ Locale configured

☐ WebMvc configured

☐ Logging configured

☐ Application starts

Stop.

Wait for approval.

Next Module

05_EXCEPTION_HANDLING.md