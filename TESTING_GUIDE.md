
# Guía de Pruebas - CoopCredit

## Pre-requisitos

1. Docker Desktop instalado y corriendo
2. Postman o cualquier cliente HTTP
3. Todos los servicios compilados correctamente

## Paso 1: Iniciar los Servicios

```powershell
# En el directorio raíz del proyecto
cd c:\Users\anonimo\Videos\CoopCredit-test

# Limpiar contenedores y volúmenes anteriores
docker-compose down -v

# Iniciar todos los servicios
docker-compose up -d

# Verificar que todos los servicios estén corriendo
docker-compose ps
```

**Servicios esperados:**
- ✅ postgres-credit (puerto 5433)
- ✅ microservice-config (puerto 8888)
- ✅ microservice-eureka (puerto 8761)
- ✅ microservice-gateway (puerto 8080)
- ✅ microservice-credit-application-service (puerto 8082)
- ✅ microservice-risk-central-service (puerto 8083)

## Paso 2: Verificar Health de Servicios

Esperar ~60 segundos para que todos los servicios se inicialicen y registren en Eureka.

**Config Server:**
```
GET http://localhost:8888/actuator/health
```

**Eureka:**
```
GET http://localhost:8761/
```

**Gateway:**
```
GET http://localhost:8080/actuator/health
```

**Credit Service:**
```
GET http://localhost:8082/actuator/health
```

## Paso 3: Verificar Migraciones de Base de Datos

```powershell
# Conectar a la base de datos PostgreSQL
docker exec -it postgres-credit psql -U postgres -d credit_db

# Listar tablas
\dt

# Debería mostrar:
#  affiliates
#  credit_applications
#  flyway_schema_history
#  risk_evaluations
#  roles                    <-- NUEVA
#  users                    <-- NUEVA
#  user_roles               <-- NUEVA

# Verificar roles insertados
SELECT * FROM roles;

# Debería mostrar:
# ROLE_ADMIN
# ROLE_ANALISTA
# ROLE_AFILIADO

# Verificar usuario admin
SELECT username, email, enabled FROM users;

# Salir
\q
```

## Paso 4: Probar Autenticación

### A. Registro de Nuevo Usuario

```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testafiliado",
  "password": "Test@123",
  "email": "test@coopcredit.com",
  "firstName": "Test",
  "lastName": "Afiliado",
  "role": "ROLE_AFILIADO"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 2,
    "username": "testafiliado",
    "email": "test@coopcredit.com",
    "firstName": "Test",
    "lastName": "Afiliado",
    "roles": ["ROLE_AFILIADO"]
  }
}
```

### B. Login con Usuario Admin

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "Admin@123"
}
```

**Respuesta esperada (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@coopcredit.com",
    "firstName": "Admin",
    "lastName": "User",
    "roles": ["ROLE_ADMIN"]
  }
}
```

**⚠️ Guardar el token para las siguientes pruebas!**

### C. Obtener Información del Usuario Actual

```http
GET http://localhost:8080/api/auth/me
Authorization: Bearer <TOKEN_DEL_LOGIN>
```

**Respuesta esperada (200 OK):**
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@coopcredit.com",
  "firstName": "Admin",
  "lastName": "User",
  "roles": ["ROLE_ADMIN"]
}
```

## Paso 5: Probar Endpoints Protegidos

### A. Sin Token (Debería fallar)

```http
GET http://localhost:8080/api/auth/me
```

**Respuesta esperada (401 Unauthorized)**

### B. Con Token Inválido (Debería fallar)

```http
GET http://localhost:8080/api/auth/me
Authorization: Bearer invalid_token_here
```

**Respuesta esperada (401 Unauthorized)**

### C. Con Token Válido (Debería funcionar)

```http
GET http://localhost:8080/api/auth/me
Authorization: Bearer <TOKEN_VALIDO>
```

**Respuesta esperada (200 OK con datos del usuario)**

## Paso 6: Verificar Logs

### Gateway Logs
```powershell
docker logs microservice-gateway -f
```

Buscar:
- ✅ JWT validation messages
- ✅ Route matching for /api/auth/**
- ✅ Forwarding to microservice-credit-application-service

### Credit Service Logs
```powershell
docker logs microservice-credit-application-service -f
```

Buscar:
- ✅ Flyway migrations executed successfully
- ✅ JPA entity scanning (UserEntity, RoleEntity)
- ✅ AuthController endpoints mapped
- ✅ JWT token generation logs
- ✅ Authentication success/failure logs

## Paso 7: Verificar Eureka Dashboard

Abrir en navegador:
```
http://localhost:8761
```

**Servicios registrados esperados:**
- ✅ MICROSERVICE-GATEWAY
- ✅ MICROSERVICE-CREDIT-APPLICATION-SERVICE
- ✅ MICROSERVICE-RISK-CENTRAL-SERVICE

**⚠️ NO debería aparecer MICROSERVICE-AUTH (eliminado)**

## Troubleshooting

### Error: "User not found" al hacer login con admin

**Causa:** Migraciones Flyway no se ejecutaron correctamente.

**Solución:**
```powershell
# Verificar logs del credit service
docker logs microservice-credit-application-service

