package ajedrez.movimientos;

import java.util.ArrayList;
import java.util.List;

import ajedrez.tablero.Color;
import ajedrez.tablero.EstadoTablero;
import ajedrez.tablero.Pieza;
import ajedrez.tablero.Posicion;

/**
 * Regla de movimiento: Rey.
 * Se mueve una sola casilla en cualquier direccion.
 */
public class Rey extends Pieza implements CalculadorMovimientos {

    public Rey(Color color, Posicion posicion) {
        super(color, posicion);
    }

    @Override
    public List<Posicion> movimientosPosibles(EstadoTablero tablero) {
        List<Posicion> movimientos = new ArrayList<>();
        int[][] direcciones = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : direcciones) {
            Posicion destino = new Posicion(posicion.getFila() + dir[0], posicion.getColumna() + dir[1]);
            if (destino.esValida()) {
                if (tablero.estaVacia(destino) || tablero.hayPiezaEnemigaEn(destino, color)) {
                    movimientos.add(destino);
                }
            }
        }

        return movimientos;
    }

    @Override
    public String getSimbolo() {
        return "R";
    }
}
