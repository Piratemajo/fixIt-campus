"""Gráficos PNG generados en memoria con matplotlib."""

from io import BytesIO

from matplotlib.figure import Figure


def grafico_barras(datos: dict[str, float], titulo: str) -> bytes:

    figura = Figure(figsize=(8, 1.2 + 0.4 * len(datos)), dpi=150)
    ejes = figura.add_subplot()

    barras = ejes.barh(list(datos), list(datos.values()), color="#2a78d6")
    ejes.invert_yaxis()  # el mayor arriba
    ejes.bar_label(barras, fmt="%.3g", padding=4)
    ejes.set_xlim(0, max(datos.values(), default=1) * 1.15)
    ejes.set_title(titulo, loc="left", fontweight="bold")
    figura.tight_layout()

    memoria = BytesIO()  # el PNG se guarda en memoria, no en un fichero
    figura.savefig(memoria, format="png")
    figura.show
    return memoria.getvalue()
