# FixIt Campus · Programación de Servicios y Procesos 

Ejemplos de concurrencia en Java aplicados a FixIt Campus: varios usuarios declaran incidencias a la vez y un hilo en segundo plano envía los correos de aviso.

## Requisitos

- JDK 17 o superior y Maven (o IntelliJ, que ya los trae).
- No necesita base de datos.

## Ejecución

Ejecuta la clase `Main` desde el IDE. Hace dos demostraciones seguidas:

1. **Correos en segundo plano** (`Main`):
   - 5 usuarios declaran una incidencia a la vez, en un `ExecutorService` de 3 hilos.
   - Cada declaración deja su correo en una cola y sigue con lo suyo, sin esperar.
   - El hilo `enviador-correos` los va enviando en segundo plano, y un hilo vigilante informa cada segundo de los correos pendientes.
   - Un `Callable` devuelve cuántos correos quedan, que se recoge con un `Future`.
2. **Estados de un hilo** (`ThreadEstados`): un hilo pasa por los 6 estados y se muestra cada uno.

Salida (resumida):

```
[pool-1-thread-1] DeclaracionIncidencia - FIX-1 declarada; el correo se envía en segundo plano
[main] Main - Correos pendientes según el Future: 5
[enviador-correos] EnviadorCorreos - Correo enviado: FIX-1 en A-01: El proyector no enciende
[main] Main - Correos enviados: 5. Hilo de correos: TERMINATED
[main] ThreadEstados - Estado: NEW
[main] ThreadEstados - Estado: RUNNABLE
[main] ThreadEstados - Estado: TIMED_WAITING
[main] ThreadEstados - Estado: WAITING
[main] ThreadEstados - Estado: BLOCKED
[main] ThreadEstados - Estado: TERMINATED
```


## Logs

Se usa SLF4J con Logback. La configuración está en `src/main/resources/logback.xml`:

- Los mensajes salen por consola y se guardan en `logs/fixit.log`.
- Cada línea indica el hilo que la escribe (`[main]`, `[pool-1-thread-2]`, `[enviador-correos]`, `[vigilante]`). Así se ve qué hace cada hilo y en qué orden.

## Qué se pide y dónde está

| Requisito | Dónde |
|---|---|
| Crear hilos heredando de `Thread` | `EnviadorCorreos` |
| Crear hilos implementando `Runnable` | `DeclaracionIncidencia` |
| Crear hilos con una lambda | `ThreadEstados` y el `Callable` de `Main` |
| Crear hilos con una clase anónima | El vigilante de `Main` |
| Tarea concurrente en segundo plano (envío de correos automáticos) | `EnviadorCorreos` |
| Todos los estados de un hilo | `ThreadEstados` |
| Recurso atómico | `AtomicInteger` en `DeclaracionIncidencia` (códigos FIX) y en `EnviadorCorreos` (correos enviados) |
| `ExecutorService` y resultado devuelto por un `Future` | `Main` |

## Estados de un hilo

| Estado | Cuándo se da en `ThreadEstados` |
|---|---|
| `NEW` | Creado, antes de `start()` |
| `RUNNABLE` | Trabajando (un bucle de 300 ms) |
| `TIMED_WAITING` | En `Thread.sleep(300)` |
| `WAITING` | En `wait()`, hasta que otro hilo hace `notify()` |
| `BLOCKED` | Quiere entrar en el `synchronized`, pero el cerrojo lo tiene `main` |
| `TERMINATED` | Ha terminado `run()` |

## Conceptos

- **Condición de carrera:** dos hilos modifican el mismo dato a la vez y el resultado depende de cuál vaya primero. Por ejemplo, con un `int` y `contador++`, dos declaraciones podrían sacar el mismo código FIX.
- **Corrupción de memoria:** el dato compartido queda con un valor incorrecto por esa condición de carrera.
- **Cómo se evita aquí:**
  - `AtomicInteger.incrementAndGet()` hace la suma de una vez, sin que otro hilo se meta en medio.
  - `LinkedBlockingQueue` es una cola segura entre hilos.
  - `volatile` hace que el hilo de correos vea el aviso de terminar.
- **Bloqueante y no bloqueante:**
  - `Future.get()`, `join()` y `wait()` bloquean el hilo hasta tener el resultado.
  - `cola.add()` es no bloqueante: la declaración deja el correo y sigue sin esperar a que se envíe.

## Estructura

```
psp/
├── Main.java                   correos en segundo plano, ExecutorService, Future, lambda y clase anónima
├── ThreadEstados.java          los 6 estados de un hilo
├── EnviadorCorreos.java        hilo de correos en segundo plano (extends Thread)
├── DeclaracionIncidencia.java  declaración de una incidencia (implements Runnable)
└── PSP.md

src/main/resources/
└── logback.xml                 configuración de los logs
```