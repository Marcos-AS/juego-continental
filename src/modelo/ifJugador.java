package src.modelo;

public interface ifJugador {
    void sumarPartida(Partida p);

    //SETTERS Y GETTERS----------------
    String getNombre();

    Partida getPartidaActiva();
}
