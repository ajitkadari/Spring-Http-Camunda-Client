# Spring HTTP Camunda Client

Minimal Spring Boot 4 example showing how to use `spring-boot-starter-restclient` to call a Camunda 8 REST endpoint with bearer tokens generated through OAuth client credentials.

## What this project does

- configures a Spring `RestClient` for Camunda API calls
- loads Camunda settings from `camunda.*` properties
- obtains and caches OAuth access tokens with a `TokenProvider`
- exposes a local endpoint at `GET /api/camunda/topology`
- forwards that request to the configured Camunda `/v2/topology` endpoint

## Main classes

- `CamundaClientConfig` wires the `RestClient` and token provider beans
- `CamundaClientProperties` is a compact record-based `@ConfigurationProperties` model
- `TokenProvider` is the small abstraction for retrieving an access token
- `ClientCredentialsTokenProvider` fetches and caches tokens from the OAuth token endpoint
- `CamundaController` exposes the local `/api/camunda/topology` endpoint

## Configuration

Configuration is defined in `src/main/resources/application.yaml` and can be overridden with environment variables.

| Property | Environment variable | Description |
| --- | --- | --- |
| `camunda.base-url` | `CAMUNDA_BASE_URL` | Camunda cluster base URL, for example `https://your-camunda-endpoint` |
| `camunda.api-path` | `CAMUNDA_API_PATH` | REST API path, defaults to `/v2` |
| `camunda.auth.token-url` | `CAMUNDA_TOKEN_URL` | OAuth token endpoint, defaults to `https://login.cloud.camunda.io/oauth/token` |
| `camunda.auth.client-id` | `CAMUNDA_CLIENT_ID` | OAuth client id |
| `camunda.auth.client-secret` | `CAMUNDA_CLIENT_SECRET` | OAuth client secret |
| `camunda.auth.audience` | `CAMUNDA_AUDIENCE` | Audience for OAuth token request, defaults to `zeebe.camunda.io` |
| `camunda.auth.scope` | `CAMUNDA_SCOPE` | Optional OAuth scope |
| `camunda.auth.refresh-skew` | `CAMUNDA_TOKEN_REFRESH_SKEW` | Token refresh buffer, defaults to `PT30S` |

### Get required values from Camunda 8 SaaS

You can get `CAMUNDA_BASE_URL`, `CAMUNDA_CLIENT_ID`, and `CAMUNDA_CLIENT_SECRET` from the Camunda Console:

1. Sign in to the Camunda Console for your organization.
2. Open your cluster and copy the cluster REST endpoint host.
   - Use that as `CAMUNDA_BASE_URL` (for example `https://<cluster-region>.zeebe.camunda.io/<cluster-id>`).
3. Open **API / Client Credentials** (wording can vary slightly by Console version).
4. Create a client credential if you do not already have one.
5. Copy the generated values:
   - **Client ID** -> `CAMUNDA_CLIENT_ID`
   - **Client Secret** -> `CAMUNDA_CLIENT_SECRET`

Keep `CAMUNDA_CLIENT_SECRET` secure. Camunda usually only shows it once when created.

## Environment variable setup

### macOS and Linux

For the current terminal session:

```zsh
export CAMUNDA_BASE_URL="https://your-camunda-endpoint"
export CAMUNDA_API_PATH="/v2"
export CAMUNDA_TOKEN_URL="https://login.cloud.camunda.io/oauth/token"
export CAMUNDA_CLIENT_ID="your-client-id"
export CAMUNDA_CLIENT_SECRET="your-client-secret"
export CAMUNDA_AUDIENCE="zeebe.camunda.io"
export CAMUNDA_SCOPE=""
export CAMUNDA_TOKEN_REFRESH_SKEW="PT30S"
```

To keep them across sessions, add the same `export ...` lines to your shell profile, for example:

- `~/.zshrc` on macOS when using `zsh`
- `~/.bashrc` or `~/.profile` on Linux

### Windows PowerShell

For the current PowerShell session:

