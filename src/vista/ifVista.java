package src.vista;

import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifJugador;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifVista {
    int ELECCION_BAJARSE = 1;
    int ELECCION_TIRAR_AL_POZO = 2;
    int ELECCION_ORDENAR_CARTAS = 3;
    int ELECCION_ACOMODAR_JUEGO_PROPIO = 4;
    int ELECCION_VER_JUEGOS_BAJADOS = 5;
    int ELECCION_ACOMODAR_JUEGO_AJENO = 6;
    int ELECCION_VER_JUEGOS_BAJADOS_MESA = 7;
    int ELECCION_VER_POZO = 8;
    int ELECCION_ROBAR_DEL_MAZO = 1;
    int ELECCION_ROBAR_DEL_POZO = 2;
    int NUEVA_PARTIDA = 6;
    int NUEVO_JUGADOR = 7;
    int DESARROLLO_TURNO = 8;
    int GANADOR = 10;
    int ROBO_CASTIGO = 11;
    int HUBO_ROBO_CASTIGO = 12;
    int RONDA_FINALIZADA = 14;
    int PUNTOS_RONDA = 15;
    int JUGADOR_INICIO_PARTIDA = 17;
    int ROBO = 18;
    int COMIENZA_PARTIDA = 19;
    int COMIENZA_RONDA = 20;
    int CORTE_RONDA = 21;
    int NUEVA_VENTANA = 23;
    int YA_NO_PUEDE_BAJAR = 1;
    int PARTIDA_AUN_NO_CREADA = 0;
    int FALTAN_JUGADORES = 1;
    int INICIAR_PARTIDA = 2;

    static String transformarNumCarta(int numCarta) {
        String num = ((Integer) numCarta).toString();
        if (numCarta <= 1 || numCarta >= 11) {
            num = switch (num) {
                case "1" -> "A";
                case "11" -> "J";
                case "12" -> "Q";
                case "13" -> "K";
                case "-1" -> "COMODIN";
                default -> num;
            };
        }
        return num;
    }

    static ArrayList<String> cartasToStringArray(ArrayList<ifCarta> mano) {
        ArrayList<String> manoString = new ArrayList<>();
        for (ifCarta c : mano)
            manoString.add(cartaToString(c));
        return manoString;
    }

    static String cartaToString(ifCarta c) {
        String carta;
        if (c.getPalo().toString().equalsIgnoreCase("COMODIN")) {
            carta = "COMODIN";
        } else {
            String numString = transformarNumCarta(c.getNumero());
            carta = numString + " de " + c.getPalo().toString();
        }
        return carta;
    }

    void actualizar(Object actualizacion, int indice) throws RemoteException;

    void setControlador(Controlador ctrl);

    int menuBienvenida();

    void mostrarReglas();

    String preguntarNombreNuevoJugador() throws RemoteException;

    void mostrarInicioPartida();

    void mostrarCombinacionRequerida(int ronda);

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
    void mostrarNoPuedeBajarJuego(int i);
    void mostrarNoPuedeAcomodarJuegoPropio();
    void mostrarLoQueFaltaParaCortar(int[] faltaParaCortar);
    void mostrarAcomodoCarta();
    void mostrarJuegosJugador(int numJugador);
    int menuBajar();
    int preguntarQueBajarParaPozo(int cantCartas);
    void mostrarNoPuedeRobarDelPozo();
    int menuRobar();
    int preguntarCartaParaAcomodar();
    int preguntarEnQueJuegoQuiereAcomodar();
    int[] preguntarQueBajarParaJuego(int cantCartas);
    void mostrarPuedeRobarConCastigo(String nombreJugador);
    void mostrarAdvertenciaBajarse();
    void mostrarFinalizoTurno();
    void mostrarCorto(String nombreJugador);
    boolean preguntarSiQuiereSeguirBajandoJuegos();
    void nuevaVentana();
    int[] preguntarParaOrdenarCartas(int cantCartas);
    int preguntarCantParaBajar();
    int preguntarEnLosJuegosDeQueJugadorAcomodar();
    boolean partida() throws RemoteException;
    void mostrarDebeCortar();
    void mostrarDebeQuedarle1o0Cartas();
    void mostrarTurnoPropio();
    boolean preguntarSiQuiereRobarCastigo();
    void mostrarComienzaPartida(ArrayList<ifJugador> jugadores);
    void mostrarComienzoRonda(int ronda);
    void mostrarCortoPropio();
    int menuRobarConCastigo();
}