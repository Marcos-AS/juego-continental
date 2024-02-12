package src.modelo;

import java.rmi.RemoteException;

public interface ifJugador {
    void sumarPartida(Partida p);

    boolean eleccionMenuRobo(int eleccion);

    //SETTERS Y GETTERS----------------
    String getNombre();

    Partida getPartidaActiva();

    void eleccionOrdenar(int[] ordenar);

    boolean cortar() throws RemoteException;

    int[] comprobarQueFaltaParaCortar();

    void tirarAlPozo(int eleccion);
}
