# FixIt Campus

FixIt Campus es un sistema de gestión del mantenimiento de un campus. Cualquier persona del campus avisa de una avería con una foto, y el equipo de mantenimiento la asigna, la sigue y la cierra desde un panel web. Cada incidencia tiene una prioridad y una fecha objetivo, las facturas de los proveedores externos quedan unidas a la avería que arreglan, y un servicio en Python convierte los datos en gráficos.

Proyecto Intermodular · 2.º de Desarrollo de Aplicaciones Multiplataforma (2.º DAM) · Curso 2026-2027.

## Autores

- Javier Peces
- Víctor García
- Miguel Inacio

## Arquitectura

```
 Web (React)  ──►  API del backend (Java)  ──►  PostgreSQL
                            │
                            ├──►  Servicio de analítica (Python): gráficos en PNG
                            └──►  Sistema de facturación (proveedores y facturas)
```

| Parte | Tecnología | Carpeta |
|---|---|---|
| Base de datos | PostgreSQL 16 (Docker) | `database/` |
| Backend: acceso a datos y concurrencia | Java 17, Maven, JDBC, Apache POI, Logback | `backend/` |
| Analítica | Python 3.11 o superior, matplotlib | `analytics/` |
| Web | React | `web/` |
| Facturación | Odoo | `facturacion/` |

## Estructura del repositorio

```
fixit-campus/
├── backend/        Java: acceso a datos (plantilla Excel, importación, informe Word, logs) y concurrencia (hilos)
├── analytics/      Python: análisis de incidencias y gráficos
├── web/            web en React
├── facturacion/    sistema de facturación
├── database/       scripts SQL
├── data/           ficheros JSON/CSV de carga inicial
├── design/         wireframes, prototipo y guía de estilo
├── docs/           API, inglés, IPE, digitalización y sostenibilidad
└── README.md
```

Cada carpeta tiene su propio README con las instrucciones detalladas.

## Requisitos

| Para ejecutar | Hace falta |
|---|---|
| Base de datos | Docker Desktop |
| Backend | JDK 17 o superior y Maven (IntelliJ IDEA ya los trae) |
| Analítica | Python 3.11 o superior |

## Instalación y ejecución

### 1. Base de datos

```bash
docker compose up -d
```

La primera vez, Docker descarga PostgreSQL 16, crea la base de datos `fixit_campus` y ejecuta el script de `database/`. Ya va configurado: usuario `fixit`, contraseña `fixit` y puerto `5432`.

Para empezar desde cero (borra todos los datos):

```bash
docker compose down -v
docker compose up -d
```

### 2. Backend

1. Abre `backend/` en IntelliJ IDEA como proyecto Maven.
2. Revisa los datos de conexión en `src/main/resources/Properties.properties` (ya coinciden con los de Docker).
3. Ejecuta la clase principal:
   - **Acceso a datos**: `Main` del paquete `accesoDatos`. Crea la plantilla Excel, importa el Excel relleno (todo o nada) y genera el informe Word.
   - **Concurrencia**: `Main` del paquete `psp`. Envía los correos de aviso en un hilo en segundo plano y muestra todos los estados de un hilo.

### 3. Analítica

```bash
cd analytics
python -m venv venv
venv\Scripts\activate              # Linux/macOS: source venv/bin/activate
pip install -r requirements.txt
python main.py
```

Muestra el análisis por consola, guarda dos gráficos PNG en `analytics/graficos/` y los enseña en una ventana.

## Estado (1T)

| Módulo | Entregado |
|---|---|
| Acceso a Datos | Script de la base de datos, plantilla Excel generada con Java, importación con rollback, informe Word y logs |
| Programación de Servicios y Procesos | Hilo de correos en segundo plano, `Thread`, `Runnable`, lambda, clase anónima, todos los estados de un hilo, contador atómico, `ExecutorService` y `Future` |
| Python | Análisis de 22 incidencias de ejemplo y dos gráficos generados en memoria |
| Digitalización | Informe del proceso actual, diagnóstico de madurez y tecnologías habilitadoras |
| Sostenibilidad | Relación con los ODS y los criterios ASG, e impactos ambientales, sociales y económicos |