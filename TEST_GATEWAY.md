# 🧪 TEST: Probar API Gateway

## Problema Detectado
El Gateway está funcionando (contenedor healthy), pero las peticiones desde PowerShell fallan por temas de SSL/TLS.

## Solución: Usar estas alternativas

### 1️⃣ Postman (Recomendado)

**Request de Login**:
- **Method**: POST
- **URL**: `http://localhost:8080/api/auth/login`
- **Headers**: 
  - `Content-Type: application/json`
- **Body (raw JSON)**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

### 2️⃣ Navegador Web

Abre Swagger UI directamente:
```
http://localhost:8080/swagger-ui.html
```

### 3️⃣ Git Bash o WSL (con curl real)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 4️⃣ PowerShell (bypass SSL verification)

```powershell
# Ignorar errores SSL (solo para desarrollo)
[System.Net.ServicePointManager]::ServerCertificateValidationCallback = {$true}
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

# Probar login
$body = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body

$response
```

### 5️⃣ Test con Python

```python
import requests

url = "http://localhost:8080/api/auth/login"
data = {
    "username": "admin",
    "password": "admin123"
}

response = requests.post(url, json=data)
print(f"Status: {response.status_code}")
print(f"Response: {response.json()}")
```

---

## ✅ Verificar que el Gateway está funcionando

### Check 1: Ver contenedores
```powershell
docker ps --filter "name=gateway"
```

Debe mostrar: **STATUS = Up X minutes (healthy)**

### Check 2: Ver logs
```powershell
docker logs microservice-gateway --tail 50
```

Debe mostrar: **RouteDefinition matched** para auth-endpoints, credit-service, etc.

### Check 3: Acceder a Swagger
Abre en navegador: `http://localhost:8080/swagger-ui.html`

---

## 📝 Endpoints Disponibles

### ✅ A través del Gateway (puerto 8080)
- Login: `POST http://localhost:8080/api/auth/login`
- Register: `POST http://localhost:8080/api/auth/register`
- Me: `GET http://localhost:8080/api/auth/me`
- Affiliates: `GET http://localhost:8080/api/credit/affiliates`
- Applications: `GET http://localhost:8080/api/credit/applications`

### ✅ Directo al servicio (puerto 8082)
- Login: `POST http://localhost:8082/auth/login`
- Register: `POST http://localhost:8082/auth/register`
- Me: `GET http://localhost:8082/auth/me`
- Affiliates: `GET http://localhost:8082/credit/affiliates`
- Applications: `GET http://localhost:8082/credit/applications`

---

## 🔍 Troubleshooting

### Error 404 Not Found
**Causa**: Falta el prefijo `/api/` en la ruta del Gateway

**Solución**: 
- ❌ `http://localhost:8080/auth/login`
- ✅ `http://localhost:8080/api/auth/login`

### Error 401 Unauthorized
**Causa**: Falta el token JWT en endpoints protegidos

**Solución**: Agregar header `Authorization: Bearer TU_TOKEN`

### Error 500 Internal Server Error
**Causa**: Problema en el microservicio backend

**Solución**: Ver logs con `docker logs microservice-credit-application-service`

### Gateway no responde
**Causa**: Contenedor no está corriendo o no está healthy

**Solución**: 
```powershell
docker-compose restart microservice-gateway
docker logs microservice-gateway
```
