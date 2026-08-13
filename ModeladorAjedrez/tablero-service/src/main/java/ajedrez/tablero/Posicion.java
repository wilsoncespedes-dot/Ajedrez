package ajedrez.tablero;

/**
 * Representa una casilla del tablero (fila, columna).
 * fila y columna van de 0 a 7 (como un tablero de 8x8).
 *
 * Es un objeto de valor inmutable: se puede pasar libremente entre
 * movimientos-service y partida-service sin exponer estado mutable
 * del tablero-service.
 */
public class Posicion {
    private final int fila;
    private final int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public boolean esValida() {
        return fila >= 0 && fila < 8 && columna >= 0 && columna < 8;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Posicion)) return false;
        Posicion otra = (Posicion) obj;
        return this.fila == otra.fila && this.columna == otra.columna;
    }

    @Override
    public int hashCode() {
        return fila * 8 + columna;
    }

    @Override
    public String toString() {
        // Convierte fila/columna a notacion de ajedrez, ej: a1, e4
        char letra = (char) ('a' + columna);
        int numero = 8 - fila;
        return "" + letra + numero;
    }
}
