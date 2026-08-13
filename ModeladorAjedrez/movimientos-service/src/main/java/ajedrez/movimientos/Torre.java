package ajedrez.movimientos;

import java.util.ArrayList;
import java.util.List;

import ajedrez.tablero.Color;
import ajedrez.tablero.EstadoTablero;
import ajedrez.tablero.Pieza;
import ajedrez.tablero.Posicion;

/**
 * Regla de movimiento: Torre.
 * Se mueve en linea recta: horizontal o vertical, cuantas casillas quiera.
 */
public class Torre extends Pieza implements CalculadorMovimientos {

    public Torre(Color color, Posicion posicion) {
        super(color, posicion);
    }

    @Override
    public List<Posicion> movimientosPosibles(EstadoTablero tablero) {
        List<Posicion> movimientos = new ArrayList<>();
        int[][] direcciones = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} }; // arriba, abajo, izq, der

        for (int[] dir : direcciones) {
            int fila = posicion.getFila() + dir[0];
            int col = posicion.getColumna() + dir[1];

            while (new Posicion(fila, col).esValida()) {
                Posicion actual = new Posicion(fila, col);

                if (tablero.estaVacia(actual)) {
                    movimientos.add(actual);
                } else {
                    if (tablero.hayPiezaEnemigaEn(actual, color)) {
                        movimientos.add(actual);
                    }
                    break; // topo con una pieza, no puede seguir en esa direccion
                }

                fila += dir[0];
                col += dir[1];
            }
        }

        return movimientos;
    }

    @Override
    public String getSimbolo() {
        return "T";
    }
}
