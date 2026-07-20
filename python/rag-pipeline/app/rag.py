"""Orquesta el pipeline RAG: ingesta (chunk → embed → store) y consulta
(embed pregunta → recuperar top-k → generar respuesta con el contexto).

La generación usa el LLM si hay ANTHROPIC_API_KEY; si no, devuelve el contexto
recuperado (modo demo), que es lo que de verdad prueba la parte de 'retrieval'."""

import os

from .chunker import chunk_text
from .embeddings import LocalHashingEmbedder
from .store import VectorStore

MODELO = os.getenv("ANTHROPIC_MODEL", "claude-sonnet-4-5")


class RagPipeline:
    def __init__(self):
        self.embedder = LocalHashingEmbedder()
        self.store = VectorStore()

    def ingerir(self, texto: str) -> int:
        chunks = chunk_text(texto)
        for c in chunks:
            self.store.agregar(c, self.embedder.embed(c))
        return len(chunks)

    def consultar(self, pregunta: str, k: int = 3) -> dict:
        vec = self.embedder.embed(pregunta)
        recuperados = self.store.buscar(vec, k)
        contexto = [{"texto": t, "score": round(s, 4)} for t, s in recuperados]

        respuesta = self._generar(pregunta, [t for t, _ in recuperados])
        return {"respuesta": respuesta, "contexto": contexto}

    def _generar(self, pregunta: str, fragmentos: list[str]) -> str:
        api_key = os.getenv("ANTHROPIC_API_KEY")
        contexto = "\n\n".join(fragmentos) if fragmentos else ""
        if not api_key:
            if not contexto:
                return "[demo] No hay documentos ingeridos todavía."
            return f"[demo · sin API key] Contexto recuperado para responder:\n{contexto}"

        from anthropic import Anthropic

        client = Anthropic(api_key=api_key)
        prompt = (
            f"Responde la pregunta usando SOLO el contexto. Si no está en el "
            f"contexto, dilo.\n\nContexto:\n{contexto}\n\nPregunta: {pregunta}"
        )
        msg = client.messages.create(
            model=MODELO,
            max_tokens=512,
            messages=[{"role": "user", "content": prompt}],
        )
        return msg.content[0].text.strip()
