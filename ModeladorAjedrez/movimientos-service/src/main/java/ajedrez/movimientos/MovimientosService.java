package ajedrez.movimientos;

import java.util.List;

import ajedrez.tablero.EstadoTablero;
import ajedrez.tablero.Pieza;
import ajedrez.tablero.Posicion;

/**
 * Fachada publica de movimientos-service.
 *
 * Es el punto de entrada que partida-service usa para preguntar "¿a
 * donde se puede mover esta pieza?". Mapea a lo que seria:
 *
 *   calcularMovimientos(pieza, estado) -> POST /movimientos/calcular
 *                                          body: { pieza, estadoTablero }
 *
 * Recibe siempre una Pieza (definida en tablero-service) y un
 * EstadoTablero (la vista de solo lectura), nunca el Tablero mutable
 * completo — asi, aunque este modulo se ejecute en otro proceso el dia
 * de mañana, no podria mover piezas por su cuenta, solo calcular.
 */
public class MovimientosService {

    public List<Posicion> calcularMovimientos(Pieza pieza, EstadoTablero tablero) {
        return pieza.movimientosPosibles(tablero);
    }
}
