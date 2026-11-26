"""Carga de las incidencias desde el fichero JSON."""

import json
from datetime import date, datetime
from pathlib import Path


def cargar_incidencias(ruta: Path) -> list[dict]:
    """Lee el JSON y devuelve las incidencias con las fechas convertidas."""
    with open(ruta, encoding="utf-8") as fichero:
        datos = json.load(fichero)

    for inc in datos:
        inc["fecha_creacion"] = datetime.fromisoformat(inc["fecha_creacion"])
        inc["fecha_objetivo"] = date.fromisoformat(inc["fecha_objetivo"])
        if inc.get("fecha_resolucion"):
            inc["fecha_resolucion"] = datetime.fromisoformat(
                inc["fecha_resolucion"])
        else:
            inc["fecha_resolucion"] = None
    return datos
