# 02_PROJECT_SETUP.md

# HSSA Backend - Project Foundation

Follow ALL rules defined in:

01_GLOBAL_RULES.md

Do not repeat those rules.

This prompt only implements the backend project foundation.

Wait for approval before continuing to the next prompt.

---

# Objective

Prepare the backend project so it is ready for enterprise development.

Do NOT implement authentication.

Do NOT implement business modules.

Do NOT generate APIs.

Do NOT generate controllers.

Do NOT generate repositories.

Do NOT generate entities.

Only prepare the project foundation.

---

# Verify Existing Project

Review the current Spring Boot project.

Verify:

- Java 21
- Spring Boot 3.x
- Maven
- PostgreSQL Driver
- Spring Data JPA
- Spring Security
- Validation
- Lombok
- SpringDoc OpenAPI

If anything is missing, report it before making changes.

---

# Verify Project Structure

Verify the project structure.

```
backend/

src/

main/

java/

resources/

test/

pom.xml
```

Do not create duplicate folders.

---

# Create Root Package Structure

Create only the following root packages.

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

Do not create internal folders yet.

Only create the root packages.

---

# Resources Structure

Verify resources folder.

It should contain

```
resources/

application.yml

application-dev.yml

application-prod.yml

logback-spring.xml

banner.txt

static/

templates/

messages/
```

Create missing files if necessary.

Do not configure them yet.

Only prepare them.

---

# Configuration

Verify

application.yml

contains

- application name
- server port
- datasource placeholder
- JPA placeholder
- logging placeholder

Do not configure JWT.

Do not configure Swagger.

Do not configure CORS.

Do not configure Security.

Those belong to later modules.

---

# Maven

Review pom.xml.

Verify

- Java 21
- Spring Boot Parent
- Dependency versions
- Plugin versions

Do not remove dependencies.

Do not add unnecessary libraries.

If additional libraries are recommended, explain why before adding them.

---

# Logging

Prepare project logging.

Create

```
logback-spring.xml
```

Only basic configuration.

Do not implement advanced logging.

---

# Banner

Create

```
banner.txt
```

with project title

Humanities & Social Sciences Alive Backend

and version information.

---

# Constants

Do not create constants yet.

This belongs to Common Infrastructure.

---

# Security

Do not implement.

---

# Authentication

Do not implement.

---

# Database

Do not generate SQL.

Do not modify database schema.

Do not generate entities.

---

# Testing

Verify

JUnit

Mockito

are correctly configured.

Do not create tests.

---

# Build Verification

Verify the project builds successfully.

Verify

```
mvn clean install
```

completes successfully.

Verify

```
mvn spring-boot:run
```

starts without errors.

If any warnings exist, explain them.

---

# Deliverables

Generate only:

✔ Updated project structure

✔ Resource structure

✔ Configuration placeholders

✔ Logging configuration

✔ Banner

✔ Build verification

✔ Recommendations

Do not generate anything else.

---

# Completion Checklist

Verify

☐ Project builds successfully

☐ Spring Boot starts

☐ PostgreSQL connection works

☐ Root package structure exists

☐ Resources folder prepared

☐ Logging prepared

☐ Banner prepared

☐ Maven verified

☐ Java 21 verified

☐ Ready for Common Infrastructure module

Stop after completing this prompt.

Wait for approval before continuing to:

03_COMMON_INFRASTRUCTURE.md