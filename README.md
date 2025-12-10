# 🏦 CoopCredit Microservices System

Distributed cooperative credit management system based on microservices architecture, designed to be scalable, resilient, and observable.

---

## 📋 System Description

CoopCredit is a platform that enables comprehensive management of the credit lifecycle for a cooperative. The system handles everything from the creation of applications by affiliates or analysts, through automatic risk assessment, to final approval or rejection.

### Key Features
- **Hexagonal Architecture**: Domain core isolated from frameworks and external dependencies.
- **Security**: Robust authentication and authorization using JWT.
- **Risk Assessment**: Integration with a rule engine for credit scoring.
- **Full Observability**: Metrics (Prometheus/Grafana), Health Checks (Actuator), and Traceability.

---

## � Use Case Diagram

```mermaid
graph TB
    subgraph Actors
        Afiliado((👤 AFILIADO))
        Analista((👔 ANALISTA))
        Admin((🔧 ADMIN))
    end

    subgraph "Authentication System"
        UC1[🔐 Login]
        UC2[📝 Register User]
        UC3[🔄 Refresh Token]
        UC4[✅ Validate Token]
    end

    subgraph "Credit Application System"
        UC5[📋 Create Credit Application]
        UC6[🔍 View My Applications]
        UC7[📊 View All Applications]
        UC8[✔️ Approve/Reject Application]
        UC9[📈 Check Application Status]
    end

    subgraph "Affiliate Management"
        UC10[👥 Create Affiliate]
        UC11[📝 Update Affiliate]
        UC12[🗑️ Delete Affiliate]
        UC13[🔍 List Affiliates]
    end

    subgraph "Risk Assessment System"
        UC14[⚠️ Evaluate Risk]
        UC15[📊 Calculate Risk Score]
    end

    %% Afiliado connections
    Afiliado --> UC1
    Afiliado --> UC5
    Afiliado --> UC6
    Afiliado --> UC9

    %% Analista connections
    Analista --> UC1
    Analista --> UC5
    Analista --> UC7
    Analista --> UC8
    Analista --> UC9
    Analista --> UC13

    %% Admin connections
    Admin --> UC1
    Admin --> UC2
    Admin --> UC3
    Admin --> UC4
    Admin --> UC5
    Admin --> UC7
    Admin --> UC8
    Admin --> UC10
    Admin --> UC11
    Admin --> UC12
    Admin --> UC13

    %% Internal use cases (extend/include)
    UC5 -.->|includes| UC14
    UC14 -.->|includes| UC15
    UC8 -.->|extends| UC7
```

---

## �🏗️ Hexagonal Architecture

The system follows a **Hexagonal Architecture (Ports and Adapters)** to ensure domain independence and clean separation of concerns.

### Architecture Overview

```mermaid
graph TB
    subgraph "Infrastructure Layer - Driving/Input Adapters"
        REST[🌐 REST Controllers]
        SEC[🔐 Security Filters]
    end

    subgraph "Application Layer"
        DTO[📦 DTOs]
        MAP[🔄 Mappers]
        SVC[⚙️ Application Services]
        UC[📋 Use Cases Implementation]
    end

    subgraph "Domain Layer - Core"
        direction TB
        PIN[📥 Input Ports - Use Case Interfaces]
        MOD[🎯 Domain Models - Entities & Value Objects]
        EXC[⚠️ Domain Exceptions]
        POUT[📤 Output Ports - Repository Interfaces]
    end

    subgraph "Infrastructure Layer - Driven/Output Adapters"
        REPO[💾 JPA Repository Adapters]
        ENT[📊 JPA Entities]
        HTTP[🌐 HTTP Clients - RestTemplate/WebClient]
        JWT[🔑 JWT Adapter]
        PWD[🔒 Password Encoder]
    end

    subgraph "External Systems"
        DB[(🐘 PostgreSQL)]
        RISK[⚠️ Risk Service]
        EUREKA[📡 Eureka Server]
    end

    REST --> PIN
    SEC --> PIN
    PIN --> UC
    UC --> MOD
    UC --> POUT
    POUT --> REPO
    POUT --> HTTP
    POUT --> JWT
    POUT --> PWD
    REPO --> ENT
    ENT --> DB
    HTTP --> RISK
    REST -.-> EUREKA
```

