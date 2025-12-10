# 📊 Guía Completa: Configurar Grafana con Prometheus

## 🚀 Paso 1: Acceder a Grafana

1. Abre tu navegador web
2. Navega a: **http://localhost:3000**
3. Credenciales por defecto:
   - **Usuario**: `admin`
   - **Password**: `admin`
4. Te pedirá cambiar la contraseña (puedes omitirlo haciendo clic en "Skip")

---

## 📡 Paso 2: Agregar Prometheus como Data Source

### 2.1 Acceder a Configuración
1. En el menú lateral izquierdo, haz clic en el ícono de **engranaje** (⚙️ Configuration)
2. Selecciona **Data sources**
3. Haz clic en el botón **Add data source**

### 2.2 Seleccionar Prometheus
1. En la lista de data sources, busca y selecciona **Prometheus**

### 2.3 Configurar Conexión
Completa los siguientes campos:

```
Name: Prometheus
Default: ☑️ (marca el checkbox)

HTTP:
  URL: http://prometheus:9090
  Access: Server (default)

Auth:
  (dejar todo desmarcado)

Timeout:
  HTTP request timeout: 60 (default)
```

### 2.4 Guardar y Probar
1. Haz scroll hasta abajo
2. Haz clic en **Save & test**
3. Deberías ver un mensaje verde: ✅ **"Data source is working"**

---

## 📈 Paso 3: Crear tu Primer Dashboard

### Opción A: Importar Dashboard Pre-configurado (RECOMENDADO)

#### 3.1 Dashboard de Spring Boot (ID: 12900)
1. En el menú lateral, haz clic en **"+"** (Create) → **Import**
2. En "Import via grafana.com", ingresa: **`12900`**
3. Haz clic en **Load**
4. Configuración:
   - **Name**: Spring Boot APM Dashboard
   - **Folder**: (dejar en General o crear uno nuevo)
   - **Prometheus**: Selecciona tu data source "Prometheus"
5. Haz clic en **Import**

#### 3.2 Dashboard de JVM (ID: 4701)
1. Repite el proceso con el ID: **`4701`**
2. Configuración:
   - **Name**: JVM (Micrometer)
   - **Prometheus**: Selecciona "Prometheus"
3. Haz clic en **Import**

#### 3.3 Dashboard de Spring Boot Statistics (ID: 10280)
1. Repite el proceso con el ID: **`10280`**
2. Configuración:
   - **Name**: Spring Boot 2.1 Statistics
   - **Prometheus**: Selecciona "Prometheus"
3. Haz clic en **Import**

### Opción B: Crear Dashboard Personalizado

#### 3.1 Crear Nuevo Dashboard
1. En el menú lateral, haz clic en **"+"** (Create) → **Dashboard**
2. Haz clic en **Add new panel**

#### 3.2 Panel: Tasa de Requests HTTP
```
Query:
rate(http_server_requests_seconds_count{application="microservice-credit-application-service"}[1m])

Legend:
{{method}} {{uri}} - {{status}}

Panel Title: HTTP Request Rate (req/s)
```

#### 3.3 Panel: Tiempo Promedio de Respuesta
```
Query:
rate(http_server_requests_seconds_sum{application="microservice-credit-application-service"}[1m]) 
/ 
rate(http_server_requests_seconds_count{application="microservice-credit-application-service"}[1m])

Legend:
{{method}} {{uri}}

Panel Title: Average Response Time (seconds)
```

#### 3.4 Panel: Tasa de Errores HTTP
```
Query:
sum by (status) (rate(http_server_requests_seconds_count{application="microservice-credit-application-service", status=~"5.."}[1m]))

Legend:
Status {{status}}

Panel Title: HTTP 5xx Error Rate
```

#### 3.5 Panel: Login Exitosos vs Fallidos
```
Query 1 (Success):
rate(auth_login_success_total{application="microservice-credit-application-service"}[5m])

Query 2 (Failures):
rate(auth_login_failure_total{application="microservice-credit-application-service"}[5m])

Legend:
Success / Failures

Panel Title: Login Success vs Failures
```

