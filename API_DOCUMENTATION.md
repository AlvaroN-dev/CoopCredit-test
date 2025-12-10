# 📚 Documentación de API - CoopCredit

## 🔗 Acceso a la Documentación Interactiva

**Swagger UI (Recomendado)**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Esta documentación proporciona ejemplos detallados de cada endpoint, sus campos requeridos y opcionales, y cómo utilizarlos.

---

## 🌐 URL Base

```
http://localhost:8080
```

Todos los endpoints se acceden a través del API Gateway en el puerto 8080.

---

## 🔐 Autenticación

### 1. Registrar Usuario

**Endpoint**: `POST /api/auth/register`

**Descripción**: Crea un nuevo usuario en el sistema. Los roles se asignan dinámicamente y se crean automáticamente si no existen. **NO retorna token JWT**, solo los datos del usuario creado.

**Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "username": "string (requerido, 3-50 caracteres)",
  "email": "string (requerido, formato email válido)",
  "password": "string (requerido, mínimo 8 caracteres)",
  "firstName": "string (requerido, nombre)",
  "lastName": "string (requerido, apellido)",
  "role": "string (requerido, valores: ADMIN, ANALISTA, AFILIADO)"
}
```

**Ejemplo de Request**:
```json
{
  "username": "jperez",
  "email": "jperez@coopcredit.com",
  "password": "Password123!",
  "firstName": "Juan",
  "lastName": "Pérez",
  "role": "AFILIADO"
}
```

**Response Exitoso (201 Created)**:
```json
{
  "id": 1,
  "username": "jperez",
  "email": "jperez@coopcredit.com",
  "firstName": "Juan",
  "lastName": "Pérez",
  "roles": ["AFILIADO"],
  "enabled": true
}
```

**Nota Importante**: Los roles se devuelven SIN el prefijo "ROLE_". Si registras con rol "ADMIN", recibirás "ADMIN" (no "ROLE_ADMIN").

**Errores Comunes**:
- `400 Bad Request`: Username ya existe, email inválido, o password muy corto
- `500 Internal Server Error`: Error al crear el rol o usuario

**Curl Example**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jperez",
    "email": "jperez@coopcredit.com",
    "password": "Password123!",
    "firstName": "Juan",
    "lastName": "Pérez",
    "role": "AFILIADO"
  }'
```

---

### 2. Iniciar Sesión (Login)

**Endpoint**: `POST /api/auth/login`

**Descripción**: Autentica un usuario y devuelve un token JWT válido por 24 horas.

**Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "username": "string (requerido)",
  "password": "string (requerido)"
}
```

**Ejemplo de Request**:
```json
{
  "username": "jperez",
  "password": "Password123!"
}
```

**Response Exitoso (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqcGVyZXoiLCJyb2xlcyI6WyJBRklMSUFETyJdLCJpYXQiOjE3MDI0MDAwMDAsImV4cCI6MTcwMjQ4NjQwMH0...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "username": "jperez",
  "roles": ["AFILIADO"]
}
```

**Nota Importante**: Los roles se devuelven SIN el prefijo "ROLE_". Si el usuario tiene rol "ROLE_ADMIN" en base de datos, recibirás "ADMIN" en el response.

**Errores Comunes**:
- `401 Unauthorized`: Credenciales incorrectas
- `404 Not Found`: Usuario no existe

**Curl Example**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jperez",
    "password": "Password123!"
  }'
