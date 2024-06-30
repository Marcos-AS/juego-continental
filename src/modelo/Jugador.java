package src.modelo;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Jugador implements Serializable, ifJugador {
    protected String nombre;
    protected ArrayList<Partida> partidas = new ArrayList<>();
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

    public void setPuntosAlFinalizar(int puntos) {
        this.puntosAlFinalizar = puntos;
    }

    public int getPuntosAlFinalizar() {
        return puntosAlFinalizar;
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
    public Carta getCartaFromMano(int eleccion) {
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
    public ArrayList<Carta> getJuego(int[] cartasABajar) {
        return null;
    }

    @Override
    public void addJuego(int[] juego) {

    }

    @Override
    public void eliminarDeLaMano(ArrayList<Carta> cartasABajar) {

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

    @Override
    public void moverCartaEnMano(int indCarta, int destino) {

    }

    @Override
    public int juegoValido(int[] cartasABajar) throws RemoteException {
        return -1;
    }

    @Override
    public boolean isTurnoActual() {
        return false;
    }

    @Override
    public void setTurnoActual(boolean valor) {
    }

    @Override
    public boolean isManoEmpty() {
        return false;
    }
}
