"""Embedder local y determinista (hashing bag-of-words) para que el proyecto
corra SIN servicios externos ni GPU. En producción se reemplaza por embeddings
de un modelo real (OpenAI, Voyage, Cohere, sentence-transformers) implementando
la misma interfaz `embed`."""

import hashlib
import re

import numpy as np

DIM = 256


def _hash_estable(token: str) -> int:
    """Hash determinista (no depende de PYTHONHASHSEED)."""
    return int.from_bytes(hashlib.md5(token.encode("utf-8")).digest()[:4], "little")


class Embedder:
    """Interfaz. Cambiar de embedder = implementar embed() y devolver un vector."""

    def embed(self, texto: str) -> np.ndarray:  # pragma: no cover - interfaz
        raise NotImplementedError


class LocalHashingEmbedder(Embedder):
    def __init__(self, dim: int = DIM):
        self.dim = dim

    def embed(self, texto: str) -> np.ndarray:
        vec = np.zeros(self.dim, dtype=np.float32)
        for token in re.findall(r"\w+", texto.lower()):
            idx = _hash_estable(token) % self.dim
            vec[idx] += 1.0
        norma = np.linalg.norm(vec)
        return vec / norma if norma > 0 else vec


def coseno(a: np.ndarray, b: np.ndarray) -> float:
    denom = np.linalg.norm(a) * np.linalg.norm(b)
    return float(np.dot(a, b) / denom) if denom > 0 else 0.0