```

**Nota Importante**: Guarda el token recibido, lo necesitarás para todas las demás peticiones.

---

### 3. Obtener Usuario Actual

**Endpoint**: `GET /api/auth/me`

**Descripción**: Obtiene la información del usuario autenticado actualmente.

**Headers**:
```
Authorization: Bearer {tu-token-jwt-aqui}
```

**Nota**: El token debe ir en el header `Authorization` con el prefijo `Bearer ` (incluye el espacio después de Bearer)

**Request Body**: No requiere

**Response Exitoso (200 OK)**:
```json
{
  "id": 1,
  "username": "jperez",
  "email": "jperez@coopcredit.com",
  "firstName": "Juan",
  "lastName": "Pérez",
  "roles": ["AFILIADO"],
  "enabled": true
}
```

**Nota Importante**: Los roles se devuelven SIN el prefijo "ROLE_".

**Errores Comunes**:
- `401 Unauthorized`: Token inválido o expirado
- `403 Forbidden`: Token no proporcionado

**Curl Example**:
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

**En Postman/Thunder Client**:
- **Auth Type**: Bearer Token
- **Token**: `eyJhbGciOiJIUzUxMiJ9...` (sin el prefijo "Bearer")

**En Headers Manualmente**:
- **Key**: `Authorization`
- **Value**: `Bearer eyJhbGciOiJIUzUxMiJ9...` (con el prefijo "Bearer " y un espacio)

---

## 👥 Gestión de Afiliados

### 4. Crear Afiliado

**Endpoint**: `POST /api/credit/affiliates`

**Descripción**: Crea un nuevo afiliado en el sistema cooperativo.

**Roles Permitidos**: `ROLE_ADMIN`, `ROLE_ANALISTA`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Request Body**:
```json
{
  "firstName": "string (requerido, máx 100 caracteres)",
  "lastName": "string (requerido, máx 100 caracteres)",
  "documentNumber": "string (requerido, único, máx 20 caracteres)",
  "email": "string (requerido, formato email válido)",
  "phone": "string (requerido, máx 20 caracteres)",
  "address": "string (opcional, máx 255 caracteres)",
  "monthlyIncome": "decimal (requerido, valor positivo)",
  "currentDebt": "decimal (opcional, valor positivo, default: 0)"
}
```

**Ejemplo de Request**:
```json
{
  "firstName": "Juan",
  "lastName": "Pérez García",
  "documentNumber": "1234567890",
  "email": "juan.perez@email.com",
  "phone": "+57 300 123 4567",
  "address": "Calle 123 #45-67, Bogotá",
  "monthlyIncome": 3500000.00,
  "currentDebt": 500000.00
}
```

**Response Exitoso (201 Created)**:
```json
{
  "id": 1,
  "firstName": "Juan",
  "lastName": "Pérez García",
  "documentNumber": "1234567890",
  "email": "juan.perez@email.com",
  "phone": "+57 300 123 4567",
  "address": "Calle 123 #45-67, Bogotá",
  "monthlyIncome": 3500000.00,
  "currentDebt": 500000.00,
  "registrationDate": "2024-12-10T15:30:00",
  "active": true
}
```

**Errores Comunes**:
- `400 Bad Request`: Datos inválidos o documento ya existe
- `401 Unauthorized`: Token inválido
- `403 Forbidden`: Usuario sin permisos (debe ser ADMIN o ANALISTA)

**Curl Example**:
```bash
curl -X POST http://localhost:8080/api/credit/affiliates \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "firstName": "Juan",
    "lastName": "Pérez García",
    "documentNumber": "1234567890",
    "email": "juan.perez@email.com",
    "phone": "+57 300 123 4567",
    "address": "Calle 123 #45-67, Bogotá",
    "monthlyIncome": 3500000.00,
    "currentDebt": 500000.00
  }'
```

---

### 5. Obtener Afiliado por ID

**Endpoint**: `GET /api/credit/affiliates/{id}`

**Descripción**: Obtiene los detalles de un afiliado específico.

**Roles Permitidos**: `ROLE_ADMIN`, `ROLE_ANALISTA`

**Headers**:
```
Authorization: Bearer {token}
```

**Path Parameters**:
- `id` (Long, requerido): ID del afiliado

**Response Exitoso (200 OK)**:
```json
{
  "id": 1,
  "firstName": "Juan",
  "lastName": "Pérez García",
  "documentNumber": "1234567890",
  "email": "juan.perez@email.com",
  "phone": "+57 300 123 4567",
  "address": "Calle 123 #45-67, Bogotá",
  "monthlyIncome": 3500000.00,
  "currentDebt": 500000.00,
  "registrationDate": "2024-12-10T15:30:00",
  "active": true
}
```

**Errores Comunes**:
- `404 Not Found`: Afiliado no existe
- `401 Unauthorized`: Token inválido
- `403 Forbidden`: Usuario sin permisos

**Curl Example**:
```bash
curl -X GET http://localhost:8080/api/credit/affiliates/1 \
  -H "Authorization: Bearer {token}"
