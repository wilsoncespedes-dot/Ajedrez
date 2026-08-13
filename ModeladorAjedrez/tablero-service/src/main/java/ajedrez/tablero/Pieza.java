package ajedrez.tablero;

import java.util.List;

/**
 * Clase abstracta que representa una pieza de ajedrez cualquiera.
 * Vive en tablero-service porque el tablero necesita saber que es una
 * "Pieza" sin importar si es un Peon, una Torre, etc.
 *
 * Las clases concretas (Peon, Torre, Caballo, Alfil, Reina, Rey) viven en
 * movimientos-service y heredan de esta clase para definir COMO se mueve
 * cada una. Por eso movimientosPosibles recibe un EstadoTablero (el
 * contrato de solo lectura) y no la clase Tablero completa: asi
 * movimientos-service solo puede consultar el tablero, nunca mutarlo
 * directamente.
 */
public abstract class Pieza {

    protected Color color;
    protected Posicion posicion;

    public Pieza(Color color, Posicion posicion) {
        this.color = color;
        this.posicion = posicion;
    }

    public Color getColor() {
        return color;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void moverA(Posicion nuevaPosicion) {
        this.posicion = nuevaPosicion;
    }

    /**
     * Cada pieza concreta (en movimientos-service) debe decir cuales son
     * sus movimientos posibles segun donde este parada y como esta el
     * tablero. Solo recibe la vista de lectura (EstadoTablero).
     */
    public abstract List<Posicion> movimientosPosibles(EstadoTablero tablero);

    /**
     * Letra que representa la pieza al imprimir el tablero (P, T, C, A, R, D)
     */
    public abstract String getSimbolo();

    @Override
    public String toString() {
        return (color == Color.BLANCO ? "B" : "N") + getSimbolo();
    }
}
