# FixIt Campus · Acceso a Datos (1T)

Programa en Java que trabaja con la base de datos PostgreSQL de FixIt Campus:

1. **Genera una plantilla Excel** (`Excel.xlsx`) con una hoja por cada una de las 12 tablas y sus columnas, para rellenarla y alimentar la base de datos.
2. **Inserta en la base de datos** lo que hay en el Excel relleno (`Excel_relleno.xlsx`). Es todo o nada: si una fila falla, no se guarda ninguna (`ROLLBACK`) y el log indica la hoja, la fila y el motivo de cada error.
3. **Genera un informe Word** (`informe_fixit.docx`) con lo que ha ocurrido en la base de datos: incidencias por estado y prioridad, incidencias fuera de plazo y operaciones registradas.
4. **Registra todo en logs**, por consola y en fichero, para poder reproducir cualquier error.

## Requisitos

- JDK 17 o superior y Maven (o IntelliJ, que ya los trae).
- Docker Desktop, para la base de datos.

## 1. Arrancar la base de datos con Docker

En la carpeta del proyecto, donde está `docker-compose.yml`:

```bash
docker compose up -d
```

La primera vez, Docker descarga PostgreSQL 16, crea la base de datos `fixit_campus` y ejecuta `database/estructura.sql`. Con `docker compose ps` se ve si ya está `healthy`.

| Para | Comando |
|---|---|
| Parar la base de datos (se conservan los datos) | `docker compose stop` |
| Volver a arrancarla | `docker compose start` |
| Empezar desde cero (borra todos los datos y vuelve a ejecutar el script) | `docker compose down -v` y luego `docker compose up -d` |

Ya va configurado: base de datos `fixit_campus`, usuario `fixit` y contraseña `fixit`, los mismos que `src/main/resources/Properties.properties`. No hay que tocar nada.

## 2. Ejecución

Con la base de datos arrancada, ejecuta la clase `Main` desde el IDE. Hace los tres pasos seguidos:

| Paso | Resultado |
|---|---|
| 1. Plantilla | Crea `Excel.xlsx` con las 12 hojas. Se sobrescribe en cada ejecución |
| 2. Inserción | Si existe `Excel_relleno.xlsx`, inserta sus filas. Sin errores hace `COMMIT`; con alguno, `ROLLBACK` |
| 3. Informe | Crea `informe_fixit.docx` |

Para rellenar vuestros datos, copia `Excel.xlsx` como `Excel_relleno.xlsx` y escribe las filas debajo de la cabecera:

- Deja vacía la columna `id`: la pone la base de datos.
- Las columnas vacías toman su valor por defecto.
- En `password_hash`, escribe la contraseña en claro: se guarda cifrada con BCrypt.
- Escribe las fechas como `AAAA-MM-DD` y los booleanos como `true` o `false`.

## Prueba de errores y logs

- `Excel_relleno.xlsx` es un ejemplo correcto. La primera vez da `COMMIT: 5 filas insertadas`.
- `Excel_con_errores.xlsx` (renómbralo a `Excel_relleno.xlsx` para probarlo) da 4 errores (email repetido, planta fuera de rango, edificio que no existe y 31 de febrero) y hace `ROLLBACK`.
- Si importas dos veces el mismo Excel, también falla: esos datos ya existen. Para volver a probarlo, empieza la base de datos desde cero (`docker compose down -v` y `docker compose up -d`).

En el log se ve cada error con su causa. Por ejemplo:

```
WARN  ExcelService - Hoja usuario, fila 2: ERROR: duplicate key value violates unique constraint "uq_usuario_email"
  Detail: Key (email)=(lucia@fixitcampus.test) already exists.
ERROR ExcelService - ROLLBACK: 5 errores, no se ha insertado nada
```

Para corregirlo, cambia en el Excel los valores que indica el log y vuelve a ejecutar.

## Estructura

```
accesoDatos/
├── pom.xml
├── docker-compose.yml            base de datos PostgreSQL en Docker
├── database/                     script que Docker ejecuta al crear la base de datos
│   └── estructura.sql            tablas, vistas y triggers
├── Excel_relleno.xlsx            ejemplo correcto
├── Excel_con_errores.xlsx        ejemplo con errores
└── src/main/
    ├── java/com/fixitcampus/accesoDatos/
    │   ├── Main.java             ejecuta los tres pasos
    │   ├── ExcelService.java     crea la plantilla e inserta el Excel relleno
    │   └── WordService.java      crea el informe Word
    └── resources/
        ├── Properties.properties datos de conexión
        └── logback.xml           configuración de los logs
```

## Librerías

| Librería | Para qué |
|---|---|
| Apache POI (`poi-ooxml`) | Crear y leer el Excel y crear el Word |
| PostgreSQL JDBC | Conexión con la base de datos |
| Logback (SLF4J) | Logs por consola y en fichero |
| jBCrypt | Cifrar las contraseñas antes de guardarlas |