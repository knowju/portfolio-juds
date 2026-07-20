"""Trocea un texto en fragmentos con solapamiento. El solapamiento evita
cortar ideas justo en el límite entre chunks."""


def chunk_text(texto: str, tam: int = 500, solapamiento: int = 50) -> list[str]:
    texto = " ".join(texto.split())   # normaliza espacios/saltos
    if not texto:
        return []

    chunks: list[str] = []
    inicio = 0
    while inicio < len(texto):
        fin = inicio + tam
        chunks.append(texto[inicio:fin])
        inicio = fin - solapamiento
        if inicio <= 0:
            inicio = fin
    return chunks