```

---

### 6. Listar Todos los Afiliados

**Endpoint**: `GET /api/credit/affiliates`

**Descripción**: Obtiene una lista paginada de todos los afiliados.

**Roles Permitidos**: `ROLE_ADMIN`, `ROLE_ANALISTA`

**Headers**:
```
Authorization: Bearer {token}
```

**Query Parameters** (todos opcionales):
- `page` (int, default: 0): Número de página
- `size` (int, default: 20): Tamaño de página
- `active` (boolean, opcional): Filtrar por estado activo

**Ejemplo de URL**:
```
GET /api/credit/affiliates?page=0&size=10&active=true
```

**Response Exitoso (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "Juan",
      "lastName": "Pérez García",
      "documentNumber": "1234567890",
      "email": "juan.perez@email.com",
      "phone": "+57 300 123 4567",
      "active": true
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

**Curl Example**:
```bash
curl -X GET "http://localhost:8080/api/credit/affiliates?page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

---

### 7. Actualizar Afiliado

**Endpoint**: `PUT /api/credit/affiliates/{id}`

**Descripción**: Actualiza los datos de un afiliado existente.

**Roles Permitidos**: `ROLE_ADMIN`, `ROLE_ANALISTA`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Path Parameters**:
- `id` (Long, requerido): ID del afiliado a actualizar

**Request Body**: Mismos campos que en crear afiliado
```json
{
  "firstName": "Juan Carlos",
  "lastName": "Pérez García",
  "documentNumber": "1234567890",
  "email": "juanc.perez@email.com",
  "phone": "+57 300 123 4567",
  "address": "Calle 123 #45-67, Bogotá",
  "monthlyIncome": 4000000.00,
  "currentDebt": 300000.00
}
```

**Response Exitoso (200 OK)**: Retorna el afiliado actualizado

**Errores Comunes**:
- `404 Not Found`: Afiliado no existe
- `400 Bad Request`: Datos inválidos
- `403 Forbidden`: Usuario sin permisos

**Curl Example**:
```bash
curl -X PUT http://localhost:8080/api/credit/affiliates/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "firstName": "Juan Carlos",
    "monthlyIncome": 4000000.00
  }'
```

---

### 8. Eliminar Afiliado

**Endpoint**: `DELETE /api/credit/affiliates/{id}`

**Descripción**: Elimina (desactiva) un afiliado del sistema.

**Roles Permitidos**: `ROLE_ADMIN`

**Headers**:
```
Authorization: Bearer {token}
```

**Path Parameters**:
- `id` (Long, requerido): ID del afiliado a eliminar

**Response Exitoso (204 No Content)**: Sin contenido en el body

**Errores Comunes**:
- `404 Not Found`: Afiliado no existe
- `403 Forbidden`: Solo ADMIN puede eliminar

**Curl Example**:
```bash
curl -X DELETE http://localhost:8080/api/credit/affiliates/1 \
  -H "Authorization: Bearer {token}"
```

---

## 💳 Solicitudes de Crédito

### 9. Crear Solicitud de Crédito

**Endpoint**: `POST /api/credit/applications`

**Descripción**: Crea una nueva solicitud de crédito. Automáticamente ejecuta una evaluación de riesgo.

