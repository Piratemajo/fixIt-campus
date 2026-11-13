import matplotlib.pyplot as plt
import json


incidencias_json = """

[
    {
        "id": 1,
        "tipo": "Infraestructura",
        "descripcion": "Filtración de agua en el techo del aula 104 durante episodios de lluvia.",
        "gravedad": "Media",
        "ubicacion": "Aula 104",
        "estado": "Abierta"
    },
    {
        "id": 2,
        "tipo": "Tecnología",
        "descripcion": "Fallo de encendido en el proyector principal del salón de actos.",
        "gravedad": "Media",
        "ubicacion": "Salón de actos",
        "estado": "Abierta"
    },
    {
        "id": 3,
        "tipo": "Mobiliario",
        "descripcion": "Silla de alumno con rotura estructural y riesgo de corte.",
        "gravedad": "Alta",
        "ubicacion": "Aula 202",
        "estado": "Abierta"
    },
    {
        "id": 4,
        "tipo": "Convivencia",
        "descripcion": "Incidente de altercado verbal entre alumnos durante el periodo de recreo.",
        "gravedad": "Alta",
        "ubicacion": "Patio central",
        "estado": "Resuelta"
    },
    {
        "id": 5,
        "tipo": "Instalaciones",
        "descripcion": "Desabastecimiento recurrente de papel higiénico y jabón de manos.",
        "gravedad": "Media",
        "ubicacion": "Baños planta baja",
        "estado": "Resuelta"
    },
    {
        "id": 6,
        "tipo": "Tecnología",
        "descripcion": "Inestabilidad intermitente en la señal de red Wi-Fi.",
        "gravedad": "Baja",
        "ubicacion": "Sala de profesores",
        "estado": "Abierta"
    },
    {
        "id": 7,
        "tipo": "Climatización",
        "descripcion": "Inoperatividad del sistema de calefacción central.",
        "gravedad": "Alta",
        "ubicacion": "Aula 301",
        "estado": "Abierta"
    },
    {
        "id": 8,
        "tipo": "Seguridad",
        "descripcion": "Obstrucción de la salida de emergencia con almacenamiento de material deportivo.",
        "gravedad": "Crítica",
        "ubicacion": "Gimnasio",
        "estado": "Resuelta"
    },
    {
        "id": 9,
        "tipo": "Infraestructura",
        "descripcion": "Desprendimiento parcial de los anclajes de la pizarra mural.",
        "gravedad": "Alta",
        "ubicacion": "Laboratorio de Física",
        "estado": "Abierta"
    },
    {
        "id": 10,
        "tipo": "Instalaciones",
        "descripcion": "Avería en la llave de paso y grifería del lavabo.",
        "gravedad": "Media",
        "ubicacion": "Laboratorio de Química",
        "estado": "Abierta"
    },
    {
        "id": 11,
        "tipo": "Limpieza",
        "descripcion": "Acumulación de residuos sólidos y restos orgánicos en zona exterior.",
        "gravedad": "Baja",
        "ubicacion": "Patio trasero",
        "estado": "Resuelta"
    },
    {
        "id": 12,
        "tipo": "Convivencia",
        "descripcion": "Incumplimiento reiterado de la normativa sobre dispositivos móviles en el aula.",
        "gravedad": "Baja",
        "ubicacion": "Aula 105",
        "estado": "Resuelta"
    },
    {
        "id": 13,
        "tipo": "Infraestructura",
        "descripcion": "Sustitución requerida de luminaria fundida en zona de tránsito.",
        "gravedad": "Baja",
        "ubicacion": "Pasillo bloque B",
        "estado": "Resuelta"
    },
    {
        "id": 14,
        "tipo": "Material escolar",
        "descripcion": "Insuficiencia de equipos ópticos operativos para prácticas experimentales.",
        "gravedad": "Media",
        "ubicacion": "Laboratorio de Biología",
        "estado": "Abierta"
    },
    {
        "id": 15,
        "tipo": "Tecnología",
        "descripcion": "Fallo de periférico (teclado no reconocido por la estación de trabajo docente).",
        "gravedad": "Baja",
        "ubicacion": "Aula 204",
        "estado": "Resuelta"
    },
    {
        "id": 16,
        "tipo": "Seguridad",
        "descripcion": "Rotura de panel de vidrio con formación de astillas expuestas.",
        "gravedad": "Alta",
        "ubicacion": "Pasillo 2ª planta",
        "estado": "Abierta"
    },
    {
        "id": 17,
        "tipo": "Convivencia",
        "descripcion": "Absentismo injustificado y continuado en el primer tramo horario.",
        "gravedad": "Media",
        "ubicacion": "Secretaría / Jefatura",
        "estado": "Abierta"
    },
    {
        "id": 18,
        "tipo": "Instalaciones",
        "descripcion": "Obstrucción completa en el sistema de evacuación de aguas de los aseos.",
        "gravedad": "Alta",
        "ubicacion": "Vestuarios polideportivo",
        "estado": "Abierta"
    },
    {
        "id": 19,
        "tipo": "Mobiliario",
        "descripcion": "Bloqueo mecánico en el mecanismo de apertura del archivador metálico.",
        "gravedad": "Baja",
        "ubicacion": "Departamento de Orientación",
        "estado": "Resuelta"
    },
    {
        "id": 20,
        "tipo": "Infraestructura",
        "descripcion": "Desincronización en la señal acústica de finalización de clases.",
        "gravedad": "Baja",
        "ubicacion": "Sistema general",
        "estado": "Resuelta"
    }
]



"""

def mostrar_grafica():
    data = json.loads(incidencias_json)


    gravedad_map = {"Baja": 1, "Media": 2, "Alta": 3}

    x = [incidencia["id"] for incidencia in data]
    y = [gravedad_map[incidencia["gravedad"]] for incidencia in data]  

    plt.figure(figsize=(10,4))
    plt.bar(x, y, color='pink')
    plt.xlabel('ID incidencia')
    plt.ylabel('Gravedad')
    plt.title('Gravedad por incidencia')
    plt.ylim(0, 3.5)
    plt.yticks([1, 2, 3], ['Baja', 'Media', 'Alta'])
    plt.xticks(x)
    plt.show()
mostrar_grafica()