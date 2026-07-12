# 06_SECURITY.md

# HSSA Backend - Security Infrastructure Module

Follow ALL rules defined in

- 01_GLOBAL_RULES.md
- 02_PROJECT_SETUP.md
- 03_COMMON_INFRASTRUCTURE.md
- 04_CONFIGURATION.md
- 05_EXCEPTION_HANDLING.md

Do not repeat them.

Implement ONLY the Security Infrastructure.

Do NOT implement Login.

Do NOT implement Register.

Do NOT implement JWT generation.

Do NOT implement Refresh Tokens.

Do NOT implement business modules.

Stop after completing this module.

Wait for approval.

---

# Objective

Create the complete Spring Security infrastructure that will support the Authentication module.

This module only prepares security.

Authentication logic belongs to the next module.

---

# Package Structure

Create

security/

```
security
│
├── config
├── filter
├── handler
├── jwt
├── service
├── annotation
├── permission
├── util
└── constant
```

---

# Responsibilities

Create ONLY the security infrastructure.

Prepare

- Spring Security
- Authentication Filter Chain
- Authorization Rules
- Password Encoder
- Security Context Utilities
- Authentication Entry Point
- Access Denied Handler
- Authentication Success Handler
- Authentication Failure Handler

Do NOT implement login.

---

# Security Configuration

Create

SecurityConfig

Requirements

- Stateless Session
- CSRF disabled
- CORS enabled
- SecurityFilterChain
- BCryptPasswordEncoder
- AuthenticationManager
- AuthenticationProvider

Prepare JWT Filter registration.

Do not implement JWT parsing.

---

# Password Encoding

Create

PasswordEncoder Bean

Use

BCrypt

Never use NoOpPasswordEncoder.

---

# Authentication Entry Point

Create

JwtAuthenticationEntryPoint

Handle

401 Unauthorized

Return

ApiResponse<ErrorResponse>

---

# Access Denied Handler

Create

AccessDeniedHandler

Handle

403 Forbidden

Return consistent JSON.

---

# Security Context Utility

Create

SecurityUtils

Methods

- getCurrentUserId()
- getCurrentUsername()
- getCurrentUserRoles()
- isAuthenticated()

Return Optional where appropriate.

Do not assume authentication exists.

---

# Public Endpoint Configuration

Prepare public endpoints only.

Examples

```
/api/v1/auth/**

/swagger-ui/**

/v3/api-docs/**

/actuator/health
```

Everything else should require authentication.

---

# Role Constants

Prepare

```
ROLE_ADMIN

ROLE_INSTRUCTOR

ROLE_USER
```

Do not implement permissions.

---

# Security Filters

Prepare

JwtAuthenticationFilter

Do not implement JWT validation.

Only prepare the filter structure.

---

# Authentication Provider

Prepare AuthenticationProvider.

Implementation belongs to Authentication module.

---

# UserDetails

Prepare

CustomUserDetails

CustomUserDetailsService interface

Do not implement database lookup.

---

# Security Events

Prepare event listeners.

Examples

AuthenticationSuccessEvent

AuthenticationFailureEvent

Do not implement logging.

---

# Method Security

Enable

```
@EnableMethodSecurity
```

Future-ready for

```
@PreAuthorize

@PostAuthorize
```

---

# Unit Testing

Create test skeletons.

Do not implement tests.

---

# Deliverables

Generate only

✔ SecurityConfig

✔ PasswordEncoder

✔ AuthenticationEntryPoint

✔ AccessDeniedHandler

✔ SecurityUtils

✔ JwtAuthenticationFilter (structure only)

✔ Role Constants

✔ CustomUserDetails

✔ UserDetailsService interface

✔ Method Security

Nothing else.

---

# Completion Checklist

Verify

☑ Stateless Security configured

☑ BCrypt configured

☑ Filter chain configured

☑ Method Security enabled

☑ Public endpoints configured

☑ No authentication logic implemented

☑ No JWT generation

☑ Ready for Authentication Module

Stop.

Wait for approval.

Next Module

07_AUTHENTICATION.md