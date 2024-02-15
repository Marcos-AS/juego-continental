package src.vista;

import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifPartida;

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

    static ArrayList<String> cartasToStringArray(ArrayList<ifCarta> mano) {
        ArrayList<String> manoString = new ArrayList<>();
        for (ifCarta c : mano) {
            String numString = transformarNumCarta(c.getNumero());
            String carta;
            if (c.getPalo().toString().equalsIgnoreCase("COMODIN")) {
                carta = "COMODIN";
            } else {
                carta = numString + " de " + c.getPalo().toString();
            }
            manoString.add(carta);
        }
        return manoString;
    }

    void actualizar(Object actualizacion, int indice) throws RemoteException;

    void setControlador(Controlador ctrl);

    int menuBienvenida();

    void mostrarReglas();

    String preguntarNombreNuevoJugador() throws RemoteException;

    void mostrarInicioPartida();

    void mostrarJugador(String nombreJugador, int numJugador);

    static void mostrarCombinacionRequerida(int ronda) {

    }

    void mostrarPozo(ifPartida p);

    void mostrarTurnoJugador(String nombreJugador);

    void mostrarCartas(ArrayList<String> cartas);

    void mostrarPuntosJugador(String nombre, int punto);

    void mostrarGanador(String ganador);

    String getNombreVista();

    void setNombreVista(String i);

    void noSePuedeIniciarPartida(int i);
    void mostrarFinalizoPartida();
    int preguntarCantJugadores();
}