package src.main;

import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.Consola;

public class App {
    public static void main(String[] args) {
        Partida partidaNueva = new Partida();
        Consola consola = new Consola();
        partidaNueva.agregarJugador("AnitaSSJ");
        partidaNueva.agregarJugador("Marcos");
        partidaNueva.crearMazo();
        partidaNueva.repartirCartas();
        for (jugadorActual j: partidaNueva.getJugadorActuales()) {
            consola.presentarCartasNombreJugador(j.getNombre());
            
        }
    }
}
