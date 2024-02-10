package src.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable {
    protected String nombre;
    protected ArrayList<Partida> partidas = new ArrayList<>();
    private int puestoRanking;
    private int puntosTotales;
    private int puntosAlFinalizar;

    public Jugador() {}

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public void sumarPartida(Partida p) {
		this.partidas.add(p);
	}

    //SETTERS Y GETTERS----------------
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
}
