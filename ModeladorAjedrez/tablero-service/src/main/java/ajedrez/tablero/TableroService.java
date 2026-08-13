package ajedrez.tablero;

/**
 * Fachada publica de tablero-service.
 *
 * Es el UNICO punto de entrada que deberian usar otros modulos
 * (movimientos-service, partida-service) para interactuar con el
 * tablero. Hoy es una clase Java normal, pero cada metodo aqui mapea
 * 1 a 1 a lo que seria un endpoint si este modulo se desplegara como
 * un servicio independiente, por ejemplo:
 *
 *   colocarPieza(pieza)                 -> POST /tablero/piezas
 *   moverPieza(origen, destino)         -> POST /tablero/mover
 *   obtenerPieza(pos)                   -> GET  /tablero/casillas/{pos}
 *   estaVacia(pos)                      -> GET  /tablero/casillas/{pos}/vacia
 *   hayPiezaEnemigaEn(pos, color)       -> GET  /tablero/casillas/{pos}/enemiga?color=...
 *   vista()                             -> GET  /tablero/estado
 *   imprimirTablero()                   -> GET  /tablero/render (texto plano)
 *
 * Mantener esta fachada delgada (sin logica de reglas de ajedrez) es lo
 * que permite que movimientos-service y partida-service dependan solo
 * de ella y de EstadoTablero, y nunca de los detalles internos de
 * Tablero.
 */
public class TableroService {

    private final Tablero tablero;

    public TableroService() {
        this.tablero = new Tablero();
    }

    public void colocarPieza(Pieza pieza) {
        tablero.colocarPieza(pieza);
    }

    public void moverPieza(Posicion origen, Posicion destino) {
        tablero.moverPieza(origen, destino);
    }

    public Pieza obtenerPieza(Posicion pos) {
        return tablero.obtenerPieza(pos);
    }

    public boolean estaVacia(Posicion pos) {
        return tablero.estaVacia(pos);
    }

    public boolean hayPiezaEnemigaEn(Posicion pos, Color colorPropio) {
        return tablero.hayPiezaEnemigaEn(pos, colorPropio);
    }

    /**
     * Vista de solo lectura del tablero, pensada para pasarsela a
     * movimientos-service sin darle acceso a las operaciones de mutacion.
     */
    public EstadoTablero vista() {
        return tablero;
    }

    public void imprimirTablero() {
        tablero.imprimirTablero();
    }
}
