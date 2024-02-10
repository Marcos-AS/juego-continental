package src.modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifPartida {
    void setValorAccion1(Integer valor);

    int determinarNumBarajas();

    void iniciarMazo();

    void mezclarCartas();

    void iniciarPozo();

    Carta sacarPrimeraDelPozo();

    Carta sacarPrimeraDelMazo();

    void agregarJugador(String nombre) throws RemoteException;

    Carta eliminarDelMazo();

    Carta eliminarDelPozo();

    void agregarAlPozo(Carta c);

    void crearMazo();

    void repartirCartas();

    void incrementarRonda();

    void resetearJuegosJugadores();

    void sumarPuntos();

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
}
