"""Analiza el JSON de incidencias y guarda los gráficos en graficos/."""

from datetime import date
from io import BytesIO
from pathlib import Path

import matplotlib.pyplot as plt

from analisis import (espacios_con_mas_averias, fuera_de_plazo,
                      tiempo_medio_resolucion)
from datos import cargar_incidencias
from graficos import grafico_barras

CARPETA = Path(__file__).parent
FICHERO_JSON = CARPETA / "data" / "incidencias.json"
SALIDA = CARPETA / "graficos"


def mostrar(graficos: dict[str, bytes]) -> None:
    """Abre una ventana con los gráficos, uno debajo de otro."""
    figura, ejes = plt.subplots(len(graficos), 1, figsize=(9, 8))
    for eje, png in zip(ejes, graficos.values()):
        eje.imshow(plt.imread(BytesIO(png)))
        eje.axis("off")
    figura.canvas.manager.set_window_title("FixIt Campus · Gráficos")
    figura.tight_layout()
    plt.show()


def main() -> None:
    """Muestra el análisis por consola y guarda los gráficos."""
    incidencias = cargar_incidencias(FICHERO_JSON)
    print(f"{len(incidencias)} incidencias leídas\n")

    medias = tiempo_medio_resolucion(incidencias)
    print("Tiempo medio de resolución por categoría:")
    for categoria, horas in medias.items():
        print(f"  {categoria:<15} {horas:7.1f} h")

    hoy = date.today()
    retrasadas = fuera_de_plazo(incidencias, hoy)
    print(f"\nFuera de plazo a {hoy:%d/%m/%Y}: {len(retrasadas)}")
    for inc in retrasadas:
        print(f"  {inc['codigo']}  objetivo {inc['fecha_objetivo']:%d/%m}")

    espacios = espacios_con_mas_averias(incidencias)
    print("\nEspacios con más averías:")
    for espacio, total in espacios:
        print(f"  {espacio:<8} {total}")

    SALIDA.mkdir(exist_ok=True)
    graficos = {
        "tiempo_medio_por_categoria.png": grafico_barras(
            medias, "Tiempo medio de resolución por categoría (horas)"),
        "espacios_con_mas_averias.png": grafico_barras(
            dict(espacios), "Espacios con más averías"),
    }
    print()
    for nombre, png in graficos.items():
        (SALIDA / nombre).write_bytes(png)
        print(f"Gráfico guardado: graficos/{nombre}")

    mostrar(graficos)


if __name__ == "__main__":
    main()