```powershell
$env:CAMUNDA_BASE_URL="https://your-camunda-endpoint"
$env:CAMUNDA_API_PATH="/v2"
$env:CAMUNDA_TOKEN_URL="https://login.cloud.camunda.io/oauth/token"
$env:CAMUNDA_CLIENT_ID="your-client-id"
$env:CAMUNDA_CLIENT_SECRET="your-client-secret"
$env:CAMUNDA_AUDIENCE="zeebe.camunda.io"
$env:CAMUNDA_SCOPE=""
$env:CAMUNDA_TOKEN_REFRESH_SKEW="PT30S"
```

To persist them for future sessions, use `setx`:

```powershell
setx CAMUNDA_BASE_URL "https://your-camunda-endpoint"
setx CAMUNDA_API_PATH "/v2"
setx CAMUNDA_TOKEN_URL "https://login.cloud.camunda.io/oauth/token"
setx CAMUNDA_CLIENT_ID "your-client-id"
setx CAMUNDA_CLIENT_SECRET "your-client-secret"
setx CAMUNDA_AUDIENCE "zeebe.camunda.io"
setx CAMUNDA_SCOPE ""
setx CAMUNDA_TOKEN_REFRESH_SKEW "PT30S"
```

Open a new PowerShell window after using `setx`.

### Windows Command Prompt

For the current `cmd.exe` session:

```bat
set CAMUNDA_BASE_URL=https://your-camunda-endpoint
set CAMUNDA_API_PATH=/v2
set CAMUNDA_TOKEN_URL=https://login.cloud.camunda.io/oauth/token
set CAMUNDA_CLIENT_ID=your-client-id
set CAMUNDA_CLIENT_SECRET=your-client-secret
set CAMUNDA_AUDIENCE=zeebe.camunda.io
set CAMUNDA_SCOPE=
set CAMUNDA_TOKEN_REFRESH_SKEW=PT30S
```

### Alternate option: direnv

This project also includes a single `.envrc` file with the same `CAMUNDA_*` variables used by `application.yaml`.

To load them with `direnv`:

```zsh
cd "/Users/ajit.kadari/github-local/Spring-Http-Camunda-Client"
direnv allow
```

You can then edit `.envrc` directly with your Camunda values.

## Example `.envrc` values

```sh
export CAMUNDA_BASE_URL="https://your-camunda-endpoint"
export CAMUNDA_API_PATH="/v2"
export CAMUNDA_TOKEN_URL="https://login.cloud.camunda.io/oauth/token"
export CAMUNDA_CLIENT_ID="your-client-id"
export CAMUNDA_CLIENT_SECRET="your-client-secret"
export CAMUNDA_AUDIENCE="zeebe.camunda.io"
export CAMUNDA_SCOPE=""
export CAMUNDA_TOKEN_REFRESH_SKEW="PT30S"
```

## Request flow

1. Call `GET /api/camunda/topology` on this application.
2. `CamundaController` invokes the configured Camunda API `RestClient`.
3. A request interceptor asks `TokenProvider` for an access token.
4. `ClientCredentialsTokenProvider` fetches or reuses a cached token.
5. The request is sent to `{camunda.base-url}{camunda.api-path}/topology` with `Authorization: Bearer ...`.

## Project structure

- `src/main/java/org/camunda/consulting/httpclient/SpringHttpCamundaClientApplication.java`
- `src/main/java/org/camunda/consulting/httpclient/CamundaClientConfig.java`
- `src/main/java/org/camunda/consulting/httpclient/CamundaClientProperties.java`
- `src/main/java/org/camunda/consulting/httpclient/TokenProvider.java`
- `src/main/java/org/camunda/consulting/httpclient/ClientCredentialsTokenProvider.java`
- `src/main/java/org/camunda/consulting/httpclient/CamundaController.java`

## Run

```zsh
./mvnw spring-boot:run
```

Then call:

```zsh
curl http://localhost:8080/api/camunda/topology
```

## Test

```zsh
./mvnw test
```




