package src.vista;

import src.controlador.Controlador;
import src.modelo.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifVista {

    static String transformarNumCarta(int numCarta) {
        String num = ((Integer) numCarta).toString();
        if (numCarta <= 1 || numCarta >= 11) {
            switch (num) {
                case "1":
                    num = "A";
                    break;
                case "11":
                    num = "J";
                    break;
                case "12":
                    num = "Q";
                    break;
                case "13":
                    num = "K";
                    break;
                case "-1":
                    num = "COMODIN";
                    break;
            }
        }
        return num;
    }

    static ArrayList<String> cartasToStringArray(ArrayList<Carta> mano) {
        ArrayList<String> manoString = new ArrayList<>();
        for (Carta c : mano) {
            String numString = transformarNumCarta(c.getNumero());
            Palo palo = c.getPalo();
            String carta;
            if (palo == Palo.COMODIN) {
                carta = "COMODIN";
            } else {
                carta = numString + " de " + palo;
            }
            manoString.add(carta);
        }
        return manoString;
    }

    void actualizar(Object actualizacion, int indice) throws RemoteException;

    void setControlador(Controlador ctrl);

    int menuBienvenida();

    void mostrarReglas();

    void preguntarNombreNuevoJugador() throws RemoteException;

    void mostrarInicioPartida();

    void mostrarJugador(String nombreJugador, int numJugador);

    static void mostrarCombinacionRequerida(int ronda) {

    }

    void mostrarPozo(ifPartida p);

    void mostrarTurnoJugador(String nombreJugador);

    void mostrarCartas(ArrayList<String> cartas);

    void mostrarPuntosJugador(String nombre, int punto);

    void mostrarGanador(String ganador);

    void setNumVista(int i);
}