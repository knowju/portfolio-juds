"""Núcleo de extracción: manda el documento (imagen o PDF) a un modelo con
visión y devuelve una FacturaExtraida. Si no hay ANTHROPIC_API_KEY, corre en
modo demo (mock) para que el proyecto arranque sin credenciales."""

import base64
import json
import os

from .models import FacturaExtraida

MODELO = os.getenv("ANTHROPIC_MODEL", "claude-sonnet-4-5")

SYSTEM = (
    "Eres un extractor de datos de facturas. Recibes la imagen o PDF de una "
    "factura y devuelves EXCLUSIVAMENTE un JSON con las claves: proveedor, "
    "fecha, folio, subtotal, impuestos, total, moneda, lineas (lista de "
    "{descripcion, cantidad, importe}). Usa null donde no haya dato. No "
    "agregues texto fuera del JSON."
)


def extraer(datos: bytes, media_type: str) -> FacturaExtraida:
    api_key = os.getenv("ANTHROPIC_API_KEY")
    if not api_key:
        return _mock()

    from anthropic import Anthropic

    client = Anthropic(api_key=api_key)
    b64 = base64.standard_b64encode(datos).decode("utf-8")

    # Bloque de imagen o de documento (PDF) según el media_type.
    if media_type == "application/pdf":
        contenido_doc = {
            "type": "document",
            "source": {"type": "base64", "media_type": media_type, "data": b64},
        }
    else:
        contenido_doc = {
            "type": "image",
            "source": {"type": "base64", "media_type": media_type, "data": b64},
        }

    respuesta = client.messages.create(
        model=MODELO,
        max_tokens=1024,
        system=SYSTEM,
        messages=[{
            "role": "user",
            "content": [
                contenido_doc,
                {"type": "text", "text": "Extrae los datos de esta factura en JSON."},
            ],
        }],
    )

    texto = respuesta.content[0].text.strip()
    datos_json = _parse_json(texto)
    datos_json["fuente"] = "modelo"
    return FacturaExtraida(**datos_json)


def _parse_json(texto: str) -> dict:
    """El modelo a veces envuelve el JSON en ```json ... ```; lo limpiamos."""
    if texto.startswith("```"):
        texto = texto.split("```")[1]
        if texto.startswith("json"):
            texto = texto[4:]
    return json.loads(texto.strip())


def _mock() -> FacturaExtraida:
    return FacturaExtraida(
        proveedor="Distribuidora Ejemplo S.A. de C.V.",
        fecha="2026-07-01",
        folio="A-1024",
        subtotal=1500.0,
        impuestos=240.0,
        total=1740.0,
        moneda="MXN",
        lineas=[
            {"descripcion": "Servicio de consultoría", "cantidad": 1, "importe": 1500.0},
        ],
        fuente="mock",
    )