**Roles Permitidos**: `ROLE_AFILIADO`, `ROLE_ANALISTA`, `ROLE_ADMIN`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Request Body**:
```json
{
  "affiliateId": "long (requerido)",
  "amount": "decimal (requerido, valor positivo, ejemplo: 10000000.00)",
  "term": "integer (requerido, meses, rango: 6-120)",
  "purpose": "string (requerido, máx 500 caracteres)",
  "comments": "string (opcional, máx 1000 caracteres)"
}
```

**Ejemplo de Request**:
```json
{
  "affiliateId": 1,
  "amount": 15000000.00,
  "term": 36,
  "purpose": "Compra de vehículo para trabajo",
  "comments": "Necesito el vehículo para transporte de mercancía"
}
```

**Response Exitoso (201 Created)**:
```json
{
  "id": 1,
  "affiliateId": 1,
  "affiliateName": "Juan Pérez García",
  "amount": 15000000.00,
  "term": 36,
  "purpose": "Compra de vehículo para trabajo",
  "status": "PENDIENTE",
  "applicationDate": "2024-12-10T16:45:00",
  "riskEvaluation": {
    "id": 1,
    "score": 720,
    "riskLevel": "MEDIO",
    "debtCapacity": 0.35,
    "maxRecommendedAmount": 12000000.00,
    "evaluationDate": "2024-12-10T16:45:00"
  },
  "comments": "Necesito el vehículo para transporte de mercancía"
}
```

**Estados Posibles**:
- `PENDIENTE`: Recién creada, esperando revisión
- `EN_REVISION`: En proceso de análisis
- `APROBADA`: Crédito aprobado
- `RECHAZADA`: Crédito rechazado
- `CANCELADA`: Cancelada por el solicitante

**Niveles de Riesgo**:
- `BAJO`: Score >= 750
- `MEDIO`: Score 600-749
- `ALTO`: Score < 600

**Errores Comunes**:
- `400 Bad Request`: Monto inválido, plazo fuera de rango, afiliado no existe
- `401 Unauthorized`: Token inválido
- `403 Forbidden`: AFILIADO solo puede crear solicitudes para sí mismo

**Curl Example**:
```bash
curl -X POST http://localhost:8080/api/credit/applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "affiliateId": 1,
    "amount": 15000000.00,
    "term": 36,
    "purpose": "Compra de vehículo para trabajo"
  }'
```

---

### 10. Obtener Solicitud por ID

**Endpoint**: `GET /api/credit/applications/{id}`

**Descripción**: Obtiene los detalles completos de una solicitud de crédito.

**Roles Permitidos**: 
- `ROLE_AFILIADO`: Solo sus propias solicitudes
- `ROLE_ANALISTA`, `ROLE_ADMIN`: Todas las solicitudes

**Headers**:
```
Authorization: Bearer {token}
```

**Path Parameters**:
- `id` (Long, requerido): ID de la solicitud

**Response Exitoso (200 OK)**:
```json
{
  "id": 1,
  "affiliateId": 1,
  "affiliateName": "Juan Pérez García",
  "amount": 15000000.00,
  "term": 36,
  "purpose": "Compra de vehículo para trabajo",
  "status": "APROBADA",
  "applicationDate": "2024-12-10T16:45:00",
  "decisionDate": "2024-12-11T10:30:00",
  "decidedBy": "analista1",
  "riskEvaluation": {
    "id": 1,
    "score": 720,
    "riskLevel": "MEDIO",
    "debtCapacity": 0.35,
    "maxRecommendedAmount": 12000000.00,
    "evaluationDate": "2024-12-10T16:45:00"
  },
  "comments": "Aprobado por buen historial crediticio"
}
```

**Errores Comunes**:
- `404 Not Found`: Solicitud no existe
- `403 Forbidden`: AFILIADO intentando ver solicitud de otro usuario

**Curl Example**:
```bash
curl -X GET http://localhost:8080/api/credit/applications/1 \
  -H "Authorization: Bearer {token}"
```

---

### 11. Listar Solicitudes

**Endpoint**: `GET /api/credit/applications`

**Descripción**: Obtiene una lista paginada de solicitudes de crédito.

