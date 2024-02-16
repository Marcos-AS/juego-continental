package src.modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifJugador {
    void sumarPartida(Partida p);

    void addCarta(Carta c);
    //SETTERS Y GETTERS----------------
    String getNombre();

    Partida getPartidaActiva();

    boolean cortar(int ronda) throws RemoteException;

    int[] comprobarQueFaltaParaCortar(int ronda);

    Carta getCartaParaTirarAlPozo(int eleccion);

    ArrayList<ArrayList<Carta>> getJuegos();

    ArrayList<Carta> getMano();

    int getNumeroJugador();

    void robarConCastigo();

    boolean acomodarCartaJuegoPropio(int numCarta, int numJuego, int ronda) throws RemoteException;
    int bajarJuego(ArrayList<Carta> juego) throws RemoteException;
    ArrayList<Carta> getJuego(Object[] cartasABajar);
    void addJuego(ArrayList<Carta> juego);
    void eliminarDeLaMano(Object[] cartasABajar);
    void setPuedeBajar();
    boolean getPuedeBajar();
    void incrementarEscalerasBajadas();
    void incrementarTriosBajados();
    void moverCartaEnMano(int indCarta, int destino);
    void setPuntosAlFinalizar(int puntos);
    int getPuntosAlFinalizar();
}
