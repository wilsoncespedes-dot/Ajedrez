package ajedrez.tablero;

/**
 * Contrato de SOLO LECTURA sobre el estado del tablero.
 *
 * Este es el "contrato de consulta" que tablero-service expone hacia
 * afuera (a movimientos-service y partida-service). Es equivalente a lo
 * que seria la respuesta de un endpoint GET /tablero/estado si este
 * modulo se convirtiera en un servicio real con API HTTP: quien lo
 * recibe puede preguntar por el estado de una casilla, pero no puede
 * modificarlo directamente.
 *
 * Las operaciones que cambian el tablero (colocar y mover piezas) viven
 * en TableroService, no aqui — esa es la separacion entre "query" y
 * "command" que despues facilita exponer cada una como su propio
 * endpoint.
 */
public interface EstadoTablero {

    Pieza obtenerPieza(Posicion pos);

    boolean estaVacia(Posicion pos);

    boolean hayPiezaEnemigaEn(Posicion pos, Color colorPropio);
}
