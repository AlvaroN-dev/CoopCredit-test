import subprocess
import time
import sys
import os

def get_docker_cmd():
    """Determina el comando base de docker-compose, agregando sudo si es necesario."""
    # En Windows no existe sudo ni os.geteuid()
    if os.name == 'nt':
        return ["docker-compose"]

    # Si no somos root, usamos sudo
    if os.geteuid() != 0:
        return ["sudo", "docker-compose"]
    return ["docker-compose"]

def run_command(cmd, description):
    """Ejecuta un comando de shell y maneja errores."""
    print(f"🚀 {description}...")
    try:
        subprocess.check_call(cmd)
        print(f"✅ {description} completado.")
        return True
    except subprocess.CalledProcessError as e:
        print(f"❌ Error en {description}: {e}")
        return False

def start_services():
    dc = get_docker_cmd()
    print("\n🔄 Iniciando despliegue ordenado de microservicios...")
    
    # 1. Base de Datos (Postgres Credit) - Solo una BD
    if not run_command(dc + ["up", "-d", "postgres-credit"], "Iniciando Base de Datos (Postgres)"): return
    print("⏳ Esperando 15 segundos para inicialización de BD...")
    time.sleep(15)

    # 2. Config Server
    if not run_command(dc + ["up", "-d", "microservice-config"], "Iniciando Config Server"): return
    print("⏳ Esperando 20 segundos para Config Server...")
    time.sleep(20)

    # 3. Eureka Server
    if not run_command(dc + ["up", "-d", "microservice-eureka"], "Iniciando Eureka Server"): return
    print("⏳ Esperando 25 segundos para Eureka Server...")
    time.sleep(25)

    # 4. Servicios de Negocio (Credit con Auth integrado, Risk)
    business_services = [
        "microservice-credit-application-service",
        "microservice-risk-central-service"
    ]
    if not run_command(dc + ["up", "-d"] + business_services, "Iniciando Servicios de Negocio (Credit + Risk)"): return
    print("⏳ Esperando 30 segundos para servicios de negocio...")
    time.sleep(30)

    # 5. Gateway
    if not run_command(dc + ["up", "-d", "microservice-gateway"], "Iniciando Gateway"): return
    print("⏳ Esperando 15 segundos para Gateway...")
    time.sleep(15)

    # 6. Observabilidad (Prometheus y Grafana)
    observability_services = ["prometheus", "grafana"]
    if not run_command(dc + ["up", "-d"] + observability_services, "Iniciando Observabilidad (Prometheus + Grafana)"): return
    
    print("\n✨ ¡Despliegue completado! Todos los servicios están arriba.")
    print("\n📌 URLs de Acceso:")
    print("🌐 Gateway (API):        http://localhost:8080")
    print("📚 Swagger UI:           http://localhost:8080/swagger-ui.html")
    print("🔍 Eureka Dashboard:     http://localhost:8761")
    print("📊 Grafana:              http://localhost:3000 (admin/admin)")
    print("📈 Prometheus:           http://localhost:9090")
    print("\n💡 Servicios disponibles:")
    print("   - Solicitudes Crédito: http://localhost:8080/api/credit")
    print("   - Evaluación Riesgo:  http://localhost:8080/api/risk")

def stop_services():
    dc = get_docker_cmd()
    print("\n🛑 Deteniendo todos los contenedores...")
    run_command(dc + ["down"], "Deteniendo y removiendo contenedores")

def view_logs():
    dc = get_docker_cmd()
    print("\n📜 Mostrando logs (Presiona Ctrl+C para salir)...")
    try:
        # -f hace follow (tiempo real), --tail 100 muestra las ultimas 100 lineas
        subprocess.call(dc + ["logs", "-f", "--tail", "100"])
    except KeyboardInterrupt:
        print("\n👋 Salida de logs.")

def main():
    while True:
        print("\n--- GESTOR DE MICROSERVICIOS (CoopCredit) ---")
        print("1. 🚀 Iniciar Servicios (Orden: DB -> Config -> Eureka -> Credit+Risk -> Gateway -> Observability)")
        print("2. 📜 Ver Logs en Tiempo Real")
        print("3. 🛑 Detener Todos los Servicios")
        print("4. 👋 Salir")
        
        choice = input("\nSelecciona una opción (1-4): ").strip()
        
        if choice == '1':
            start_services()
        elif choice == '2':
            view_logs()
        elif choice == '3':
            stop_services()
        elif choice == '4':
            print("👋 ¡Hasta luego!")
            sys.exit(0)
        else:
            print("⚠️ Opción no válida. Intenta de nuevo.")

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\n👋 Interrupción detectada. Saliendo...")
        sys.exit(0)
