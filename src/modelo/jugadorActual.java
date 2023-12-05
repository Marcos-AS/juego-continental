package src.modelo;

import java.util.ArrayList;

public class jugadorActual extends Jugador {
    private int numeroJugador;
    private ArrayList<Carta> mano = new ArrayList<>();
    private int puntosPartida;
    private ArrayList<ArrayList<Carta>> juegos;
    
    //PRIVATE -------------------------------------------------------------
    private Partida getPartidaActual() {
        Partida partActual = null;
        if (!this.partidas.isEmpty()) {
            partActual = this.partidas.get(this.partidas.size()-1);
        }
        return partActual;
    }

	private ArrayList<Carta> seleccionarCartasABajar(int[] indicesCartasABajar) {
		ArrayList<Carta> juego = new ArrayList<>();
		for(int i = 0; i < indicesCartasABajar.length; i++) {
			juego.add(this.mano.get(indicesCartasABajar[i]));
		}
		return juego;
	}

	private void eliminarDeLaMano(int[] indicesCartasABajar) {
		for(int i = 0; i < indicesCartasABajar.length; i++) {
			this.mano.remove(this.mano.get(indicesCartasABajar[i]));
		}
	}

    //PUBLIC-----------------------------------------------------------------
    public void robarDelMazo() {
		Carta c = getPartidaActual().eliminarDelMazo();
		this.mano.add(c);
	}
	
	public void robarDelPozo() {
		Carta c = getPartidaActual().eliminarDelPozo();
		this.mano.add(c);
	}

    public void tirarAlPozo(int indiceCarta) {
		Carta cartaATirar = this.mano.get(indiceCarta); 
		this.mano.remove(indiceCarta);
		getPartidaActual().agregarAlPozo(cartaATirar);
	}

    public void agregarCarta(Carta c) {
		this.mano.add(c);
	}
   
    public void moverCartaEnMano(int indCarta, int destino) {
		Carta c = this.mano.get(indCarta);
		this.mano.remove(indCarta);
		this.mano.add(destino, c);
	}
    
    public boolean bajarJuego(int[] indicesCartasABajar) {
		boolean puedeBajar = false;
		ArrayList<Carta> juego = seleccionarCartasABajar(indicesCartasABajar);
		if(Juego.comprobarJuego(juego, getPartidaActual().getRonda())) {
			puedeBajar = true;
			this.juegos.add(juego);
			this.eliminarDeLaMano(indicesCartasABajar);
		}
		return puedeBajar;
	}
    //SETTERS Y GETTERS-----------------------
    public jugadorActual() {}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNumJugador(int num) {
        this.numeroJugador = num;
    }
    
    public ArrayList<Carta> getMano() {
        return this.mano;
    }
}
