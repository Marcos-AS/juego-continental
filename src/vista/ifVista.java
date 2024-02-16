package src.vista;

import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifPartida;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifVista {
    int ELECCION_BAJARSE = 1;
    int ELECCION_NO_BAJARSE = 2;
    int ELECCION_ORDENAR_CARTAS = 3;
    int ELECCION_CORTAR = 4;
    int ELECCION_ACOMODAR_JUEGO_PROPIO = 5;
    int ELECCION_ROBAR_DEL_MAZO = 1;
    int ELECCION_ROBAR_DEL_POZO = 2;

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

    void mostrarPozo(ifCarta c);

    void mostrarTurnoJugador(String nombreJugador);

    void mostrarCartas(ArrayList<String> cartas);

    void mostrarPuntosJugador(String nombre, int punto);

    void mostrarGanador(String ganador);

    String getNombreVista();

    void setNombreVista(String i);

    void noSePuedeIniciarPartida(int i);
    void mostrarFinalizoPartida();
    int preguntarCantJugadores();
    void mostrarJuegos(ArrayList<ArrayList<String>> juegos);
    void mostrarNoPuedeBajarJuego();
    void mostrarNoPuedeAcomodarJuegoPropio();
    void mostrarLoQueFaltaParaCortar(int[] faltaParaCortar);
    int menuBajar();
    int preguntarQueBajarParaPozo(int cantCartas);
    static int getEleccionBajarse() {
        return ELECCION_BAJARSE;
    }
    static int getEleccionOrdenarCartas() {
        return ELECCION_ORDENAR_CARTAS;
    }
    static int getEleccionNoBajarse() {
        return ELECCION_NO_BAJARSE;
    }
    static int getEleccionCortar() {
        return ELECCION_CORTAR;
    }
    static int getEleccionAcomodarJuegoPropio() {
        return ELECCION_ACOMODAR_JUEGO_PROPIO;
    }
    static int getEleccionRobarDelMazo() {
        return ELECCION_ROBAR_DEL_MAZO;
    }
    static int getEleccionRobarDelPozo() {
        return ELECCION_ROBAR_DEL_POZO;
    }
    void mostrarNoPuedeRobarDelPozo();
    int menuRobar();
    int preguntarCartaParaAcomodar();
    int preguntarEnQueJuegoQuiereAcomodar();
    Object[] preguntarQueBajarParaJuego();
    void jugadorPuedeRobarConCastigo(String nombreJugador);
    int menuRobarDelPozo();
    void nuevaVentana();
    int[] preguntarParaOrdenarCartas();
}