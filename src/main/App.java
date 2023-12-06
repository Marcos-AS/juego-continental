package src.main;

import src.modelo.Partida;
import src.vista.Consola;
import src.controlador.Controlador;

public class App {
    private static Controlador ctrl;
    public static void main(String[] args) {
        ctrl = new Controlador();
        Partida partidaNueva = new Partida();
        Consola consola = new Consola();
        String j1 = "AnitaSSJ";
        String j2 = "Marcos";
        partidaNueva.agregarJugador(j1);
        partidaNueva.agregarJugador(j2);
        partidaNueva.crearMazo();
        partidaNueva.repartirCartas();
        consola.mostrarCartasNombreJugador(j1);
        consola.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j1));
        consola.mostrarCartasNombreJugador(j2);
        consola.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j2));
    }
}
