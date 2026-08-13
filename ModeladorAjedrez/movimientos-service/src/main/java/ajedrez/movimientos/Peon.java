package ajedrez.movimientos;

import java.util.ArrayList;
import java.util.List;

import ajedrez.tablero.Color;
import ajedrez.tablero.EstadoTablero;
import ajedrez.tablero.Pieza;
import ajedrez.tablero.Posicion;

/**
 * Regla de movimiento: Peon.
 * Se mueve 1 casilla hacia adelante (2 si es su primer movimiento).
 * Los blancos avanzan hacia arriba (fila menor), los negros hacia abajo.
 */
public class Peon extends Pieza implements CalculadorMovimientos {

    private boolean primerMovimiento = true;

    public Peon(Color color, Posicion posicion) {
        super(color, posicion);
    }

    @Override
    public List<Posicion> movimientosPosibles(EstadoTablero tablero) {
        List<Posicion> movimientos = new ArrayList<>();
        int direccion = (color == Color.BLANCO) ? -1 : 1;

        Posicion unPaso = new Posicion(posicion.getFila() + direccion, posicion.getColumna());
        if (unPaso.esValida() && tablero.estaVacia(unPaso)) {
            movimientos.add(unPaso);

            if (primerMovimiento) {
                Posicion dosPasos = new Posicion(posicion.getFila() + 2 * direccion, posicion.getColumna());
                if (dosPasos.esValida() && tablero.estaVacia(dosPasos)) {
                    movimientos.add(dosPasos);
                }
            }
        }

        // Capturas en diagonal
        int[] columnasCaptura = { posicion.getColumna() - 1, posicion.getColumna() + 1 };
        for (int col : columnasCaptura) {
            Posicion diagonal = new Posicion(posicion.getFila() + direccion, col);
            if (diagonal.esValida() && tablero.hayPiezaEnemigaEn(diagonal, color)) {
                movimientos.add(diagonal);
            }
        }

        return movimientos;
    }

    @Override
    public void moverA(Posicion nuevaPosicion) {
        super.moverA(nuevaPosicion);
        primerMovimiento = false;
    }

    @Override
    public String getSimbolo() {
        return "P";
    }
}