#### 3.6 Panel: Uso de Memoria JVM
```
Query:
jvm_memory_used_bytes{application="microservice-credit-application-service", area="heap"}

Legend:
{{id}}

Panel Title: JVM Heap Memory Usage
```

#### 3.7 Panel: Threads JVM
```
Query:
jvm_threads_live_threads{application="microservice-credit-application-service"}

Panel Title: JVM Live Threads
```

#### 3.8 Panel: Conexiones de Base de Datos
```
Query 1 (Active):
hikaricp_connections_active{application="microservice-credit-application-service"}

Query 2 (Idle):
hikaricp_connections_idle{application="microservice-credit-application-service"}

Query 3 (Max):
hikaricp_connections_max{application="microservice-credit-application-service"}

Legend:
Active / Idle / Max

Panel Title: Database Connection Pool
```

#### 3.9 Guardar Dashboard
1. Haz clic en el ícono de **disco** (💾) en la parte superior
2. Nombre: **CoopCredit - Credit Service**
3. Haz clic en **Save**

---

## 🔔 Paso 4: Configurar Alertas (Opcional)

### 4.1 Crear Alerta de Tasa de Errores Alta

1. Crea o edita un panel
2. En la pestaña **Alert**, haz clic en **Create alert rule from this panel**
3. Configuración:
   ```
   Name: High Error Rate
   
   Condition:
   WHEN last() OF query(A, 1m, now) IS ABOVE 0.05
   
   Evaluate every: 1m
   For: 5m
   ```
4. En **Notifications**, selecciona o crea un contact point
5. Haz clic en **Save**

### 4.2 Crear Alerta de Tiempo de Respuesta Alto

```
Name: High Response Time

Condition:
WHEN avg() OF query(A, 5m, now) IS ABOVE 2

Evaluate every: 1m
For: 5m
```

### 4.3 Crear Alerta de Memoria Alta

```
Name: High Memory Usage

Condition:
WHEN last() OF query(A, 1m, now) IS ABOVE 90000000

Evaluate every: 1m
For: 5m
```

---

## 📊 Paso 5: Queries Útiles de Prometheus

### Verificar que Prometheus está Scraping

1. Abre: **http://localhost:9090**
2. Ve a **Status** → **Targets**
3. Deberías ver:
   - `microservice-credit-application-service` - **UP** ✅

### Queries para Probar en Prometheus

#### Ver todas las métricas del servicio:
```promql
{application="microservice-credit-application-service"}
```

#### Requests HTTP por segundo:
```promql
rate(http_server_requests_seconds_count[1m])
```

#### Percentil 95 de tiempos de respuesta:
```promql
histogram_quantile(0.95, 
  sum by (le, uri) (
    rate(http_server_requests_seconds_bucket{application="microservice-credit-application-service"}[5m])
  )
)
```

#### Tasa de login fallidos:
```promql
rate(auth_login_failure_total[5m])
```

#### Uso de CPU:
```promql
system_cpu_usage{application="microservice-credit-application-service"}
```

#### Garbage Collection:
```promql
rate(jvm_gc_pause_seconds_count[5m])
```

---

## 🎨 Paso 6: Personalizar Dashboard

### Variables de Dashboard

1. En el dashboard, haz clic en **Settings** (⚙️)
2. Ve a **Variables**
3. Haz clic en **Add variable**

#### Variable: Application
```
Name: application
Type: Query
Data source: Prometheus
Query: label_values(application)
Multi-value: ☑️
Include All option: ☑️
```

#### Variable: URI
```
Name: uri
Type: Query
Data source: Prometheus
Query: label_values(http_server_requests_seconds_count{application="$application"}, uri)
Multi-value: ☑️
Include All option: ☑️
```

Luego usa las variables en tus queries:
```promql
rate(http_server_requests_seconds_count{application="$application", uri=~"$uri"}[1m])
```

