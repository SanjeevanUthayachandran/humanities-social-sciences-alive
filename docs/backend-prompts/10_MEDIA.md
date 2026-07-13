# 10_MEDIA.md

# HSSA Backend - Media Management Module

Follow ALL previous prompts.

Implement ONLY the Media Management module.

Do NOT implement Courses.

Do NOT implement Lessons.

Do NOT implement Authentication.

Stop after completing this module.

Wait for approval.

---

# Objective

Implement a reusable enterprise Media Management module.

Every module in the system must use this module for handling files.

No module may upload files directly.

Media must be centralized.

---

# Database

Read the database documentation.

Use ONLY existing media tables.

Do NOT modify schema.

Do NOT rename tables.

Do NOT create new tables.

Generate only

- Entity
- Repository
- Service
- Controller

according to the existing database.

---

# Package Structure

media/

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

storage/

processor/
```

---

# Responsibilities

Implement

✔ File Upload

✔ File Download

✔ File Preview

✔ File Delete

✔ Soft Delete

✔ Restore

✔ File Metadata

✔ File Search

✔ File Filtering

✔ File Pagination

✔ File Versioning (future ready)

✔ Image Thumbnail Support

✔ Access Control

✔ Public/Private Files

✔ Signed URL Support (future)

---

# Storage

Current

Local Storage

Future

AWS S3

Design storage using an interface.

Example

```
StorageService

LocalStorageService

S3StorageService
```

Never couple business logic to storage implementation.

---

# Supported File Types

Images

PDF

DOCX

PPTX

XLSX

TXT

ZIP

Videos

Audio

Reject unsupported formats.

---

# Upload Rules

Validate

Maximum Size

Allowed Extensions

MIME Type

Duplicate Detection

Virus Scan Placeholder

Unique File Name

Store Metadata

Never trust client filename.

---

# Metadata

Store

Original Name

Stored Name

Extension

MIME Type

Size

Checksum

Owner

Upload Time

Visibility

Download Count

Last Access

---

# APIs

POST

/api/v1/media/upload

GET

/api/v1/media/{id}

GET

/api/v1/media/download/{id}

GET

/api/v1/media/search

PUT

/api/v1/media/{id}

DELETE

/api/v1/media/{id}

PATCH

/api/v1/media/{id}/restore

PATCH

/api/v1/media/{id}/visibility

---

# Search

Support

Owner

Type

Extension

Visibility

Date

Size

Keyword

Pagination

Sorting

---

# Security

Authenticated users only.

Only owner or ADMIN can modify.

Public files readable without login.

Private files require authorization.

---

# DTOs

Create

UploadRequest

UpdateMetadataRequest

SearchMediaRequest

MediaResponse

MediaSummaryResponse

FileMetadataResponse

DownloadResponse

---

# Validation

Validate

UUID

Filename

Extension

MIME Type

File Size

Visibility

---

# Logging

Log

Upload

Download

Delete

Restore

Visibility Change

Never log file contents.

---

# Events

Create

FileUploadedEvent

FileDeletedEvent

FileDownloadedEvent

VisibilityChangedEvent

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

✔ Storage abstraction

✔ Local storage implementation

✔ DTOs

✔ Validators

✔ Repository

✔ Search

✔ Pagination

✔ Security

✔ Events

Nothing else.

---

# Completion Checklist

☑ Upload works

☑ Download works

☑ Delete works

☑ Restore works

☑ Search works

☑ Pagination works

☑ Metadata stored

☑ Storage abstraction complete

☑ AWS S3 ready

☑ No business module coupling

Stop.

Wait for approval.

Next Module

10_COURSE.md