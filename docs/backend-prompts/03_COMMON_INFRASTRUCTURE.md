# 03_COMMON_INFRASTRUCTURE.md

# HSSA Backend - Common Infrastructure

Follow ALL rules defined in:

- 01_GLOBAL_RULES.md
- 02_PROJECT_SETUP.md

Do not repeat them.

Implement ONLY the Common Infrastructure.

Do NOT implement Security.

Do NOT implement Authentication.

Do NOT implement any business module.

Stop after completing this module.

Wait for approval before continuing.

---

# Objective

Build the shared backend infrastructure that every module will use.

Everything in this module must be reusable.

No module-specific business logic is allowed.

---

# Package Structure

Create the following structure.

common/

```
common
│
├── entity
├── dto
│   ├── request
│   ├── response
│   └── pagination
│
├── response
├── constants
├── enums
├── mapper
├── validator
├── util
└── annotation
```

---

# Base Entity

Create

BaseEntity.java

Requirements

- UUID Primary Key
- createdAt
- updatedAt
- deletedAt (Soft Delete)
- createdBy
- updatedBy
- version (Optimistic Locking)

Use

- @MappedSuperclass
- @EntityListeners
- @Version

Do not duplicate these fields elsewhere.

Every future entity must extend BaseEntity.

---

# Auditing

Configure auditing support.

Create

AuditConfig.java

Enable

- Created Date
- Last Modified Date
- Created By
- Updated By

Prepare for Spring Security integration later.

---

# Common API Response

Create

ApiResponse<T>

Structure

```
success

message

data

timestamp

path

status
```

Create builder methods

```
success()

created()

updated()

deleted()

failure()
```

Use generics.

All controllers must return ApiResponse.

---

# Pagination

Create

PaginationRequest

PaginationResponse<T>

Support

- page
- size
- sort
- direction

Include metadata

- totalElements
- totalPages
- currentPage
- hasNext
- hasPrevious

Use Spring Pageable.

---

# Constants

Create

ApplicationConstants

Include

Application Name

API Version

Default Page Size

Maximum Page Size

Date Formats

Timezone

Character Encoding

Media Types

JWT Prefix Placeholder

Role Names

Response Messages

Do NOT hardcode constants elsewhere.

---

# Common Enums

Create reusable enums.

Examples

```
Status

ActiveStatus

SortDirection

Gender

Language

YesNo

RecordStatus
```

Do NOT create business-specific enums.

Authentication enums belong to Auth module.

Payment enums belong to Payment module.

Notification enums belong to Notification module.

---

# Utility Classes

Create

DateUtils

StringUtils

ValidationUtils

UUIDUtils

PaginationUtils

ResponseUtils

Do not create JwtUtils.

That belongs to Security.

---

# Custom Annotations

Prepare reusable annotations.

Examples

```
TrimString

NullOrNotBlank

ValidUUID
```

Do not create authentication annotations.

---

# Validation

Create reusable validators.

Examples

UUID Validator

Pagination Validator

File Name Validator

Do not create module validators.

---

# Mapper Configuration

Create

MapperConfig

Configure MapStruct.

Centralize mapper configuration.

---

# JSON Configuration

Prepare common JSON serialization utilities.

Use Jackson best practices.

Do not configure ObjectMapper yet.

That belongs to Config module.

---

# Exception Base

Create

BusinessException

ValidationException

ResourceNotFoundException

ConflictException

UnauthorizedException

ForbiddenException

Do NOT create GlobalExceptionHandler.

That belongs to the Exception module.

---

# Soft Delete

Prepare BaseEntity for soft delete.

Do not implement repository filters yet.

---

# Documentation

Every class

Every method

Every public field

must include JavaDoc.

---

# Unit Testing

Create test skeletons.

Do not implement test cases.

---

# Deliverables

Generate

✔ BaseEntity

✔ ApiResponse

✔ PaginationRequest

✔ PaginationResponse

✔ ApplicationConstants

✔ Common Enums

✔ Utility Classes

✔ MapperConfig

✔ Validators

✔ Custom Annotations

✔ Base Exceptions

✔ Audit Configuration

Nothing else.

---

# Completion Checklist

Verify

☐ BaseEntity complete

☐ Auditing configured

☐ ApiResponse complete

☐ Pagination complete

☐ Constants centralized

☐ Utilities created

☐ Validators created

☐ MapStruct configured

☐ Base exceptions created

☐ No business logic added

Stop.

Wait for approval.

Next module:

04_CONFIGURATION.md