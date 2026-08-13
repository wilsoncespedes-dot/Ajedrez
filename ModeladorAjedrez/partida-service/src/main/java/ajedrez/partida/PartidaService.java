package ajedrez.partida;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import ajedrez.movimientos.MovimientosService;
import ajedrez.tablero.Color;
import ajedrez.tablero.Pieza;
import ajedrez.tablero.Posicion;
import ajedrez.tablero.TableroService;

/**
 * Fachada publica de partida-service. Es el orquestador: el unico modulo
 * que conoce a la vez a tablero-service y a movimientos-service, y el
 * unico que sabe de turnos, historial de jugadas y piezas capturadas.
 *
 * En una arquitectura de microservicios real, esta clase es la que mas
 * naturalmente se convierte en el servicio "de negocio" detras de un
 * API Gateway, por ejemplo:
 *
 *   colocarPieza(pieza)               -> POST /partida/piezas
 *   movimientosDisponibles(pos)       -> GET  /partida/movimientos/{pos}
 *   mover(origen, destino)            -> POST /partida/mover
 *   historial()                       -> GET  /partida/historial
 *   piezasCapturadas(color)           -> GET  /partida/capturadas/{color}
 *   turnoActual()                     -> GET  /partida/turno
 *
 * Nota: no valida si el movimiento pedido esta dentro de la lista de
 * movimientosDisponibles antes de ejecutarlo (igual que el modelo
 * original no lo hacia en Main.java) — sigue siendo un modelador de
 * movimientos, no un motor de ajedrez completo con validacion estricta.
 */
public class PartidaService {

    private final TableroService tableroService;
    private final MovimientosService movimientosService;

    private Color turnoActual;
    private final List<Jugada> historial;
    private final Map<Color, List<Pieza>> capturadas;

    public PartidaService() {
        this.tableroService = new TableroService();
        this.movimientosService = new MovimientosService();
        this.turnoActual = Color.BLANCO;
        this.historial = new ArrayList<>();
        this.capturadas = new EnumMap<>(Color.class);
        this.capturadas.put(Color.BLANCO, new ArrayList<>());
        this.capturadas.put(Color.NEGRO, new ArrayList<>());
    }

    public void colocarPieza(Pieza pieza) {
        tableroService.colocarPieza(pieza);
    }

    public Pieza obtenerPieza(Posicion pos) {
        return tableroService.obtenerPieza(pos);
    }

    public List<Posicion> movimientosDisponibles(Posicion pos) {
        Pieza pieza = tableroService.obtenerPieza(pos);
        if (pieza == null) return new ArrayList<>();
        return movimientosService.calcularMovimientos(pieza, tableroService.vista());
    }

    /**
     * Ejecuta un movimiento, registra si hubo captura y pasa el turno.
     */
    public Jugada mover(Posicion origen, Posicion destino) {
        Pieza piezaMovida = tableroService.obtenerPieza(origen);
        Pieza piezaCapturada = tableroService.obtenerPieza(destino);

        tableroService.moverPieza(origen, destino);

        if (piezaCapturada != null) {
            capturadas.get(piezaCapturada.getColor()).add(piezaCapturada);
        }

        Jugada jugada = new Jugada(piezaMovida, origen, destino, piezaCapturada);
        historial.add(jugada);

        if (piezaMovida != null) {
            turnoActual = turnoActual.contrario();
        }

        return jugada;
    }

    public Color turnoActual() {
        return turnoActual;
    }

    public List<Jugada> historial() {
        return historial;
    }

    public List<Pieza> piezasCapturadas(Color color) {
        return capturadas.get(color);
    }

    public void imprimirTablero() {
        tableroService.imprimirTablero();
    }
}
