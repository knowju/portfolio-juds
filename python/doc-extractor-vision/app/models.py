"""Esquemas de la salida estructurada. Pydantic valida y tipa lo que el
modelo devuelve, de modo que el resto del sistema recibe datos limpios."""

from pydantic import BaseModel, Field


class LineaFactura(BaseModel):
    descripcion: str
    cantidad: float | None = None
    importe: float | None = None


class FacturaExtraida(BaseModel):
    proveedor: str | None = Field(None, description="Nombre del emisor")
    fecha: str | None = Field(None, description="Fecha del documento (ISO si es posible)")
    folio: str | None = None
    subtotal: float | None = None
    impuestos: float | None = None
    total: float | None = None
    moneda: str | None = None
    lineas: list[LineaFactura] = []
    fuente: str = Field("modelo", description="'modelo' si lo extrajo el LLM, 'mock' si es demo sin API key")
