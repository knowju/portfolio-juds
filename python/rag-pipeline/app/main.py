"""API RAG: ingiere documentos y responde preguntas sobre ellos."""

from fastapi import FastAPI
from pydantic import BaseModel

from .rag import RagPipeline

app = FastAPI(
    title="rag-pipeline",
    description="Pipeline RAG: ingesta, chunking, embeddings, recuperación y respuesta.",
    version="1.0.0",
)

pipeline = RagPipeline()


class IngestRequest(BaseModel):
    texto: str


class QueryRequest(BaseModel):
    pregunta: str
    k: int = 3


@app.get("/health")
def health() -> dict:
    return {"status": "ok", "chunks_indexados": pipeline.store.tamano()}


@app.post("/ingest")
def ingest(req: IngestRequest) -> dict:
    n = pipeline.ingerir(req.texto)
    return {"chunks_agregados": n, "total_indexado": pipeline.store.tamano()}


@app.post("/query")
def query(req: QueryRequest) -> dict:
    return pipeline.consultar(req.pregunta, req.k)
