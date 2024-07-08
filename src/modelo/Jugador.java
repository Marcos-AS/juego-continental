package src.modelo;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Jugador implements Serializable {
    protected String nombre;

    protected ArrayList<Partida> partidas = new ArrayList<>();
    private int puntosAlFinalizar;

    public Jugador() {}

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public void sumarPartida(Partida p) {
		this.partidas.add(p);
	}


    public String getNombre() {
        return this.nombre;
    }

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

}