**Roles Permitidos**: `ROLE_ANALISTA`, `ROLE_ADMIN`

**Headers**:
```
Authorization: Bearer {token}
```

**Query Parameters** (todos opcionales):
- `page` (int, default: 0): Número de página
- `size` (int, default: 20): Tamaño de página
- `status` (string, opcional): Filtrar por estado (PENDIENTE, EN_REVISION, APROBADA, RECHAZADA, CANCELADA)
- `affiliateId` (long, opcional): Filtrar por afiliado

**Ejemplo de URL**:
```
GET /api/credit/applications?page=0&size=10&status=PENDIENTE
```

**Response Exitoso (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "affiliateName": "Juan Pérez García",
      "amount": 15000000.00,
      "term": 36,
      "status": "PENDIENTE",
      "applicationDate": "2024-12-10T16:45:00",
      "riskLevel": "MEDIO"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

**Curl Example**:
```bash
curl -X GET "http://localhost:8080/api/credit/applications?status=PENDIENTE" \
  -H "Authorization: Bearer {token}"
```

---

### 12. Listar Solicitudes por Afiliado

**Endpoint**: `GET /api/credit/applications/affiliate/{affiliateId}`

**Descripción**: Obtiene todas las solicitudes de un afiliado específico.

**Roles Permitidos**: 
- `ROLE_AFILIADO`: Solo sus propias solicitudes (affiliateId debe coincidir)
- `ROLE_ANALISTA`, `ROLE_ADMIN`: Cualquier afiliado

**Headers**:
```
Authorization: Bearer {token}
```

**Path Parameters**:
- `affiliateId` (Long, requerido): ID del afiliado

**Query Parameters**:
- `page` (int, default: 0)
- `size` (int, default: 20)

**Response Exitoso (200 OK)**: Similar a listar solicitudes

**Curl Example**:
```bash
curl -X GET http://localhost:8080/api/credit/applications/affiliate/1 \
  -H "Authorization: Bearer {token}"
```

---

### 13. Aprobar/Rechazar Solicitud

**Endpoint**: `PUT /api/credit/applications/{id}/decision`

**Descripción**: Registra una decisión (aprobación o rechazo) sobre una solicitud de crédito.

**Roles Permitidos**: `ROLE_ANALISTA`, `ROLE_ADMIN`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Path Parameters**:
- `id` (Long, requerido): ID de la solicitud

**Request Body**:
```json
{
  "decision": "string (requerido, valores: APPROVED, REJECTED)",
  "comments": "string (requerido, máx 1000 caracteres)"
}
```

**Ejemplo de Request - Aprobación**:
```json
{
  "decision": "APPROVED",
  "comments": "Solicitud aprobada. El afiliado tiene buen historial crediticio y capacidad de pago demostrada."
}
```

**Ejemplo de Request - Rechazo**:
```json
{
  "decision": "REJECTED",
  "comments": "Solicitud rechazada. El monto solicitado excede la capacidad de endeudamiento recomendada según la evaluación de riesgo."
}
```

**Response Exitoso (200 OK)**:
```json
{
  "id": 1,
  "affiliateName": "Juan Pérez García",
  "amount": 15000000.00,
  "status": "APROBADA",
  "decisionDate": "2024-12-11T10:30:00",
  "decidedBy": "analista1",
  "comments": "Solicitud aprobada. El afiliado tiene buen historial crediticio..."
}
```

**Errores Comunes**:
- `404 Not Found`: Solicitud no existe
- `400 Bad Request`: Decision inválida o solicitud ya tiene decisión
- `403 Forbidden`: Usuario sin permisos (debe ser ANALISTA o ADMIN)

**Curl Example - Aprobar**:
```bash
curl -X PUT http://localhost:8080/api/credit/applications/1/decision \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "decision": "APPROVED",
    "comments": "Solicitud aprobada por capacidad de pago demostrada"
  }'
```

**Curl Example - Rechazar**:
```bash
curl -X PUT http://localhost:8080/api/credit/applications/1/decision \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "decision": "REJECTED",
    "comments": "Monto excede capacidad de endeudamiento"
  }'
```

---

## ⚠️ Evaluación de Riesgo

### 14. Evaluar Riesgo (Endpoint Interno)

**Endpoint**: `POST /api/risk/evaluation`

**Descripción**: Evalúa el riesgo crediticio basado en perfil financiero. Este endpoint es llamado automáticamente por Credit Service al crear una solicitud.

**Uso**: Principalmente interno, pero puede ser usado directamente por ADMIN para pruebas.

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Request Body**:
```json
{
  "amount": "decimal (requerido, monto solicitado)",
  "term": "integer (requerido, plazo en meses)",
  "monthlyIncome": "decimal (requerido, ingreso mensual)",
  "currentDebt": "decimal (requerido, deuda actual)"
}
```

**Ejemplo de Request**:
```json
{
  "amount": 15000000.00,
  "term": 36,
  "monthlyIncome": 3500000.00,
  "currentDebt": 500000.00
}
```

**Response Exitoso (200 OK)**:
```json
{
  "score": 720,
  "riskLevel": "MEDIO",
  "debtCapacity": 0.35,
  "maxRecommendedAmount": 12000000.00,
  "evaluationDate": "2024-12-10T16:45:00",
  "factors": {
    "incomeToDebtRatio": 0.14,
    "requestedAmount": 15000000.00,
    "affordabilityIndex": 0.42
  }
}
```

**Cálculo de Score**:
- **Base**: 500 puntos
- **Ingreso alto**: +100 puntos por cada millón de ingreso (max 300)
- **Deuda baja**: +150 puntos si deuda < 20% ingreso
- **Plazo favorable**: +50 puntos si plazo <= 24 meses
- **Monto razonable**: +100 puntos si monto < 10x ingreso mensual

**Curl Example**:
```bash
curl -X POST http://localhost:8080/api/risk/evaluation \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "amount": 15000000.00,
    "term": 36,
    "monthlyIncome": 3500000.00,
    "currentDebt": 500000.00
  }'
```

---

## 🔒 Seguridad y Tokens

### Token JWT

**Ubicación del Token**:
El token JWT **SIEMPRE** debe enviarse en el header `Authorization` con el formato:
```
Authorization: Bearer {tu-token-jwt}
```

**⚠️ IMPORTANTE**:
- ❌ **NO** enviar el token en el body del request
- ❌ **NO** enviar el token como query parameter
- ✅ **SÍ** enviar en header `Authorization` con prefijo `Bearer `
- ✅ Debe haber un espacio entre "Bearer" y el token

**Ejemplo de Header Correcto**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqcGVyZXoiLCJyb2xlcyI6WyJBRklMSUFETyJdLCJpYXQiOjE3MDI0MDAwMDAsImV4cCI6MTcwMjQ4NjQwMH0...
```

**En Herramientas de Testing**:
- **Postman**: Authorization tab → Type: Bearer Token → Token: {tu-token}
- **Thunder Client**: Auth → Bearer → Token: {tu-token}
- **Insomnia**: Auth → Bearer Token → Token: {tu-token}

**Características del Token**:
- **Algoritmo**: HS512 (HMAC con SHA-512)
- **Validez**: 24 horas
- **Claims**: username, roles (sin prefijo "ROLE_"), iat (issued at), exp (expiration)

**Renovación**: Cuando el token expire (después de 24 horas), debes hacer login nuevamente para obtener un nuevo token.

---

## 📊 Códigos de Respuesta HTTP

| Código | Significado | Cuándo ocurre |
|--------|-------------|---------------|
| `200 OK` | Éxito | GET, PUT exitosos |
| `201 Created` | Recurso creado | POST exitosos |
| `204 No Content` | Éxito sin contenido | DELETE exitosos |
| `400 Bad Request` | Datos inválidos | Validación falló, campos requeridos faltantes |
| `401 Unauthorized` | No autenticado | Token inválido, expirado o no proporcionado |
| `403 Forbidden` | Sin permisos | Usuario no tiene el rol requerido |
| `404 Not Found` | Recurso no existe | ID no encontrado en base de datos |
| `500 Internal Server Error` | Error del servidor | Error inesperado (bug, BD caída, etc.) |

---

## 🎯 Flujo de Trabajo Completo

### Caso de Uso: Solicitud de Crédito por Afiliado

#### Paso 0: Registrar usuario (si no existe)
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jperez",
    "email": "jperez@coopcredit.com",
    "password": "Password123!",
    "firstName": "Juan",
    "lastName": "Pérez",
    "role": "AFILIADO"
  }'
```
**Response:**
```json
{
  "id": 1,
  "username": "jperez",
  "roles": ["AFILIADO"]
}
```

#### Paso 1: Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "jperez", "password": "Password123!"}'
```
**Response:**
```json
{
  "token": "eyJhbGc...",
  "username": "jperez",
  "roles": ["AFILIADO"]
}
```
**Guardar el token recibido**

#### Paso 2: Verificar perfil
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer {TOKEN}"
```
**Response:**
```json
{
  "id": 1,
  "username": "jperez",
  "roles": ["AFILIADO"]
}
```

#### Paso 3: Crear solicitud de crédito
```bash
curl -X POST http://localhost:8080/api/credit/applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TOKEN}" \
  -d '{
    "affiliateId": 1,
    "amount": 10000000.00,
    "term": 24,
    "purpose": "Compra de motocicleta para transporte"
  }'
```

#### Paso 4: Consultar estado de la solicitud
```bash
curl -X GET http://localhost:8080/api/credit/applications/1 \
  -H "Authorization: Bearer {TOKEN}"
```

---

## 📱 Cómo Enviar el Token en Diferentes Herramientas

### 🔵 Postman
1. Abre tu request
2. Ve a la pestaña **"Authorization"**
3. En **"Type"** selecciona: **Bearer Token**
4. En el campo **"Token"** pega tu token (solo el token, sin "Bearer")
5. Postman automáticamente agregará: `Authorization: Bearer {tu-token}`

### ⚡ Thunder Client (VS Code Extension)
1. En tu request, ve a **"Auth"**
2. Selecciona **"Bearer"**
3. Pega tu token en el campo **"Token"**
4. Se agrega automáticamente al header

### 🟣 Insomnia
1. En tu request, ve a **"Auth"**
2. Selecciona **"Bearer Token"**
3. Pega tu token en el campo **"Token"**
4. Se configura automáticamente

### 💻 cURL (Terminal/Command Line)
```bash
# Formato correcto
curl -X GET http://localhost:8080/api/credit/applications/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

# Nota: Incluye "Bearer " con un espacio después
```

### 🟢 JavaScript (Fetch API)
```javascript
const token = "eyJhbGciOiJIUzUxMiJ9..."; // Tu token

fetch('http://localhost:8080/api/credit/applications/1', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(response => response.json())
.then(data => console.log(data));
```

### 🔴 Axios (JavaScript)
```javascript
const token = "eyJhbGciOiJIUzUxMiJ9..."; // Tu token

axios.get('http://localhost:8080/api/credit/applications/1', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
.then(response => console.log(response.data));
```

### ☕ Java (Spring RestTemplate)
```java
String token = "eyJhbGciOiJIUzUxMiJ9..."; // Tu token

HttpHeaders headers = new HttpHeaders();
headers.set("Authorization", "Bearer " + token);
headers.setContentType(MediaType.APPLICATION_JSON);

HttpEntity<String> entity = new HttpEntity<>(headers);

ResponseEntity<String> response = restTemplate.exchange(
    "http://localhost:8080/api/credit/applications/1",
    HttpMethod.GET,
    entity,
    String.class
);
```

### 🐍 Python (Requests)
```python
token = "eyJhbGciOiJIUzUxMiJ9..."  # Tu token

headers = {
    "Authorization": f"Bearer {token}",
    "Content-Type": "application/json"
}

response = requests.get(
    "http://localhost:8080/api/credit/applications/1",
    headers=headers
)

print(response.json())
```

### 🔷 C# (.NET)
```csharp
string token = "eyJhbGciOiJIUzUxMiJ9..."; // Tu token

using (var client = new HttpClient())
{
    client.DefaultRequestHeaders.Authorization = 
        new AuthenticationHeaderValue("Bearer", token);
    
    var response = await client.GetAsync(
        "http://localhost:8080/api/credit/applications/1"
    );
    
    var content = await response.Content.ReadAsStringAsync();
}
```

---

### ⚠️ Errores Comunes con el Token

#### Error 1: "No Authorization header"
**Causa**: No enviaste el header Authorization
**Solución**: Verifica que el header esté presente en el request

#### Error 2: "Invalid token format"
**Causa**: Olvidaste el prefijo "Bearer " o lo escribiste mal
**Solución**: Debe ser `Bearer {token}` con un espacio después de Bearer

#### Error 3: "Token expired"
**Causa**: El token tiene más de 24 horas
**Solución**: Haz login nuevamente para obtener un nuevo token

#### Error 4: "Unauthorized 401"
**Causa**: Token inválido o mal formado
**Solución**: Verifica que copiaste el token completo sin espacios extra

---
  -H "Authorization: Bearer {TOKEN}"
```

---

### Caso de Uso: Aprobación por Analista

#### Paso 1: Login como analista
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "analista1", "password": "Analyst123!"}'
```

#### Paso 2: Listar solicitudes pendientes
```bash
curl -X GET "http://localhost:8080/api/credit/applications?status=PENDIENTE" \
  -H "Authorization: Bearer {TOKEN}"
```

#### Paso 3: Ver detalles de solicitud específica
```bash
curl -X GET http://localhost:8080/api/credit/applications/1 \
  -H "Authorization: Bearer {TOKEN}"
```

#### Paso 4: Aprobar solicitud
```bash
curl -X PUT http://localhost:8080/api/credit/applications/1/decision \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TOKEN}" \
  -d '{
    "decision": "APPROVED",
    "comments": "Aprobado por buen perfil crediticio"
  }'
```

---

## 🐛 Solución de Problemas Comunes

### Error: "Token expired"
**Causa**: El token JWT tiene 24 horas de validez
**Solución**: Hacer login nuevamente para obtener un nuevo token

### Error: "Access Denied" o 403
**Causa**: El usuario no tiene el rol requerido para ese endpoint
**Solución**: Verificar que el usuario tenga el rol correcto (AFILIADO/ANALISTA/ADMIN)

### Error: "Affiliate not found"
**Causa**: El ID de afiliado no existe en la base de datos
**Solución**: Crear el afiliado primero con POST /api/credit/affiliates

### Error: "Amount exceeds recommended limit"
**Causa**: El monto solicitado es muy alto según la evaluación de riesgo
**Solución**: Reducir el monto o aumentar el ingreso mensual del afiliado

---

## 📞 Información de Contacto

Para más información, consulta:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **README principal**: [README.md](README.md)
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

---

## 📝 Notas Adicionales

### Formatos de Fecha
- Todas las fechas se devuelven en formato ISO-8601: `2024-12-10T16:45:00`
- Zona horaria: UTC

### Paginación
- Por defecto: page=0, size=20
- Máximo permitido: size=100

### Valores Monetarios
- Formato: Decimal con 2 decimales (ejemplo: 10000000.00)
- Moneda: COP (Pesos Colombianos)

### Validaciones de Negocio
- **Monto mínimo**: $1,000,000 COP
- **Monto máximo**: $100,000,000 COP
- **Plazo mínimo**: 6 meses
- **Plazo máximo**: 120 meses (10 años)
- **Capacidad de endeudamiento recomendada**: Máximo 40% del ingreso mensual
