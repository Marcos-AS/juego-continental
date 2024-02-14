package src.modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifPartida {

    int determinarNumBarajas();

    void iniciarMazo();

    void mezclarCartas();

    void iniciarPozo();

    Carta sacarPrimeraDelPozo();

    void agregarJugador(String nombre) throws RemoteException;

    Carta eliminarDelMazo();

    Carta eliminarDelPozo();

    void agregarAlPozo(Carta c);

    void crearMazo() throws RemoteException;

    void repartirCartas() throws RemoteException;

    void resetearJuegosJugadores();

    void sumarPuntos() throws RemoteException;

    String determinarGanador();

    int[] getPuntosJugadores();

    //SETTERS Y GETTERS------------
    int getNumJugadores();

    ArrayList<jugadorActual> getJugadoresActuales();

    int getRonda();

    jugadorActual getJugador(String nombreJugador);

    int getTotalRondas();

    ArrayList<Carta> getMazo();

    ArrayList<Carta> getPozo();

    boolean getEstadoPartida();

    void setEstadoPartida();

    void partidaIniciada() throws RemoteException;
}
