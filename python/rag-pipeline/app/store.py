"""Store vectorial en memoria. Guarda (texto, vector) y recupera los top-k por
similitud coseno. En producción esto sería pgvector, Pinecone, Qdrant, etc.,
detrás de la misma interfaz."""

import numpy as np

from .embeddings import coseno


class VectorStore:
    def __init__(self):
        self._textos: list[str] = []
        self._vectores: list[np.ndarray] = []

    def agregar(self, texto: str, vector: np.ndarray) -> None:
        self._textos.append(texto)
        self._vectores.append(vector)

    def buscar(self, consulta: np.ndarray, k: int = 3) -> list[tuple[str, float]]:
        puntuados = [
            (texto, coseno(consulta, vec))
            for texto, vec in zip(self._textos, self._vectores)
        ]
        puntuados.sort(key=lambda t: t[1], reverse=True)
        return puntuados[:k]

    def tamano(self) -> int:
        return len(self._textos)
