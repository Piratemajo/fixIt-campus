# FixIt Campus · Python

Programa en Python que lee un JSON con incidencias de mantenimiento del campus, muestra tres análisis por consola y genera dos gráficos en PNG. Los gráficos se crean en memoria, se guardan en `graficos/` y se enseñan en una ventana.

## Requisitos

- Python 3.11 o superior.
- Las librerías de `requirements.txt` (`matplotlib` para los gráficos y `flake8` para revisar el estilo).

## Instalación (una sola vez)

Dentro de la carpeta `analytics`:

```bash
python -m venv venv
venv\Scripts\activate              
pip install -r requirements.txt
```

## Ejecución

Ejecuta `main.py`, desde el IDE o con:

```bash
python main.py
```

No necesita argumentos. Hace lo siguiente:

1. Lee `data/incidencias.json` (22 incidencias).
2. Muestra por consola:
   - el tiempo medio de resolución por categoría, en horas;
   - las incidencias fuera de plazo a fecha de hoy;
   - los espacios con más averías.
3. Guarda `tiempo_medio_por_categoria.png` y `espacios_con_mas_averias.png` en `graficos/`.
4. Abre una ventana con los dos gráficos. El programa termina al cerrarla.

## Revisión de estilo (PEP 8)

```bash
flake8 --exclude venv .
```

Si no sale nada, no hay avisos.

## Estructura

```
analytics/
├── main.py            punto de entrada: análisis, gráficos y ventana
├── datos.py           carga el JSON y convierte las fechas
├── analisis.py        funciones de análisis
├── graficos.py        gráfico de barras generado en memoria que devuelve el PNG
├── data/
│   ├── incidencias.json    22 incidencias simuladas
├── graficos/          PNG generados
├── tests/             pruebas
└── requirements.txt   librerías con su versión
```

## Formato del JSON

Una lista de incidencias. Cada una tiene:

| Campo | Ejemplo | Obligatorio |
|---|---|---|
| `codigo` | `"FIX-0001"` | Sí |
| `categoria` | `"Audiovisuales"` | Sí |
| `espacio` | `"A-01"` | Sí |
| `espacio_nombre` | `"Aula A-01"` | No |
| `edificio` | `"Edificio A · Aulario"` | No |
| `prioridad` | `"ALTA"` | Sí |
| `estado` | `"CERRADA"` | Sí |
| `fecha_creacion` | `"2026-10-19T09:10:00"` | Sí |
| `fecha_objetivo` | `"2026-10-20"` | Sí |
| `fecha_resolucion` | `"2026-10-20T10:30:00"` o `null` | No (`null` si no está resuelta) |