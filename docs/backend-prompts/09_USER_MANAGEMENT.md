# 09_USER_MANAGEMENT.md

# HSSA Backend - User Management Module

Follow ALL previous prompts.

- 01_GLOBAL_RULES.md
- 02_PROJECT_SETUP.md
- 03_COMMON_INFRASTRUCTURE.md
- 04_CONFIGURATION.md
- 05_EXCEPTION_HANDLING.md
- 06_SECURITY.md
- 07_AUTHENTICATION.md
- 08_IDENTITY_PROFILE.md

Implement ONLY the User Management module.

Do NOT implement Courses.

Do NOT implement Media.

Do NOT implement Research.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement enterprise User Management for administrators.

Only ADMIN users can access this module.

Do not implement self-profile management.

That belongs to Identity Profile module.

---

# Database

Read the existing database documentation.

Use ONLY existing tables.

Do NOT modify schema.

Do NOT create tables.

Use existing relationships.

Use existing constraints.

---

# Package Structure

user/

```
controller/

service/
    impl/

repository/

entity/

dto/
    request/
    response/

mapper/

validator/

exception/

specification/
```

---

# Features

Implement

✔ View Users

✔ Search Users

✔ Filter Users

✔ User Details

✔ Activate User

✔ Deactivate User

✔ Lock User

✔ Unlock User

✔ Suspend User

✔ Restore User

✔ Soft Delete User

✔ Assign Role

✔ Remove Role

✔ View Roles

✔ View Permissions

✔ Assign Permissions

✔ Remove Permissions

✔ Reset User Password (Admin)

✔ Force Logout User

✔ View User Sessions

✔ View Login History

✔ Export User List

✔ Bulk Activate

✔ Bulk Deactivate

✔ Bulk Delete

✔ Bulk Role Assignment

---

# APIs

GET

/api/v1/admin/users

GET

/api/v1/admin/users/{id}

POST

/api/v1/admin/users/search

PUT

/api/v1/admin/users/{id}

PATCH

/api/v1/admin/users/{id}/activate

PATCH

/api/v1/admin/users/{id}/deactivate

PATCH

/api/v1/admin/users/{id}/lock

PATCH

/api/v1/admin/users/{id}/unlock

PATCH

/api/v1/admin/users/{id}/roles

PATCH

/api/v1/admin/users/{id}/permissions

PATCH

/api/v1/admin/users/{id}/reset-password

DELETE

/api/v1/admin/users/{id}

POST

/api/v1/admin/users/bulk

GET

/api/v1/admin/users/export

---

# Search

Support

Username

Email

Role

Status

Department

Institution

Country

Registration Date

Created Date

Last Login

Account Status

Use

Spring Specification

Pageable

Sorting

Filtering

---

# Pagination

Support

Page

Size

Sort

Direction

Search

Filtering

---

# Business Rules

Only ADMIN can access.

Cannot delete yourself.

Cannot remove last ADMIN.

Cannot deactivate own account.

Cannot remove system roles.

Cannot assign invalid roles.

Soft delete only.

---

# Role Management

Implement

Assign Role

Remove Role

View Roles

Role History

Future-ready for multiple roles.

---

# Permission Management

Implement

Assign Permission

Remove Permission

View Permissions

Use RBAC.

Do not hardcode permissions.

---

# Password Management

Admin Reset Password

Generate temporary password.

Force password change on next login.

Log action.

---

# Sessions

View user sessions.

Terminate selected session.

Terminate all sessions.

---

# Login History

View

Login Time

Logout Time

IP Address

Device

Browser

Status

---

# DTOs

Create

CreateUserRequest

UpdateUserRequest

SearchUserRequest

AssignRoleRequest

AssignPermissionRequest

ResetPasswordRequest

BulkOperationRequest

UserResponse

UserSummaryResponse

UserDetailsResponse

RoleResponse

PermissionResponse

SessionResponse

LoginHistoryResponse

---

# Validation

Validate

UUID

Email

Username

Roles

Permissions

Pagination

Search

Bulk operations

---

# Logging

Log

User Created

User Updated

User Deleted

Role Assigned

Permission Assigned

Password Reset

Account Locked

Account Unlocked

Account Activated

Account Deactivated

Never log passwords.

Never log JWT.

---

# Events

Create

UserActivatedEvent

UserDeactivatedEvent

RoleAssignedEvent

PermissionAssignedEvent

UserDeletedEvent

PasswordResetEvent

---

# Security

Require

ROLE_ADMIN

Use

@PreAuthorize

Secure every endpoint.

---

# Documentation

Every class

Every method

Every public API

must contain JavaDoc.

---

# Unit Testing

Create test skeletons.

Do not implement tests.

---

# Deliverables

Generate

✔ Controllers

✔ Services

✔ Repositories

✔ DTOs

✔ Specifications

✔ Validators

✔ Mappers

✔ Role Management

✔ Permission Management

✔ Session Management

✔ Login History

✔ Bulk Operations

✔ Search APIs

Nothing else.

---

# Completion Checklist

☑ CRUD complete

☑ Search complete

☑ Filtering complete

☑ Pagination complete

☑ Role Management complete

☑ Permission Management complete

☑ Session Management complete

☑ Login History complete

☑ Bulk Operations complete

☑ Admin Security enforced

Stop.

Wait for approval.

Next Module

10_MEDIA.md