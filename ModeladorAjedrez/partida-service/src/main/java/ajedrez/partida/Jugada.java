package ajedrez.partida;

import ajedrez.tablero.Pieza;
import ajedrez.tablero.Posicion;

/**
 * Registro de una jugada ya ejecutada: que pieza se movio, de donde a
 * donde, y si hubo captura. Es el tipo de dato que devolveria un
 * GET /partida/historial si este modulo se convirtiera en un servicio
 * con API propia.
 */
public class Jugada {

    private final Pieza piezaMovida;
    private final Posicion origen;
    private final Posicion destino;
    private final Pieza piezaCapturada; // null si no hubo captura

    public Jugada(Pieza piezaMovida, Posicion origen, Posicion destino, Pieza piezaCapturada) {
        this.piezaMovida = piezaMovida;
        this.origen = origen;
        this.destino = destino;
        this.piezaCapturada = piezaCapturada;
    }

    public Pieza getPiezaMovida() {
        return piezaMovida;
    }

    public Posicion getOrigen() {
        return origen;
    }

    public Posicion getDestino() {
        return destino;
    }

    public Pieza getPiezaCapturada() {
        return piezaCapturada;
    }

    public boolean huboCaptura() {
        return piezaCapturada != null;
    }

    @Override
    public String toString() {
        String base = piezaMovida + ": " + origen + " -> " + destino;
        return huboCaptura() ? base + " (captura " + piezaCapturada + ")" : base;
    }
}
