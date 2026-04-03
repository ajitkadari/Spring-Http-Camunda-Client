# Spring HTTP Camunda Client

**Last Updated:** April 2026

A Spring Boot client application that calls Camunda 8 SaaS REST endpoints using OAuth client credentials.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
  - [Environment Variables](#environment-variables)
  - [Get Required Values from Camunda 8 SaaS](#get-required-values-from-camunda-8-saas)
  - [Network Access Requirements](#network-access-requirements)
  - [Setting Environment Variables](#setting-environment-variables)
    - [macOS and Linux](#macos-and-linux)
    - [Windows PowerShell](#windows-powershell)
    - [Windows Command Prompt](#windows-command-prompt)
  - [Using direnv (Alternative)](#using-direnv-alternative)
- [Request Flow](#request-flow)
- [Running the Application](#running-the-application)
- [API Endpoint](#api-endpoint)
- [API Documentation](#api-documentation)
- [Running Tests](#running-tests)
- [Building a JAR](#building-a-jar)
- [Troubleshooting](#troubleshooting)

---

## Overview

This project provides a minimal, production-style example of:

- configuring a Spring `RestClient` for Camunda API calls
- loading Camunda settings from `camunda.*` properties
- obtaining and caching OAuth access tokens with a `TokenProvider`
- exposing `GET /api/camunda/topology` locally
- forwarding requests to `{camunda.base-url}{camunda.api-path}/topology`

---

## Tech Stack

| Technology | Version |
| --- | --- |
| Java | 25 |
| Spring Boot | 4.0.5 |
| Spring Web MVC | via `spring-boot-starter-webmvc` |
| Spring RestClient | via `spring-boot-starter-restclient` |
| Bean Validation | via `spring-boot-starter-validation` |
| OpenAPI + Swagger UI | `springdoc-openapi-starter-webmvc-ui:3.0.2` |
| Maven | 3.9.14 (via wrapper `mvnw`) |

---

## Project Structure

```text
src/
├── main/
│   ├── java/org/camunda/consulting/httpclient/
│   │   ├── CamundaClientApplication.java        # Spring Boot entry point
│   │   ├── CamundaClientConfig.java             # RestClient and token provider wiring
│   │   ├── CamundaClientProperties.java         # Configuration properties + validation
│   │   ├── CamundaService.java                  # Camunda API service using RestClient
│   │   ├── OpenApiConfig.java                   # OpenAPI metadata configuration
│   │   ├── TokenProvider.java                   # Token abstraction
│   │   ├── ClientCredentialsTokenProvider.java  # OAuth client credentials token flow
│   │   └── CamundaController.java               # HTTP endpoint delegating to CamundaService
│   └── resources/
│       └── application.yaml                     # Env-backed configuration defaults
└── test/
    └── java/org/camunda/consulting/httpclient/
        ├── CamundaClientApplicationTests.java
        ├── CamundaControllerTest.java
        ├── CamundaRestClientTest.java
        ├── CamundaTestSupport.java
        └── ClientCredentialsTokenProviderTest.java
```

---

## Prerequisites

- Java 25 (or compatible with this project)
- Maven 3.x (or use `./mvnw`)
- Camunda 8 SaaS account with client credentials

---

## Configuration

Configuration is defined in `src/main/resources/application.yaml` and is environment-variable driven.

### Environment Variables

| Property | Environment variable | Required | Default | Notes                                                                            |
| --- | --- |----------| --- |----------------------------------------------------------------------------------|
| `camunda.base-url` | `CAMUNDA_BASE_URL` | Yes      | None | Value of environment variable, ZEEBE_REST_ADDRESS, obtained from Camunda Console |
| `camunda.api-path` | `CAMUNDA_API_PATH` | No       | `/v2` | API prefix                                                                       |
| `camunda.auth.token-url` | `CAMUNDA_TOKEN_URL` | No       | `https://login.cloud.camunda.io/oauth/token` | OAuth token endpoint                                                             |
| `camunda.auth.client-id` | `CAMUNDA_CLIENT_ID` | Yes      | None | Must not be blank                                                                |
| `camunda.auth.client-secret` | `CAMUNDA_CLIENT_SECRET` | Yes      | None | Must not be blank                                                                |
| `camunda.auth.audience` | `CAMUNDA_AUDIENCE` | No       | `zeebe.camunda.io` | OAuth audience                                                                   |
| `camunda.auth.scope` | `CAMUNDA_SCOPE` | No       | `Zeebe` | Allowed: `Zeebe`, `Tasklist`, `Operate`                                          |
| `camunda.auth.refresh-skew` | `CAMUNDA_TOKEN_REFRESH_SKEW` | No       | `PT30S` | ISO-8601 duration                                                                |

`camunda.base-url`, `camunda.auth.token-url`, `camunda.auth.client-id`, `camunda.auth.client-secret`, `camunda.auth.audience`, and `camunda.auth.scope` are validated as non-blank at startup. `camunda.auth.token-url`, `camunda.auth.audience`, and `camunda.auth.scope` have defaults from `application.yaml` unless you override them.

### Get Required Values from Camunda 8 SaaS

1. Sign in to Camunda Console.
2. Open your cluster and copy the REST endpoint from `ZEEBE_REST_ADDRESS` for `CAMUNDA_BASE_URL`.
3. Open **API / Client Credentials**.
4. Create or open an M2M credential.
5. Copy:
   - `CAMUNDA_BASE_URL` from `ZEEBE_REST_ADDRESS`
   - `CAMUNDA_CLIENT_ID`
   - `CAMUNDA_CLIENT_SECRET`

### Network Access Requirements

Ensure outbound access from your environment to:

- `ZEEBE_REST_ADDRESS` environment variable obtained from Camunda Console. Format will be: `https://<CAMUNDA_CLUSTER_REGION>.zeebe.camunda.io/<CAMUNDA_CLUSTER_ID>`
- `https://login.cloud.camunda.io/oauth/token`

### Setting Environment Variables

#### macOS and Linux

```zsh
export CAMUNDA_BASE_URL="https://your-camunda-endpoint"
export CAMUNDA_API_PATH="/v2"
export CAMUNDA_TOKEN_URL="https://login.cloud.camunda.io/oauth/token"
export CAMUNDA_CLIENT_ID="your-client-id"
export CAMUNDA_CLIENT_SECRET="your-client-secret"
export CAMUNDA_AUDIENCE="zeebe.camunda.io"
export CAMUNDA_SCOPE="Zeebe"
export CAMUNDA_TOKEN_REFRESH_SKEW="PT30S"
```

Persist by adding the same exports to `~/.zshrc`, `~/.bashrc`, or `~/.profile`.

#### Windows PowerShell

```powershell
$env:CAMUNDA_BASE_URL="https://your-camunda-endpoint"
$env:CAMUNDA_API_PATH="/v2"
$env:CAMUNDA_TOKEN_URL="https://login.cloud.camunda.io/oauth/token"
$env:CAMUNDA_CLIENT_ID="your-client-id"
$env:CAMUNDA_CLIENT_SECRET="your-client-secret"
$env:CAMUNDA_AUDIENCE="zeebe.camunda.io"
$env:CAMUNDA_SCOPE="Zeebe"
$env:CAMUNDA_TOKEN_REFRESH_SKEW="PT30S"
```

For persistent user variables:

```powershell
setx CAMUNDA_BASE_URL "https://your-camunda-endpoint"
setx CAMUNDA_API_PATH "/v2"
setx CAMUNDA_TOKEN_URL "https://login.cloud.camunda.io/oauth/token"
setx CAMUNDA_CLIENT_ID "your-client-id"
setx CAMUNDA_CLIENT_SECRET "your-client-secret"
setx CAMUNDA_AUDIENCE "zeebe.camunda.io"
setx CAMUNDA_SCOPE "Zeebe"
setx CAMUNDA_TOKEN_REFRESH_SKEW "PT30S"
```

#### Windows Command Prompt

```bat
set CAMUNDA_BASE_URL=https://your-camunda-endpoint
set CAMUNDA_API_PATH=/v2
set CAMUNDA_TOKEN_URL=https://login.cloud.camunda.io/oauth/token
set CAMUNDA_CLIENT_ID=your-client-id
set CAMUNDA_CLIENT_SECRET=your-client-secret
set CAMUNDA_AUDIENCE=zeebe.camunda.io
set CAMUNDA_SCOPE=Zeebe
set CAMUNDA_TOKEN_REFRESH_SKEW=PT30S
```

### Using direnv (Alternative)

If you want `direnv` to inject variables, create `.envrc` in the project root and define `CAMUNDA_*` values there.

```zsh
cd /path/to/your/project
direnv allow
```

Run `direnv allow` again after modifying `.envrc`.

Example `.envrc`:

```sh
export CAMUNDA_BASE_URL="https://your-camunda-endpoint"
export CAMUNDA_API_PATH="/v2"
export CAMUNDA_TOKEN_URL="https://login.cloud.camunda.io/oauth/token"
export CAMUNDA_CLIENT_ID="your-client-id"
export CAMUNDA_CLIENT_SECRET="your-client-secret"
export CAMUNDA_AUDIENCE="zeebe.camunda.io"
export CAMUNDA_SCOPE="Zeebe"
export CAMUNDA_TOKEN_REFRESH_SKEW="PT30S"
```

---

## Request Flow

1. Call `GET /api/camunda/topology`.
2. `CamundaController` delegates to `CamundaService`.
3. `CamundaService` invokes the Camunda `RestClient`.
4. The request interceptor asks `TokenProvider` for a token.
5. `ClientCredentialsTokenProvider` fetches or reuses a cached token.
6. Request is sent with `Authorization: Bearer <token>`.

---

## Running the Application

```zsh
./mvnw spring-boot:run
```

---

## API Endpoint

Local endpoint:

- `GET http://localhost:8080/api/camunda/topology`

Quick check:

```zsh
curl http://localhost:8080/api/camunda/topology
```

This endpoint delegates to `CamundaService`, which calls the configured Camunda Cluster `/topology` endpoint by using the shared Camunda `RestClient` bean.

---

## API Documentation

After starting the app, OpenAPI/Swagger is available at:

- `Swagger UI`: `http://localhost:8080/swagger-ui/index.html`
- `OpenAPI JSON`: `http://localhost:8080/v3/api-docs`

---

## Running Tests

```zsh
./mvnw test
```

---

## Building a JAR

```zsh
./mvnw clean package
```

---

## Troubleshooting

- `camunda.base-url must not be blank`
  - Set `CAMUNDA_BASE_URL` to your Camunda 8 SaaS cluster REST endpoint (`ZEEBE_REST_ADDRESS`).
- `camunda.auth.client-id must be configured` or `camunda.auth.client-secret must be configured`
  - Set non-empty values for `CAMUNDA_CLIENT_ID` and `CAMUNDA_CLIENT_SECRET`.
- `camunda.auth.scope` validation fails
  - Use one of: `Zeebe`, `Tasklist`, `Operate`.
- `camunda.auth.token-url`, `camunda.auth.audience`, or `camunda.auth.scope` must not be blank
  - Remove the blank override or provide a non-empty value. If not overridden, the defaults from `application.yaml` are used.
- Startup binding failures for required properties
  - Ensure `CAMUNDA_BASE_URL`, `CAMUNDA_CLIENT_ID`, and `CAMUNDA_CLIENT_SECRET` are set and non-blank.
- OAuth/token request failures
  - Verify `CAMUNDA_TOKEN_URL`, `CAMUNDA_AUDIENCE`, and network connectivity to Camunda SaaS endpoints.
