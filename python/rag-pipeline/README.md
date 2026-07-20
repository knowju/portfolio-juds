# rag-pipeline

Pipeline **RAG** (Retrieval-Augmented Generation) en **FastAPI**: ingiere texto, lo trocea, lo indexa por embeddings y responde preguntas recuperando los fragmentos más relevantes. Corre **offline** (embedder local); si hay API key, genera la respuesta con el LLM.

## Qué demuestra

- Las etapas reales de un RAG: **chunking → embeddings → indexación → recuperación (top-k por coseno) → generación**.
- Diseño por interfaces: el `Embedder` y el `VectorStore` son intercambiables (hoy locales, mañana Voyage/OpenAI + pgvector/Qdrant) sin tocar el pipeline.
- Recuperación con score de similitud, para inspeccionar *por qué* se eligió cada fragmento (groundedness).
- Arranca sin credenciales ni GPU.

## Correr

```bash
cd python/rag-pipeline
python -m venv .venv && .venv\Scripts\activate      # Windows
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8091
```

## Probar

```bash
# 1. Ingerir un documento
curl -X POST http://localhost:8091/ingest \
     -H "Content-Type: application/json" \
     -d '{"texto":"Real Flow AI es una agencia mexicana de IA en Xalapa. Ofrece bots de WhatsApp con IA, extracción de documentos y reportes automáticos."}'

# 2. Preguntar
curl -X POST http://localhost:8091/query \
     -H "Content-Type: application/json" \
     -d '{"pregunta":"¿Qué servicios ofrece Real Flow AI?","k":2}'
```

La respuesta trae el `contexto` recuperado con su `score`. Con `ANTHROPIC_API_KEY` puesta, `respuesta` la redacta el modelo usando solo ese contexto.

Swagger UI: http://localhost:8091/docs

## Stack

Python 3.12 · FastAPI · NumPy · Pydantic · Anthropic (opcional) · uvicorn
