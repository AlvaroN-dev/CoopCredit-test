# 🏦 CoopCredit - Microservices System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Enterprise-grade distributed cooperative credit management system built with microservices architecture, designed for scalability, resilience, and full observability.

---

## 📋 Table of Contents

- [System Overview](#-system-overview)
- [Architecture](#-architecture)
- [Technology Stack](#-technology-stack)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Observability](#-observability--monitoring)
- [Security](#-security)
- [Database Schema](#-database-schema)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)

---

## 🎯 System Overview

CoopCredit is a comprehensive platform for managing the complete credit lifecycle in cooperative organizations, from application submission through automated risk assessment to final approval/rejection decisions.

### Key Features

✅ **Hexagonal Architecture** - Domain-driven design with clear separation of concerns  
✅ **JWT Authentication** - Secure token-based authentication with HS512 algorithm  
✅ **Automated Risk Assessment** - Real-time credit scoring and debt capacity analysis  
✅ **Full Observability** - Prometheus metrics, Grafana dashboards, and Actuator endpoints  
✅ **Service Discovery** - Dynamic service registration with Netflix Eureka  
✅ **Centralized Configuration** - Spring Cloud Config Server  
✅ **API Gateway** - Single entry point with unified Swagger documentation  
✅ **Role-Based Access Control** - Dynamic RBAC with automatic role creation  

---

## 🏗️ Architecture

### System Components

| Component | Port | Technology | Purpose |
|-----------|------|------------|---------|
| **API Gateway** | 8080 | Spring Cloud Gateway | Request routing, JWT validation |
| **Credit Service** | 8082 | Spring Boot | Authentication, credit management |
| **Risk Service** | 8083 | Spring Boot | Risk assessment and scoring |
| **Eureka Server** | 8761 | Netflix Eureka | Service discovery |
| **Config Server** | 8888 | Spring Cloud Config | Configuration management |
| **PostgreSQL** | 5433 | PostgreSQL 16 | Database |
| **Prometheus** | 9090 | Prometheus | Metrics collection |
| **Grafana** | 3000 | Grafana | Metrics visualization |

### Architecture Diagrams

- **Microservices Architecture**: `docs/diagrams/microservices-architecture.puml`
- **Observability Stack**: `docs/diagrams/observability-architecture.puml`
- **Hexagonal Architecture**: `docs/diagrams/hexagonal-architecture.puml`

### Hexagonal Architecture (Credit Service)

```
src/main/java/com/riwi/microservice/coopcredit/credit/
├── domain/                 # Business logic (entities, ports, exceptions)
├── application/            # Use cases, DTOs, mappers
└── infrastructure/         # Adapters (REST, JPA, security, metrics)
```

**Layer Responsibilities:**
- **Domain**: Pure business logic, framework-independent
- **Application**: Use case orchestration, DTO transformation
- **Infrastructure**: External system adapters (REST, database, etc.)

---

## 🛠️ Technology Stack

### Core
- **Java 17** - LTS version
- **Spring Boot 3.3.0** - Application framework
- **Spring Cloud 2023.0.3** - Microservices infrastructure

### Security
- **Spring Security 6** - Authentication & authorization
- **jjwt 0.12.6** - JWT implementation
- **BCrypt** - Password hashing

### Data
- **PostgreSQL 16** - Relational database
- **Spring Data JPA** - ORM layer
- **Flyway 9.22.3** - Database migrations
- **HikariCP** - Connection pooling

### Observability
- **Spring Boot Actuator** - Health checks & metrics
- **Micrometer** - Metrics instrumentation
- **Prometheus** - Metrics collection
- **Grafana** - Visualization & dashboards

### Documentation
- **Springdoc OpenAPI 2.5.0** - API documentation
- **Swagger UI** - Interactive API testing

---

## 🚀 Getting Started

### Prerequisites

- Docker 24.0+ and Docker Compose 2.20+
- Java 17 (for local development)
- Maven 3.9+ (for local development)

### Quick Start with Docker

```bash
# Clone repository
git clone https://github.com/AlvaroN-dev/CoopCredit-test.git
cd CoopCredit-test

# Start all services
docker-compose up -d --build

# Verify services
docker-compose ps
```

### Access Applications

| Service | URL | Credentials |
|---------|-----|-------------|
| **Swagger UI** | http://localhost:8080/swagger-ui.html | N/A |
| **Eureka Dashboard** | http://localhost:8761 | N/A |
| **Prometheus** | http://localhost:9090 | N/A |
| **Grafana** | http://localhost:3000 | admin / admin |

### Local Development

```bash
# Start infrastructure
docker-compose up -d postgres-credit microservice-eureka microservice-config

# Compile project
mvn clean install -DskipTests

# Run services
cd microservice-credit-application-service
mvn spring-boot:run
```

---

## 📚 API Documentation

### Centralized Swagger UI

Access all API documentation: **http://localhost:8080/swagger-ui.html**

### Authentication Endpoints

**Base Path**: `/api/auth`

#### POST `/api/auth/register`

Register new user with role assignment.

**Request:**
```json
{
  "username": "john.doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "roles": ["ADMIN"]
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "john.doe",
  "roles": ["ADMIN"]
}
```

**Available Roles:**
- `ADMIN` - Full system access
- `ANALISTA` - Can manage applications
- `AFILIADO` - Can create own applications

#### POST `/api/auth/login`

Authenticate and receive JWT token (24-hour validity).

**Request:**
```json
{
  "username": "john.doe",
  "password": "SecurePass123!"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "john.doe",
  "roles": ["ADMIN"]
}
```

#### GET `/api/auth/me`

Get current authenticated user details.

**Headers:** `Authorization: Bearer {JWT_TOKEN}`

**Response:**
```json
{
  "username": "john.doe",
  "email": "john@example.com",
  "roles": ["ADMIN"]
}
```

### Credit Application Endpoints

**Base Path**: `/api/credit`

#### POST `/api/credit/applications`

Create new credit application.

**Required Roles**: `AFILIADO`, `ANALISTA`, `ADMIN`

**Request:**
```json
{
  "amount": 50000.00,
  "term": 24,
  "purpose": "Home renovation",
  "affiliateId": 1
}
```

**Response:**
```json
{
  "id": 1,
  "amount": 50000.00,
  "term": 24,
  "status": "PENDING",
  "riskEvaluation": {
    "score": 750,
    "riskLevel": "LOW",
    "recommendation": "APPROVE"
  },
  "createdAt": "2024-12-11T10:30:00Z"
}
```

#### GET `/api/credit/applications/{id}`

Get application details.

**Required Roles**: `AFILIADO` (own), `ANALISTA`, `ADMIN`

#### GET `/api/credit/applications`

List all applications with pagination.

**Required Roles**: `ANALISTA`, `ADMIN`

**Query Parameters:**
- `status` (optional): Filter by status
- `page` (default: 0): Page number
- `size` (default: 20): Page size

#### PUT `/api/credit/applications/{id}/decision`

Approve or reject application.

**Required Roles**: `ANALISTA`, `ADMIN`

**Request:**
```json
{
  "decision": "APPROVED",
  "comments": "Application meets all criteria"
}
```

### Risk Assessment Endpoints

**Base Path**: `/api/risk`

#### POST `/api/risk/evaluation`

Evaluate credit risk (internal service call).

**Request:**
```json
{
  "amount": 50000.00,
  "term": 24,
  "monthlyIncome": 8000.00,
  "currentDebt": 2000.00
}
```

**Response:**
```json
{
  "score": 750,
  "riskLevel": "LOW",
  "debtCapacity": 0.35,
  "recommendation": "APPROVE"
}
```

**Risk Levels:**
- `LOW` (score >= 700) - Recommended approval
- `MEDIUM` (550-699) - Manual review required
- `HIGH` (< 550) - High risk

---

## 📊 Observability & Monitoring

### Actuator Endpoints

Each service exposes:

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Health check with component details |
| `/actuator/metrics` | Available metrics list |
| `/actuator/prometheus` | Prometheus scraping format |

**Example**: http://localhost:8082/actuator/health

### Custom Metrics

**HTTP Metrics:**
- `http_server_requests_seconds` - Request duration histogram
- `http_server_request_count` - Total requests per endpoint
- `http_server_errors` - Error count by type

**Authentication Metrics:**
- `auth_login_success_total` - Successful logins
- `auth_login_failure_total` - Failed login attempts
- `auth_token_validation_success_total` - Valid JWT tokens
- `auth_token_validation_failure_total` - Invalid/expired tokens

**JVM Metrics** (automatic):
- `jvm_memory_used_bytes` - Memory usage
- `jvm_gc_pause_seconds` - GC pause time
- `jvm_threads_live` - Active threads

**Database Metrics** (automatic):
- `jdbc_connections_active` - Active connections
- `hikaricp_connections_usage` - Connection pool

### Prometheus Configuration

**Scraping Configuration** (`prometheus.yml`):

```yaml
scrape_configs:
  - job_name: 'microservice-credit-application-service'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['microservice-credit-application-service:8082']
```

**Verify Scraping:**
1. Open http://localhost:9090
2. Navigate to **Status → Targets**
3. All targets should show `UP` status

**Correct Endpoints:**
- ✅ **UI**: `http://localhost:9090`
- ✅ **Targets**: `http://localhost:9090/targets`
- ✅ **Graph**: `http://localhost:9090/graph`
- ❌ **INCORRECT**: `http://localhost:9090/metrics` (does NOT exist)

### Grafana Setup

**1. Access Grafana:**

URL: http://localhost:3000  
Credentials: `admin` / `admin`

**2. Add Prometheus Data Source:**

1. Go to **Configuration → Data Sources**
2. Add **Prometheus**
3. URL: `http://prometheus:9090`
4. Click **Save & Test**

**3. Import Recommended Dashboards:**

| ID | Name | Description |
|----|------|-------------|
| **12900** | Spring Boot APM | Complete Spring Boot metrics |
| **4701** | JVM Micrometer | Detailed JVM metrics |
| **10280** | Spring Boot Statistics | Application-level stats |

**Example Prometheus Queries:**

```promql
# HTTP Request Rate
rate(http_server_requests_seconds_count[5m])

# Average Response Time (P95)
histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m]))

# Login Success vs Failures
sum(rate(auth_login_success_total[5m])) by (application)

# JVM Heap Memory
jvm_memory_used_bytes{area="heap"}

# Database Connections
jdbc_connections_active{application="microservice-credit-application-service"}
```

For complete setup instructions: [docs/CONFIGURAR_GRAFANA.md](docs/CONFIGURAR_GRAFANA.md)

---

## 🔒 Security

### JWT Authentication

- **Algorithm**: HS512 (HMAC with SHA-512)
- **Token Expiration**: 24 hours
- **Secret Key**: 256-bit minimum (configured via `JWT_SECRET`)

**Token Structure:**
```json
{
  "sub": "username",
  "roles": ["ADMIN", "ANALISTA"],
  "iat": 1702300800,
  "exp": 1702387200
}
```

**Using JWT:**
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### Password Security

- **Algorithm**: BCrypt
- **Strength**: 10 rounds (2^10 iterations)
- **Storage**: Only hashed passwords stored

### Role-Based Access Control

**Dynamic Role Management:**

Roles are created automatically during user registration if they don't exist.

**Role Storage:**
- **Database**: `ROLE_ADMIN`, `ROLE_ANALISTA`, `ROLE_AFILIADO`
- **JWT Token**: `ADMIN`, `ANALISTA`, `AFILIADO` (without prefix)
- **Security Filter**: Adds `ROLE_` prefix during authentication

**Access Matrix:**

| Endpoint | AFILIADO | ANALISTA | ADMIN |
|----------|----------|----------|-------|
| `POST /api/credit/applications` | ✅ Own | ✅ | ✅ |
| `GET /api/credit/applications` | ❌ | ✅ | ✅ |
| `PUT /api/credit/applications/{id}/decision` | ❌ | ✅ | ✅ |
| `POST /api/credit/affiliates` | ❌ | ✅ | ✅ |
| `DELETE /api/credit/affiliates/{id}` | ❌ | ❌ | ✅ |

---

## 🗄️ Database Schema

### Entity-Relationship Model

```
users ←→ user_roles ←→ roles
  ↓
affiliates
  ↓
credit_applications
  ↓
risk_evaluations
```

### Flyway Migrations

**V1__create_schema.sql** - Create all tables
**V2__create_relationships.sql** - Add foreign key constraints
**V3__insert_initial_data.sql** - Seed default data

**Initial Data:**
- Roles: `ROLE_ADMIN`, `ROLE_ANALISTA`, `ROLE_AFILIADO`
- Admin User: `admin` / `admin123`

---

## ❗ Troubleshooting

### Common Issues

#### Database Connection Failures

```bash
# Verify PostgreSQL is running
docker-compose ps postgres-credit

# Restart database
docker-compose restart postgres-credit
```

#### JWT Token Expired

**Symptom**: `401 Unauthorized` after 24 hours  
**Solution**: Request new token via `/api/auth/login`

#### Flyway Migration Errors

```bash
# Reset database and volumes
docker-compose down -v
docker-compose up -d --build
```

#### Prometheus Not Scraping

1. Check targets: http://localhost:9090/targets
2. Verify Actuator endpoint: http://localhost:8082/actuator/prometheus
3. Check `prometheus.yml` configuration

#### Swagger UI Not Loading

```bash
# Verify Gateway is running
docker-compose ps microservice-gateway

# Restart Gateway
docker-compose restart microservice-gateway
```

### Correct Endpoints Reference

| Service | Endpoint | Purpose |
|---------|----------|---------|
| Credit Service | `http://localhost:8082/actuator/health` | Health check |
| Credit Service | `http://localhost:8082/actuator/prometheus` | Metrics |
| Prometheus | `http://localhost:9090` | UI |
| Prometheus | `http://localhost:9090/targets` | Scraping status |
| Grafana | `http://localhost:3000` | Dashboards |
| Swagger | `http://localhost:8080/swagger-ui.html` | API docs |
| Eureka | `http://localhost:8761` | Service registry |

---

## 📄 License

This project is licensed under the **MIT License** - see [LICENSE](LICENSE) file for details.

---