---

## 🔧 Troubleshooting

### Problema 1: "Data source is not working"

**Solución**:
1. Verifica que Prometheus esté corriendo: `docker ps | grep prometheus`
2. Verifica la URL: debe ser `http://prometheus:9090` (nombre del servicio Docker)
3. Prueba acceder directamente: http://localhost:9090

### Problema 2: "No data" en los paneles

**Solución**:
1. Verifica en Prometheus que está scraping:
   - http://localhost:9090/targets
2. Verifica que el servicio está exponiendo métricas:
   - http://localhost:8082/actuator/prometheus
3. Verifica el nombre de la aplicación en la query
4. Ajusta el rango de tiempo (arriba a la derecha)

### Problema 3: Dashboard importado no muestra datos

**Solución**:
1. Edita el dashboard
2. En cada panel, verifica que el data source sea "Prometheus"
3. Verifica que los labels coincidan (application, instance, etc.)
4. Ajusta las queries si es necesario

---

## 📱 Paso 7: Configurar Panel Mobile-Friendly

1. En el dashboard, haz clic en **Settings** (⚙️)
2. Ve a **General**
3. Configuración recomendada:
   ```
   Timezone: Browser Time
   Auto refresh: 5s, 10s, 30s, 1m
   Hide time picker: No
   ```
4. Guarda

---

## 🎯 Dashboards Recomendados para Importar

| ID | Nombre | Descripción |
|----|--------|-------------|
| 12900 | Spring Boot APM Dashboard | Dashboard completo de Spring Boot |
| 4701 | JVM (Micrometer) | Métricas detalladas de JVM |
| 10280 | Spring Boot 2.1 Statistics | Estadísticas generales |
| 11378 | JVM Micrometer + Actuator | JVM con Actuator |
| 13230 | Spring Boot Dashboard | Dashboard alternativo |

---

## 📊 Ejemplo de Layout de Dashboard Completo

```
┌─────────────────────────────────────────────────┐
│  CoopCredit - Credit Service Dashboard         │
├───────────────────┬─────────────────────────────┤
│ HTTP Request Rate │ Average Response Time       │
│ (req/s)           │ (seconds)                   │
├───────────────────┼─────────────────────────────┤
│ HTTP 5xx Errors   │ Login Success vs Failures   │
│ (req/s)           │ (events/s)                  │
├───────────────────┼─────────────────────────────┤
│ JVM Heap Memory   │ JVM Threads                 │
│ (bytes)           │ (count)                     │
├───────────────────┴─────────────────────────────┤
│ Database Connection Pool                        │
│ (active/idle/max)                               │
├─────────────────────────────────────────────────┤
│ HTTP Requests by Endpoint (Table)               │
│ endpoint | requests | avg_time | p95 | errors  │
└─────────────────────────────────────────────────┘
```

---

## ✅ Verificación Final

Checklist de configuración:

- [ ] Grafana accesible en http://localhost:3000
- [ ] Prometheus configurado como data source
- [ ] "Data source is working" mensaje verde
- [ ] Prometheus scraping el servicio (http://localhost:9090/targets)
- [ ] Dashboard importado muestra datos
- [ ] Métricas visibles en tiempo real
- [ ] Variables de dashboard funcionan
- [ ] Alertas configuradas (opcional)

---

## 🎓 Recursos Adicionales

### Documentación Oficial
- Grafana: https://grafana.com/docs/grafana/latest/
- Prometheus: https://prometheus.io/docs/
- Micrometer: https://micrometer.io/docs

### Tutoriales
- Grafana Getting Started: https://grafana.com/docs/grafana/latest/getting-started/
- PromQL Tutorial: https://prometheus.io/docs/prometheus/latest/querying/basics/

### Comunidad
- Grafana Dashboards: https://grafana.com/grafana/dashboards/
- Grafana Community: https://community.grafana.com/

---

**¡Listo para monitorear tu microservicio! 📊🎉**

Última actualización: 11 Diciembre 2025