# Debería mostrar:
# Flyway: Successfully applied 6 migrations
# V1__create_affiliates_schema.sql
# V2__create_credit_applications_schema.sql
# V3__create_risk_evaluations_schema.sql
# V4__insert_initial_data.sql
# V5__create_users_and_roles_schema.sql
# V6__insert_default_users_and_roles.sql

# Si no se ejecutaron, recrear la base de datos
docker-compose down -v
docker-compose up -d
```

### Error: "Invalid credentials" con password correcto

**Causa:** Hash BCrypt del admin no coincide.

**Solución:**
1. Conectar a la base de datos
2. Verificar el hash: `SELECT password FROM users WHERE username = 'admin';`
3. Si no coincide, actualizar: 
   ```sql
   UPDATE users 
   SET password = '$2a$10$TKh8H1.PfQx37YgCzwiKb.KMmq2SxFJN4fDdtZJqJZ8qKqKqKqKqK' 
   WHERE username = 'admin';
   ```

### Error: Gateway no encuentra el servicio

**Causa:** Credit Service no está registrado en Eureka.

**Solución:**
1. Verificar Eureka dashboard: `http://localhost:8761`
2. Esperar ~30 segundos para que el servicio se registre
3. Verificar logs del Credit Service para errores de Eureka

### Error: 403 Forbidden en lugar de 401 Unauthorized

**Causa:** Spring Security bloqueando la petición antes de llegar al filtro JWT.

**Solución:**
1. Verificar SecurityConfig del Credit Service
2. Confirmar que `/auth/login` y `/auth/register` están en `.permitAll()`
3. Verificar que no hay otro SecurityFilterChain conflictivo

### Error: Token expirado

**Causa:** Token JWT expiró (24 horas por defecto).

**Solución:**
1. Hacer login nuevamente para obtener un nuevo token
2. O ajustar `JWT_EXPIRATION` en `.env` para mayor duración

## Colección Postman

Importar esta colección para pruebas rápidas:

```json
{
  "info": {
    "name": "CoopCredit - Auth",
    "_postman_id": "coopcredit-auth",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Register",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"testuser\",\n  \"password\": \"Test@123\",\n  \"email\": \"test@coopcredit.com\",\n  \"firstName\": \"Test\",\n  \"lastName\": \"User\",\n  \"role\": \"ROLE_AFILIADO\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/auth/register",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "auth", "register"]
        }
      }
    },
    {
      "name": "Login",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"admin\",\n  \"password\": \"Admin@123\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/auth/login",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "auth", "login"]
        }
      }
    },
    {
      "name": "Get Current User",
      "request": {
        "method": "GET",
        "header": [
          {"key": "Authorization", "value": "Bearer {{token}}"}
        ],
        "url": {
          "raw": "http://localhost:8080/api/auth/me",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "auth", "me"]
        }
      }
    }
  ],
  "variable": [
    {
      "key": "token",
      "value": "",
      "type": "string"
    }
  ]
}
```

## Verificación de Éxito

✅ Todos los servicios corriendo sin errores
✅ Migraciones Flyway ejecutadas (6 migrations)
✅ Tablas users, roles, user_roles creadas
✅ Usuario admin insertado con hash BCrypt
✅ 3 roles insertados (ADMIN, ANALISTA, AFILIADO)
✅ Registro de nuevo usuario funciona (201 Created)
✅ Login con admin funciona (200 OK + token)
✅ Login con usuario nuevo funciona (200 OK + token)
✅ GET /api/auth/me con token funciona (200 OK + user data)
✅ GET /api/auth/me sin token falla (401 Unauthorized)
✅ Token JWT contiene claims correctos (userId, email, roles)
✅ Gateway redirige /api/auth/** a Credit Service
✅ Eureka muestra solo 3 servicios (no microservice-auth)

## Próximos Pasos

Una vez verificada la autenticación:

1. **Probar endpoints de crédito**
   - Crear afiliado
   - Solicitar crédito
   - Evaluar riesgo

2. **Verificar autorización por roles**
   - ROLE_AFILIADO: puede crear solicitudes
   - ROLE_ANALISTA: puede aprobar/rechazar
   - ROLE_ADMIN: acceso completo

3. **Monitoreo y métricas**
   - Prometheus: http://localhost:9090
   - Grafana: http://localhost:3000

4. **Documentación API**
   - Swagger UI Credit: http://localhost:8082/swagger-ui.html
   - OpenAPI docs: http://localhost:8082/v3/api-docs