### Layer Details

| Layer | Package | Responsibility |
|-------|---------|----------------|
| **Domain** | `domain.models`, `domain.port.in`, `domain.port.out`, `domain.exception` | Business logic, entities, use case interfaces (ports) |
| **Application** | `application.usecases`, `application.dto`, `application.mapper`, `application.services` | Use case implementation, DTO transformation |
| **Infrastructure** | `infrastructure.controller`, `infrastructure.adapters`, `infrastructure.entities`, `infrastructure.security` | REST endpoints, JPA implementation, external integrations |

### Ports and Adapters Detail

```mermaid
graph LR
    subgraph "Input Ports - domain.port.in"
        IPC[CreateCreditApplicationUseCase]
        IPR[RetrieveCreditApplicationUseCase]
        IPD[ProcessCreditDecisionUseCase]
        IPA[AuthenticateUserUseCase]
        IPU[RegisterUserUseCase]
    end

    subgraph "Output Ports - domain.port.out"
        OPR[CreditApplicationRepositoryPort]
        OPA[AffiliateRepositoryPort]
        OPK[RiskAssessmentPort]
        OPU[UserRepositoryPort]
        OPJ[JwtTokenPort]
    end

    subgraph "Input Adapters"
        CC[CreditApplicationController]
        AC[AuthController]
        AFC[AffiliateController]
    end

    subgraph "Output Adapters"
        CRA[CreditApplicationRepositoryAdapter]
        ARA[AffiliateRepositoryAdapter]
        RSA[RiskAssessmentAdapter]
        URA[UserRepositoryAdapter]
        JTA[JwtTokenAdapter]
    end

    CC -->|implements| IPC
    CC -->|implements| IPR
    CC -->|implements| IPD
    AC -->|implements| IPA
    AC -->|implements| IPU

    OPR -->|implemented by| CRA
    OPA -->|implemented by| ARA
    OPK -->|implemented by| RSA
    OPU -->|implemented by| URA
    OPJ -->|implemented by| JTA
```

---

## 🔗 Microservices Diagram

### System Architecture

```mermaid
graph TB
    subgraph "External"
        CLIENT[🖥️ Client Application]
    end

    subgraph "Edge Layer"
        GW[🚪 API Gateway - Port 8080]
    end

    subgraph "Infrastructure Services"
        CONFIG[⚙️ Config Server - Port 8888]
        EUREKA[📡 Eureka Server - Port 8761]
    end

    subgraph "Business Microservices"
        AUTH[🔐 Auth Service - Port 8081]
        CREDIT[💳 Credit Service - Port 8082]
        RISK[⚠️ Risk Service - Port 8083]
    end

    subgraph "Data Layer"
        DB_AUTH[(🐘 PostgreSQL Auth - Port 5434)]
        DB_CREDIT[(🐘 PostgreSQL Credit - Port 5433)]
    end

    subgraph "Observability"
        PROM[📊 Prometheus - Port 9090]
        GRAF[📈 Grafana - Port 3000]
    end

    CLIENT -->|HTTP/REST| GW
    GW -->|/api/auth/**| AUTH
    GW -->|/api/credit/**| CREDIT
    GW -->|/api/risk/**| RISK

    AUTH --> DB_AUTH
    CREDIT --> DB_CREDIT
    CREDIT -->|Risk Evaluation| RISK

    AUTH -.->|Register| EUREKA
    CREDIT -.->|Register| EUREKA
    RISK -.->|Register| EUREKA
    GW -.->|Register| EUREKA

    AUTH -.->|Get Config| CONFIG
    CREDIT -.->|Get Config| CONFIG
    RISK -.->|Get Config| CONFIG
    GW -.->|Get Config| CONFIG

    PROM -->|Scrape /actuator/prometheus| AUTH
    PROM -->|Scrape /actuator/prometheus| CREDIT
    PROM -->|Scrape /actuator/prometheus| RISK
    GRAF -->|Query| PROM
```

### Service Communication Flow

