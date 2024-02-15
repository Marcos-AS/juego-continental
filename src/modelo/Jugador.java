package src.modelo;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Jugador implements Serializable, ifJugador {
    protected String nombre;
    protected ArrayList<Partida> partidas = new ArrayList<>();
    private int puestoRanking;
    private int puntosTotales;
    private int puntosAlFinalizar;

    public Jugador() {}

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void sumarPartida(Partida p) {
		this.partidas.add(p);
	}

    @Override
    public void addCarta(Carta c) {

    }

    //SETTERS Y GETTERS----------------
    @Override
    public String getNombre() {
        return this.nombre;
    }

    @Override
    public Partida getPartidaActiva() {
        boolean encontrado = false;
        int i = 0;
        while (!encontrado && i < this.partidas.size()) {
            if (this.partidas.get(i).getEstadoPartida()) {
                encontrado = true;
            } else {
                i++;
            }
        }
        return this.partidas.get(i);
    }

    @Override
    public void eleccionOrdenar(int[] ordenar) {

    }

    @Override
    public boolean cortar(int ronda) throws RemoteException {
        return false;
    }

    @Override

    public int[] comprobarQueFaltaParaCortar(int ronda) {
        return new int[0];
    }

    @Override
    public Carta getCartaParaTirarAlPozo(int eleccion) {
    return null;
    }

    @Override
    public ArrayList<ArrayList<Carta>> getJuegos() {
        return null;
    }

    @Override
    public ArrayList<Carta> getMano() {
        return null;
    }

    @Override
    public int getNumeroJugador() {
        return 0;
    }

    @Override
    public void robarConCastigo() {

    }

    @Override
    public boolean acomodarCartaJuegoPropio(int numCarta, int numJuego, int ronda) throws RemoteException {
        return false;
    }

    @Override
    public int bajarJuego(ArrayList<Carta> juego) throws RemoteException {
        return 0;
    }

    @Override
    public ArrayList<Carta> getJuego(Object[] cartasABajar) {
        return null;
    }

    @Override
    public void addJuego(ArrayList<Carta> juego) {

    }

    @Override
    public void eliminarDeLaMano(Object[] cartasABajar) {

    }

    @Override
    public void setPuedeBajar() {

    }

    @Override
    public boolean getPuedeBajar() {
        return false;
    }

    @Override
    public void incrementarEscalerasBajadas() {

    }

    @Override
    public void incrementarTriosBajados() {

    }
}
