package ajedrez.tablero;

/**
 * Representa el color de una pieza o de un jugador.
 * Vive en tablero-service porque todo el estado del tablero depende de esto.
 */
public enum Color {
    BLANCO,
    NEGRO;

    public Color contrario() {
        return this == BLANCO ? NEGRO : BLANCO;
    }
}