```mermaid
sequenceDiagram
    participant C as 🖥️ Client
    participant G as 🚪 Gateway
    participant A as 🔐 Auth Service
    participant CR as 💳 Credit Service
    participant R as ⚠️ Risk Service
    participant DB as 🐘 PostgreSQL

    Note over C,DB: 1. Authentication Flow
    C->>G: POST /api/auth/login
    G->>A: Forward request
    A->>DB: Validate credentials
    DB-->>A: User data
    A-->>G: JWT Token
    G-->>C: JWT Token

    Note over C,DB: 2. Credit Application Flow
    C->>G: POST /api/credit/applications with Authorization Bearer JWT
    G->>G: Validate JWT
    G->>CR: Forward request
    CR->>R: POST /risk/evaluation
    R-->>CR: Risk Score & Level
    CR->>DB: Save Application
    DB-->>CR: Confirmation
    CR-->>G: Application Created
    G-->>C: 201 Created

    Note over C,DB: 3. Decision Flow - Analyst
    C->>G: PUT /api/credit/applications/id/decision
    G->>CR: Forward request
    CR->>DB: Update Status
    DB-->>CR: Updated
    CR-->>G: Decision Registered
    G-->>C: 200 OK
```

### Ecosystem Services

| Service | Port | Technology | Description |
|----------|--------|------------|-------------|
| **Gateway** | `8080` | Spring Cloud Gateway | Single entry point, routing, and perimeter security. |
| **Auth Service** | `8081` | Spring Security + JWT | User management and token issuance. |
| **Credit Service** | `8082` | Spring Boot | Credit application lifecycle management. |
| **Risk Service** | `8083` | Spring Boot | Risk assessment and scoring engine. |
| **Eureka** | `8761` | Netflix Eureka | Service discovery. |
| **Config Server** | `8888` | Spring Cloud Config | Centralized configuration management. |
| **Prometheus** | `9090` | Prometheus | Metrics collection and storage. |
| **Grafana** | `3000` | Grafana | Metrics visualization and dashboards. |

---

## 📁 Project Structure (Hexagonal Architecture)

```
microservice-credit-application-service/
└── src/main/java/com/riwi/microservice/coopcredit/credit/
    ├── 📂 domain/                          # 🎯 DOMAIN LAYER (Core)
    │   ├── 📂 models/                      # Domain Entities
    │   │   ├── Affiliate.java
    │   │   ├── CreditApplication.java
    │   │   ├── RiskEvaluation.java
    │   │   └── 📂 enums/
    │   ├── 📂 port/
    │   │   ├── 📂 in/                      # Input Ports (Use Cases)
    │   │   │   ├── CreateCreditApplicationUseCase.java
    │   │   │   ├── RetrieveCreditApplicationUseCase.java
    │   │   │   └── ProcessCreditDecisionUseCase.java
    │   │   └── 📂 out/                     # Output Ports (Repositories)
    │   │       ├── CreditApplicationRepositoryPort.java
    │   │       ├── AffiliateRepositoryPort.java
    │   │       └── RiskAssessmentPort.java
    │   └── 📂 exception/                   # Domain Exceptions
    │
    ├── 📂 application/                     # ⚙️ APPLICATION LAYER
    │   ├── 📂 usecases/                    # Use Case Implementations
    │   │   ├── CreateCreditApplicationUseCaseImpl.java
    │   │   ├── RetrieveCreditApplicationUseCaseImpl.java
    │   │   └── ProcessCreditDecisionUseCaseImpl.java
    │   ├── 📂 dto/                         # Data Transfer Objects
    │   ├── 📂 mapper/                      # DTO <-> Domain Mappers
    │   └── 📂 services/                    # Application Services
    │
    └── 📂 infrastructure/                  # 🔌 INFRASTRUCTURE LAYER
        ├── 📂 controller/                  # REST Controllers (Input Adapters)
        ├── 📂 adapters/                    # Output Adapters
        │   ├── CreditApplicationRepositoryAdapter.java
        │   ├── AffiliateRepositoryAdapter.java
        │   └── RiskAssessmentAdapter.java
        ├── 📂 entities/                    # JPA Entities
        ├── 📂 repositories/                # JPA Repositories
        ├── 📂 mapper/                      # Entity <-> Domain Mappers
        ├── 📂 security/                    # Security Configuration
        ├── 📂 config/                      # Infrastructure Config
        └── 📂 exception/                   # Global Exception Handlers
```

