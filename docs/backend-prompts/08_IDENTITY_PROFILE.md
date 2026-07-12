# 08_IDENTITY_PROFILE.md

# HSSA Backend - Identity & Profile Module

Follow ALL previous prompts.

Implement ONLY the Identity & Profile module.

Do NOT implement administration.

Do NOT implement courses.

Do NOT implement media.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement the authenticated user's identity and profile management.

This module allows every authenticated user to manage their own account.

Only self-service functionality belongs here.

Administration belongs to the next module.

---

# Database

Use ONLY existing tables.

Do not create tables.

Do not modify schema.

Read the existing database documentation.

Use existing relationships.

---

# Package Structure

identity/

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
```

---

# Features

Authenticated user can

✔ View Profile

✔ Update Profile

✔ Change Profile Picture

✔ Delete Profile Picture

✔ Update Contact Information

✔ Update Biography

✔ Update Social Links

✔ Update Academic Information

✔ Update Language Preference

✔ Update Time Zone

✔ Update Notification Preference

✔ Update Privacy Settings

✔ View Account Information

✔ View Login History

✔ View Active Sessions

✔ Terminate Own Session

✔ Delete Own Account (Soft Delete)

---

# APIs

GET

/api/v1/profile

PUT

/api/v1/profile

PATCH

/api/v1/profile/photo

DELETE

/api/v1/profile/photo

GET

/api/v1/profile/sessions

DELETE

/api/v1/profile/sessions/{id}

GET

/api/v1/profile/login-history

DELETE

/api/v1/profile

---

# Business Rules

Only owner may update profile.

Only owner may delete account.

Email change requires verification.

Username uniqueness maintained.

Profile image validation.

Soft delete only.

---

# DTOs

Create request DTOs

UpdateProfileRequest

UpdatePhotoRequest

UpdatePreferencesRequest

UpdatePrivacyRequest

Create response DTOs

ProfileResponse

AccountResponse

SessionResponse

LoginHistoryResponse

PreferenceResponse

---

# Validation

Validate

Email

Phone

URLs

Biography length

Profile image

Language

Timezone

---

# Security

Use authenticated user from SecurityContext.

Never allow updating another user's profile.

---

# Logging

Log

Profile Update

Photo Update

Session Removal

Preference Update

Account Deletion

---

# Deliverables

Generate

✔ Controllers

✔ Services

✔ DTOs

✔ Validators

✔ Mappers

✔ Repository

✔ Profile APIs

✔ Session APIs

✔ Login History APIs

Nothing else.

---

# Completion Checklist

☑ Profile CRUD complete

☑ Session management complete

☑ Login history available

☑ Preferences implemented

☑ Privacy implemented

☑ Security enforced

☑ No administration features

Stop.

Wait for approval.

Next Module

09_USER_MANAGEMENT.md