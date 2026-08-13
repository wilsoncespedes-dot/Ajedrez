package ajedrez.movimientos;

import java.util.List;

import ajedrez.tablero.EstadoTablero;
import ajedrez.tablero.Posicion;

/**
 * Contrato que cumple toda pieza de movimientos-service: dada la
 * posicion en la que esta parada (implicita en la propia pieza) y una
 * vista de solo lectura del tablero, devuelve a que casillas se puede
 * mover.
 *
 * Pieza (en tablero-service) ya declara este mismo metodo de forma
 * abstracta; esta interfaz existe ademas para que MovimientosService
 * (la fachada del modulo) pueda hablar en terminos de "algo que calcula
 * movimientos" sin acoplarse a la jerarquia de herencia de Pieza.
 */
public interface CalculadorMovimientos {
    List<Posicion> movimientosPosibles(EstadoTablero tablero);
}