---

## 👥 Roles and Business Flow

### System Roles
The system implements Role-Based Access Control (RBAC) extracted from the JWT token:

1.  **ROLE_AFILIADO**: Can create credit applications for themselves and check their status.
2.  **ROLE_ANALISTA**: Can create applications, query any application, and register decisions (approve/reject).
3.  **ROLE_ADMIN**: Full access to the system for management and configuration.

### Credit Application Flow
1.  **Authentication**: User obtains a JWT token via `/api/auth/login`.
2.  **Application**: Sends a `POST` request to the credit service with amount, term, and purpose.
3.  **Automatic Assessment**: The credit service synchronously queries the **Risk Service**.
    *   Score, debt capacity, and risk level are calculated.
4.  **Persistence**: The application is saved with status `PENDIENTE` (Pending) or `EN_REVISION` (Under Review).
5.  **Decision**: An analyst reviews the application and registers approval or rejection.

---

## 🔌 Main Endpoints

### 🔐 Auth Service (`/api/auth`)
- `POST /login`: Login and obtain Token.
- `POST /register`: Register new user.

### 💳 Credit Service (`/api/credit`)
- `POST /applications`: Create new application.
- `GET /applications/{id}`: Get application details.
- `PUT /applications/{id}/decision`: (Analyst) Approve or reject application.

### ⚠️ Risk Service (`/api/risk`)
- `POST /evaluation`: Evaluate risk of a financial profile.

---

## 🚀 Project Execution

### Prerequisites
- Docker and Docker Compose
- Java 17 (for local development)
- Maven 3.8+

### 🐳 Execution with Docker Compose (Recommended)

Starts the entire ecosystem (Databases, Services, Observability):

```bash
docker-compose up -d --build
```

**Access Tools:**
- **Grafana**: [http://localhost:3000](http://localhost:3000) (User: `admin`, Pass: `admin`)
- **Prometheus**: [http://localhost:9090](http://localhost:9090)
- **Eureka Dashboard**: [http://localhost:8761](http://localhost:8761)

### 💻 Local Execution (Development)

1.  **Compile the entire project:**
    ```bash
    mvn clean install -DskipTests
    ```
2.  **Start base infrastructure (DBs, Eureka, Config):**
    ```bash
    docker-compose up -d postgres-auth postgres-credit microservice-eureka microservice-config
    ```
3.  **Run microservice (e.g., Credit Service):**
    ```bash
    cd microservice-credit-application-service
    mvn spring-boot:run
    ```

---

## 📊 Observability and Monitoring

The system implements a full observability stack:

### Metrics (Actuator + Prometheus)
Each microservice exposes metrics at `/actuator/prometheus`.
- **Key Metrics**:
    - `http_server_requests_seconds`: Latency and throughput per endpoint.
    - `security_authentication_failures_total`: Failed login attempts.
    - `jdbc_connections_active`: Connection pool usage.

### Logs
Structured logging in console, collectible by tools like ELK or Loki (not included in this basic compose).

---

## 🧪 Testing and Quality

### Running Tests
The project includes unit and integration tests using **Testcontainers**.

```bash
mvn test
```

### Test Evidence
*(Placeholder for JUnit/Surefire report screenshots)*
> ![Test Results Placeholder](https://via.placeholder.com/800x200?text=Successful+Tests+Screenshot)

### Metrics Evidence (Grafana)
*(Placeholder for Grafana dashboard screenshot)*
> ![Grafana Dashboard Placeholder](https://via.placeholder.com/800x400?text=Grafana+Metrics+Dashboard)

---

## 🛠️ Technologies Used

- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Cloud**: Spring Cloud (Gateway, Config, Eureka)
- **Database**: PostgreSQL
- **Migrations**: Flyway
- **Mapping**: MapStruct
- **Testing**: JUnit 5, Mockito, Testcontainers
- **Containers**: Docker
