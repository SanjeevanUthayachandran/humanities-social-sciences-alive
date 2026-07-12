# 07_AUTHENTICATION.md

# HSSA Backend - Authentication Module

Follow ALL rules defined in

- 01_GLOBAL_RULES.md
- 02_PROJECT_SETUP.md
- 03_COMMON_INFRASTRUCTURE.md
- 04_CONFIGURATION.md
- 05_EXCEPTION_HANDLING.md
- 06_SECURITY.md

Do not repeat them.

Implement ONLY the Authentication module.

Do NOT implement User Management.

Do NOT implement Courses.

Do NOT implement Media.

Do NOT implement any business modules.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a complete enterprise-grade Authentication module.

The implementation must integrate with the existing PostgreSQL database.

Do NOT modify the database schema.

Do NOT create new authentication tables.

Use only the tables defined in the existing database design.

---

# Database

Read the database documentation before generating code.

Use the existing authentication tables.

Examples include (use the exact names from the database):

- users
- roles
- permissions
- user_roles
- role_permissions
- refresh_tokens
- password_reset_tokens
- email_verification_tokens
- login_history
- user_sessions

Do not invent additional tables.

---

# Package Structure

Create

auth/

```
auth
│
├── controller
├── service
│   └── impl
├── repository
├── entity
├── dto
│   ├── request
│   └── response
├── mapper
├── validator
├── exception
├── enums
├── event
├── listener
└── util
```

---

# Authentication Features

Implement

✔ Register

✔ Login

✔ Logout

✔ Refresh Token

✔ Forgot Password

✔ Reset Password

✔ Change Password

✔ Verify Email

✔ Resend Verification Email

✔ Token Validation

✔ Current User

✔ Session Validation

---

# JWT

Implement

Access Token

Refresh Token

Token Validation

Token Expiration

Claims

Subject

Roles

Authorities

JWT Utility

JWT Service

JWT Provider

JWT Filter integration

Never store JWT inside database.

Store Refresh Tokens only.

---

# Registration

Requirements

Validate

- email uniqueness

- username uniqueness

- password policy

- role assignment

Automatically

- hash password

- assign default USER role

- create email verification token

- send verification event

Do not automatically activate account.

---

# Login

Authenticate using

username OR email

plus password.

After successful login

Generate

- Access Token

- Refresh Token

Return

AuthenticationResponse

Update

Login History

Session

Last Login

---

# Logout

Invalidate

Refresh Token

Session

Security Context

---

# Password Management

Implement

Forgot Password

Reset Password

Change Password

Requirements

Old password verification

Password confirmation

Password policy

Password hashing

Password history (future ready)

---

# Email Verification

Generate

Verification Token

Expiration

Resend Token

Activate account

Reject expired tokens.

---

# Refresh Token

Store

Refresh Token

Expiration

Status

User

Device information (future ready)

IP (future ready)

Support

Revoke

Rotation

Expiration

---

# User Details

Implement

CustomUserDetailsService

Load user by

username

or

email

Integrate with Spring Security.

---

# DTOs

Create

Request DTOs

RegisterRequest

LoginRequest

RefreshTokenRequest

ForgotPasswordRequest

ResetPasswordRequest

ChangePasswordRequest

VerifyEmailRequest

LogoutRequest

Response DTOs

AuthenticationResponse

CurrentUserResponse

TokenResponse

RefreshTokenResponse

UserSummaryResponse

---

# Validation

Use Jakarta Validation.

Examples

@NotBlank

@NotNull

@Email

@Pattern

@Size

Validate every request DTO.

---

# Events

Prepare

UserRegisteredEvent

UserLoggedInEvent

UserLoggedOutEvent

PasswordResetEvent

EmailVerifiedEvent

Do not integrate email service yet.

---

# Business Rules

Passwords

BCrypt

Minimum password strength

Email unique

Username unique

Inactive users cannot login

Unverified users cannot login

Disabled users cannot login

Expired tokens rejected

Revoked refresh tokens rejected

---

# Security

Integrate with

Security Module.

Do not duplicate security configuration.

---

# Logging

Log

Login

Logout

Failed Login

Password Change

Verification

Do not log passwords.

Do not log JWT.

---

# Unit Testing

Create test skeletons.

Do not implement tests.

---

# Documentation

Every class

Every public method

must contain JavaDoc.

---

# Deliverables

Generate

✔ Controllers

✔ Services

✔ Repositories

✔ Entities

✔ DTOs

✔ JWT Service

✔ JWT Utility

✔ Refresh Token Service

✔ Authentication Manager

✔ Validators

✔ Events

✔ Mappers

✔ Exceptions

✔ Complete Authentication Flow

Nothing else.

---

# Completion Checklist

Verify

☑ Registration works

☑ Login works

☑ JWT generated

☑ Refresh Token works

☑ Logout works

☑ Email Verification implemented

☑ Password Reset implemented

☑ BCrypt used

☑ Existing database respected

☑ No database schema modified

☑ No business modules implemented

Stop.

Wait for approval.

Next Module

08_USER_MANAGEMENT.md