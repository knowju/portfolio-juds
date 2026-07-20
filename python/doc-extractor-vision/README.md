# doc-extractor-vision

API en **FastAPI** que extrae datos estructurados desde documentos (facturas en imagen o PDF) usando un **modelo con visión** (Claude). La salida se valida con **Pydantic**. Corre con o sin API key (modo demo).

## Qué demuestra

- Extracción documento → JSON estructurado a escala (PDFs, imágenes).
- Salida tipada y validada con Pydantic (contrato de datos limpio).
- Manejo de bloques `image` / `document` de la API de Anthropic.
- Diseño defensivo: sin `ANTHROPIC_API_KEY` responde un mock, así el proyecto arranca y se prueba sin credenciales.

## Correr

```bash
cd python/doc-extractor-vision
python -m venv .venv && source .venv/Scripts/activate   # Windows: .venv\Scripts\activate
pip install -r requirements.txt
cp .env.example .env        # opcional: pon tu ANTHROPIC_API_KEY
uvicorn app.main:app --reload --port 8090
```

## Probar

- **Swagger UI:** http://localhost:8090/docs
- **Con curl:**
```bash
curl -X POST http://localhost:8090/extract \
     -F "archivo=@factura.png;type=image/png"
```

Respuesta (validada):
```json
{
  "proveedor": "Distribuidora Ejemplo S.A. de C.V.",
  "fecha": "2026-07-01",
  "total": 1740.0,
  "moneda": "MXN",
  "lineas": [{"descripcion": "Servicio de consultoría", "cantidad": 1, "importe": 1500.0}],
  "fuente": "modelo"
}
```

`fuente` es `"modelo"` cuando lo extrajo el LLM, `"mock"` en modo demo.

## Stack

Python 3.12 · FastAPI · Pydantic · Anthropic (visión) · uvicorn
