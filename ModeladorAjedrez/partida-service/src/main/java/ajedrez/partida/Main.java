package ajedrez.partida;

import java.util.List;

import ajedrez.movimientos.Caballo;
import ajedrez.movimientos.Peon;
import ajedrez.movimientos.Reina;
import ajedrez.movimientos.Rey;
import ajedrez.movimientos.Torre;
import ajedrez.tablero.Color;
import ajedrez.tablero.Pieza;
import ajedrez.tablero.Posicion;

/**
 * Punto de entrada para probar el modelador ya reorganizado en
 * microservicios logicos. Aqui se ve como partida-service orquesta a
 * tablero-service (estado) y movimientos-service (reglas) a traves de
 * sus fachadas publicas, sin tocar las clases internas de ninguno.
 */
public class Main {

    public static void main(String[] args) {
        PartidaService partida = new PartidaService();

        // Colocamos algunas piezas blancas
        partida.colocarPieza(new Torre(Color.BLANCO, new Posicion(7, 0)));
        partida.colocarPieza(new Caballo(Color.BLANCO, new Posicion(7, 1)));
        partida.colocarPieza(new Rey(Color.BLANCO, new Posicion(7, 4)));
        partida.colocarPieza(new Reina(Color.BLANCO, new Posicion(7, 3)));
        partida.colocarPieza(new Peon(Color.BLANCO, new Posicion(6, 4)));

        // Y algunas piezas negras
        partida.colocarPieza(new Torre(Color.NEGRO, new Posicion(0, 0)));
        partida.colocarPieza(new Rey(Color.NEGRO, new Posicion(0, 4)));
        partida.colocarPieza(new Peon(Color.NEGRO, new Posicion(1, 4)));

        System.out.println("Tablero inicial:");
        partida.imprimirTablero();

        // Probamos los movimientos posibles de la Reina blanca
        Pieza reina = partida.obtenerPieza(new Posicion(7, 3));
        List<Posicion> movimientos = partida.movimientosDisponibles(new Posicion(7, 3));

        System.out.println("\nLa Reina blanca en " + reina.getPosicion() + " puede moverse a:");
        for (Posicion p : movimientos) {
            System.out.print(p + " ");
        }

        // Movemos el peon blanco dos casillas hacia adelante
        System.out.println("\n\nMovemos el peon blanco de e2 a e4...");
        partida.mover(new Posicion(6, 4), new Posicion(4, 4));
        partida.imprimirTablero();

        System.out.println("\nTurno actual: " + partida.turnoActual());
        System.out.println("Historial: " + partida.historial());
    }
}
