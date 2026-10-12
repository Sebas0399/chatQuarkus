# API & Frontend Integration Guide

This document describes the architecture, authentication, multi-tenancy model, and API endpoints for integrating the Angular frontend with the Quarkus backend.

---

## 1. Authentication & Security (JWT)

- **Endpoint**: `POST /api/auth/login`
- **Payload**: `{ "username": "...", "password": "..." }`
- **Response**: `{ "token": "eyJhbGciOi..." }`
- **Token Storage**: Stored in `localStorage` via `TokenService`.
- **Request Interceptor**: `AuthInterceptor` automatically injects the Bearer token into the `Authorization` header for all requests under `/api/`.

---

## 2. Multi-Tenancy & Roles Architecture

- **Logical Multi-Tenancy (`company_id`)**:
  - Each user belongs to a specific company (`companyId` stored in user records and injected into JWT claims).
  - All operations (contacts, messages, assistants) are scoped to the tenant's `companyId` extracted securely from the JWT claim.
- **Roles**:
  - Users have a `role` attribute (e.g. `ADMIN`, `USER`).
  - Roles are included in the JWT `groups` claim and can be used for endpoint protection (`@RolesAllowed`).

---

## 3. REST API Endpoints

Base URL: `/api`

### Auth
- `POST /auth/login` — Authenticate user and receive JWT.
- `POST /auth/register` — Register a new user and company.

### Contacts
- `GET /contacts` — List all contacts for the authenticated company.
- `POST /contacts` — Create a new contact.

### Messages
- `GET /messages/{contactId}` — Get message history for a contact.
- `POST /messages` — Send a message to a contact.

### Assistant
- `GET /assistant` — Get company AI assistant settings.
- `POST /assistant` — Save/update assistant configuration.
- `POST /assistant/test` — Test JavaScript script in GraalVM sandbox.

---

## 4. Real-Time WebSockets

- **URL**: `ws://<host>/ws/chat`
- **Behavior**: Broadcasts incoming real-time messages so clients update message lists dynamically without full REST reloads.
