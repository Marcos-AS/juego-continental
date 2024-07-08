package src.modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifJugador {
    void sumarPartida(Partida p);

    void addCarta(Carta c);
    //SETTERS Y GETTERS----------------
    String getNombre();

    Partida getPartidaActiva();
    void setPuedeBajar(int valor);
    int getTriosBajados();
    int getEscalerasBajadas();

    int[] comprobarQueFaltaParaCortar(int ronda);

    Carta getCartaFromMano(int eleccion);

    ArrayList<ArrayList<Carta>> getJuegos();

    ArrayList<Carta> getMano();

    int getNumeroJugador();

    boolean acomodarCartaJuegoPropio(int numCarta, int numJuego, int ronda) throws RemoteException;
    void addJuego(int[] juego);
    void eliminarDeLaMano(ArrayList<Carta> cartasABajar);
    void incrementarPuedeBajar();
    int getPuedeBajar();
    void incrementarEscalerasBajadas();
    void incrementarTriosBajados();
    void moverCartaEnMano(int indCarta, int destino);
    void setPuntosAlFinalizar(int puntos);
    int getPuntosAlFinalizar();
    boolean isTurnoActual();
    void setTurnoActual(boolean valor);
    boolean isManoEmpty();
    boolean getRoboConCastigo();
    void setRoboConCastigo(boolean valor);
    ArrayList<Carta> seleccionarCartasABajar(int[] cartasABajar);
}
