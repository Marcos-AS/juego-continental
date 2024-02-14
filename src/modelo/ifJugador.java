package src.modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifJugador {
    void sumarPartida(Partida p);

    void addCarta(Carta c);
    //SETTERS Y GETTERS----------------
    String getNombre();

    Partida getPartidaActiva();

    void eleccionOrdenar(int[] ordenar);

    boolean cortar() throws RemoteException;

    int[] comprobarQueFaltaParaCortar();

    Carta getCartaParaTirarAlPozo(int eleccion);

    ArrayList<ArrayList<Carta>> getJuegos();

    ArrayList<Carta> getMano();

    int getNumeroJugador();

    void robarConCastigo();
}
