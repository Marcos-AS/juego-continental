package src.main;

import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.Consola;

public class App {
    public static void main(String[] args) {
        Partida partidaNueva = new Partida();
        Consola consola = new Consola();
        String j1 = "AnitaSSJ";
        String j2 = "Marcos";
        partidaNueva.agregarJugador(j1);
        partidaNueva.agregarJugador(j2);
        partidaNueva.crearMazo();
        partidaNueva.repartirCartas();
    
        consola.mostrarCartasJugador(j1);
        consola.mostrarCartasJugador(j2);
    }
}
