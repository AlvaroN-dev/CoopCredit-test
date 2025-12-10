# 🚪 Rutas del API Gateway - Guía Rápida

## 📝 Problema Común: Error 404

Si recibes un **404** al hacer peticiones al Gateway (puerto 8080), es porque **DEBES incluir el prefijo `/api/`** en la ruta.

---

## ✅ Rutas Correctas

### 🔐 Autenticación

| Acción | Método | Ruta Correcta (Gateway) | Ruta Directa (sin Gateway) |
|--------|--------|------------------------|---------------------------|
| Registrar | POST | `http://localhost:8080/api/auth/register` | `http://localhost:8082/auth/register` |
| Login | POST | `http://localhost:8080/api/auth/login` | `http://localhost:8082/auth/login` |
| Usuario Actual | GET | `http://localhost:8080/api/auth/me` | `http://localhost:8082/auth/me` |

### 👥 Afiliados

| Acción | Método | Ruta Correcta (Gateway) | Ruta Directa |
|--------|--------|------------------------|--------------|
| Crear | POST | `http://localhost:8080/api/credit/affiliates` | `http://localhost:8082/credit/affiliates` |
| Listar Todos | GET | `http://localhost:8080/api/credit/affiliates` | `http://localhost:8082/credit/affiliates` |
| Ver por ID | GET | `http://localhost:8080/api/credit/affiliates/{id}` | `http://localhost:8082/credit/affiliates/{id}` |
| Actualizar | PUT | `http://localhost:8080/api/credit/affiliates/{id}` | `http://localhost:8082/credit/affiliates/{id}` |
| Eliminar | DELETE | `http://localhost:8080/api/credit/affiliates/{id}` | `http://localhost:8082/credit/affiliates/{id}` |

### 💳 Solicitudes de Crédito

| Acción | Método | Ruta Correcta (Gateway) | Ruta Directa |
|--------|--------|------------------------|--------------|
| Crear | POST | `http://localhost:8080/api/credit/applications` | `http://localhost:8082/credit/applications` |
| Listar Todas | GET | `http://localhost:8080/api/credit/applications` | `http://localhost:8082/credit/applications` |
| Ver por ID | GET | `http://localhost:8080/api/credit/applications/{id}` | `http://localhost:8082/credit/applications/{id}` |
| Aprobar | POST | `http://localhost:8080/api/credit/applications/{id}/approve` | `http://localhost:8082/credit/applications/{id}/approve` |
| Rechazar | POST | `http://localhost:8080/api/credit/applications/{id}/reject` | `http://localhost:8082/credit/applications/{id}/reject` |

### 📊 Swagger UI

| Recurso | Ruta |
|---------|------|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |

---

## ❌ Errores Comunes

### Error: 404 Not Found

**Causa**: Olvidaste incluir el prefijo `/api/` en la ruta del Gateway.

**Incorrecto**:
```bash
curl -X POST http://localhost:8080/auth/login  # ❌ 404 Error
```

**Correcto**:
```bash
curl -X POST http://localhost:8080/api/auth/login  # ✅ Funciona
```

---

## 🔧 Cómo Funciona el Gateway

El Gateway usa `StripPrefix=1` para remover el primer segmento de la ruta antes de enviarla al microservicio:

1. **Petición al Gateway**: `http://localhost:8080/api/auth/login`
2. **Gateway remueve** `/api` (StripPrefix=1)
3. **Envía al servicio**: `http://microservice-credit-application-service/auth/login`

---

## 📌 Ejemplos de Uso

### 1. Login via Gateway

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 2. Crear Afiliado via Gateway (con JWT)

```bash
curl -X POST http://localhost:8080/api/credit/affiliates \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_JWT_AQUI" \
  -d '{
    "firstName": "María",
    "lastName": "González",
    "email": "maria@example.com",
    "phone": "+57 300 123 4567",
    "identificationNumber": "1234567890",
    "identificationType": "CC"
  }'
```

### 3. Listar Solicitudes via Gateway (con JWT)

```bash
curl -X GET http://localhost:8080/api/credit/applications \
  -H "Authorization: Bearer TU_TOKEN_JWT_AQUI"
```

---

## 🌐 Postman / Insomnia Configuration

### Base URL
```
http://localhost:8080/api
```

### Variables de Entorno
```json
{
  "gateway_url": "http://localhost:8080/api",
  "direct_url": "http://localhost:8082",
  "jwt_token": "tu_token_aqui"
}
```

### Authorization Header
```
Authorization: Bearer {{jwt_token}}
```

---

## 📖 Más Información

Ver documentación completa en: [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
