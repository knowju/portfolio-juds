"""API de extracción de documentos. Subes una factura (imagen o PDF) y
recibes sus datos estructurados y validados."""

from fastapi import FastAPI, File, HTTPException, UploadFile

from .extractor import extraer
from .models import FacturaExtraida

app = FastAPI(
    title="doc-extractor-vision",
    description="Extracción de datos estructurados desde documentos con un modelo de visión.",
    version="1.0.0",
)

TIPOS_PERMITIDOS = {"image/png", "image/jpeg", "image/webp", "application/pdf"}


@app.get("/health")
def health() -> dict:
    return {"status": "ok"}


@app.post("/extract", response_model=FacturaExtraida)
async def extract(archivo: UploadFile = File(...)) -> FacturaExtraida:
    if archivo.content_type not in TIPOS_PERMITIDOS:
        raise HTTPException(
            status_code=415,
            detail=f"Tipo no soportado: {archivo.content_type}. "
                   f"Permitidos: {', '.join(sorted(TIPOS_PERMITIDOS))}",
        )
    datos = await archivo.read()
    if not datos:
        raise HTTPException(status_code=400, detail="Archivo vacío")

    return extraer(datos, archivo.content_type)
