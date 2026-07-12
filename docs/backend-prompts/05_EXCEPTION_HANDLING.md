# 05_EXCEPTION_HANDLING.md

# HSSA Backend - Global Exception Handling Module

Follow ALL rules defined in

- 01_GLOBAL_RULES.md
- 02_PROJECT_SETUP.md
- 03_COMMON_INFRASTRUCTURE.md
- 04_CONFIGURATION.md

Do not repeat those rules.

Implement ONLY the Global Exception Handling module.

Do NOT implement Security.

Do NOT implement Authentication.

Do NOT implement business modules.

Stop after completing this module.

Wait for approval.

---

# Objective

Build a centralized, enterprise-grade exception handling system.

All backend modules must use this exception framework.

Never use try-catch blocks inside controllers.

Controllers should allow exceptions to propagate to the global handler.

---

# Package Structure

Create

exception/

```
exception
│
├── handler
├── dto
├── enums
├── constants
└── advice
```

Do not create business-specific exceptions.

---

# Global Exception Handler

Create

GlobalExceptionHandler

Requirements

Use

@RestControllerAdvice

Handle

- Exception
- RuntimeException
- ValidationException
- ConstraintViolationException
- MethodArgumentNotValidException
- HttpMessageNotReadableException
- HttpRequestMethodNotSupportedException
- MissingServletRequestParameterException
- MissingRequestHeaderException
- MissingPathVariableException
- TypeMismatchException
- AccessDeniedException
- AuthenticationException
- DataIntegrityViolationException
- EntityNotFoundException
- NoHandlerFoundException
- MaxUploadSizeExceededException

Return

ResponseEntity<ApiResponse<ErrorResponse>>

Never expose stack traces.

Never expose internal database information.

---

# Error Response DTO

Create

ErrorResponse

Fields

```
timestamp

status

error

message

path

method

traceId

validationErrors
```

validationErrors should support

```
field

rejectedValue

message
```

---

# Validation Error DTO

Create

ValidationError

Fields

```
field

message

rejectedValue
```

---

# Error Codes

Create reusable error codes.

Examples

```
COMMON_001

COMMON_002

VALIDATION_001

SECURITY_001

DATABASE_001
```

Do not create module-specific error codes.

Those belong inside each module.

---

# HTTP Status Mapping

Map exceptions correctly.

Examples

Validation

400

Resource Not Found

404

Unauthorized

401

Forbidden

403

Conflict

409

Internal Server Error

500

Method Not Allowed

405

Unsupported Media Type

415

Request Timeout

408

Bad Request

400

---

# Logging

Log exceptions using SLF4J.

Requirements

Log

- timestamp
- request URI
- HTTP method
- exception type
- message

Never log

- passwords
- JWT
- refresh tokens

---

# Validation Handling

Support

@NotBlank

@NotNull

@Email

@Pattern

@Size

Return all validation errors.

Do not stop at the first validation error.

---

# API Response

Every exception must return

ApiResponse<ErrorResponse>

Maintain a consistent response structure.

Never return Spring Boot default error pages.

---

# Stack Trace

Hide stack traces in production.

Allow detailed logging only in development profile.

---

# Internationalization

Prepare error messages for future localization.

Do not hardcode all messages.

Use message keys where appropriate.

---

# File Upload Errors

Prepare handlers for

- file size exceeded
- unsupported file type
- missing file

Do not implement file upload.

---

# Database Errors

Handle

DataIntegrityViolationException

Duplicate keys

Foreign key violations

Constraint violations

Never expose SQL statements.

---

# REST Best Practices

Return meaningful HTTP status codes.

Return consistent JSON.

Never return HTML error pages.

---

# Unit Testing

Create test skeletons.

Do not implement test cases.

---

# Documentation

Every class

Every public method

must contain JavaDoc.

---

# Deliverables

Generate

✔ GlobalExceptionHandler

✔ ErrorResponse

✔ ValidationError

✔ Error Codes

✔ Exception Advice

✔ Logging

✔ Validation Handling

✔ REST Error Responses

Nothing else.

---

# Completion Checklist

Verify

☐ GlobalExceptionHandler implemented

☐ ErrorResponse complete

☐ Validation errors supported

☐ HTTP status mapping complete

☐ Logging implemented

☐ Stack traces hidden

☐ REST responses standardized

☐ No business logic added

Stop.

Wait for approval.

Next Module

06_SECURITY.md