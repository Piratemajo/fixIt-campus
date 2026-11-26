"""Funciones de análisis de las incidencias."""

from collections import Counter
from datetime import date


def tiempo_medio_resolucion(incidencias: list[dict]) -> dict[str, float]:
    """Calcula el tiempo medio de resolución por categoría, en horas.
    """
    horas: dict[str, list[float]] = {}
    for inc in incidencias:
        if inc["fecha_resolucion"] is not None:
            duracion = inc["fecha_resolucion"] - inc["fecha_creacion"]
            horas.setdefault(inc["categoria"], []).append(
                duracion.total_seconds() / 3600)

    medias = {cat: sum(lista) / len(lista) for cat, lista in horas.items()}
    return dict(sorted(medias.items(), key=lambda par: par[1], reverse=True))


def fuera_de_plazo(incidencias: list[dict], hoy: date) -> list[dict]:
    """Devuelve las incidencias que no han cumplido su fecha objetivo.
    """
    retrasadas = []
    for inc in incidencias:
        if inc["fecha_resolucion"] is None:
            tarde = inc["fecha_objetivo"] < hoy
        else:
            tarde = inc["fecha_resolucion"].date() > inc["fecha_objetivo"]
        if tarde:
            retrasadas.append(inc)
    return sorted(retrasadas, key=lambda inc: inc["fecha_objetivo"])


def espacios_con_mas_averias(incidencias: list[dict],
                             cuantos: int = 5) -> list[tuple[str, int]]:
    """Devuelve los espacios con más incidencias.
    """
    cuenta = Counter(inc["espacio"] for inc in incidencias)
    return cuenta.most_common(cuantos)
