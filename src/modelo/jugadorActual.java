package src.modelo;

import java.util.ArrayList;

public class jugadorActual extends Jugador {
    private int numeroJugador;
    private ArrayList<Carta> mano = new ArrayList<>();
    private int puntosPartida;
    private ArrayList<ArrayList<Carta>> juegos = new ArrayList<>();
	private int escalerasBajadas;
	private int triosBajados;
	private boolean puedeBajar = true;
    
    //PRIVATE -------------------------------------------------------------
    private Partida getPartidaActual() {
        Partida partActual = null;
        if (!this.partidas.isEmpty()) {
            partActual = this.partidas.get(this.partidas.size()-1);
        }
        return partActual;
    }

	private ArrayList<Carta> seleccionarCartasABajar(Object[] cartasABajar) {
		ArrayList<Carta> juego = new ArrayList<>();
		int numCarta;
		String paloCarta;
		int j = 0;
		for(int i = 0; i < cartasABajar.length/2; i++) {
			numCarta = (int)cartasABajar[j];
			paloCarta = cartasABajar[j+1].toString().toUpperCase();
			juego.add(getCartaDeLaMano(this.mano, numCarta,paloCarta));
			j += 2;
		}
		return juego;
	}

	private Carta getCartaDeLaMano(ArrayList<Carta> mano, int numCarta, String paloCarta) {
		Carta cI = null;
		boolean encontrada = false;
		int i = 0;
		while (!encontrada && i < mano.size()) {
			cI = mano.get(i);
			if (cI.getNumero() == numCarta && cI.getPalo().name().equals(paloCarta)) {
				encontrada = true;
			} else {
				i++;
			}
		}
		return cI;
	}

	private void eliminarDeLaMano(Object[] cartasABajar) {
		int numCarta;
		String paloCarta;
		for(int i = 0; i < cartasABajar.length; i+=2) {
			numCarta = (int)cartasABajar[i];
			paloCarta = cartasABajar[i+1].toString();
			this.mano.remove(getCartaDeLaMano(this.mano, numCarta, paloCarta));
		}
	}

    private void moverCartaEnMano(int indCarta, int destino) {
		Carta c = this.mano.get(indCarta);
		this.mano.remove(indCarta);
		this.mano.add(destino, c);
	}

	//PUBLIC-----------------------------------------------------------------

	public void eleccionMenuRobo(int eleccion) {
		switch(eleccion) {
		case 1:
			this.robarDelMazo();
			break;
		case 2:
			this.robarDelPozo();
			break;
		}
	}

	public void eleccionOrdenar(int[] elecciones) {
		this.moverCartaEnMano(elecciones[0], elecciones[1]);
	}

    public void robarDelMazo() {
		Carta c = getPartidaActual().eliminarDelMazo();
		this.mano.add(c);
	}
	
	public void robarDelPozo() {
		Carta c = getPartidaActual().eliminarDelPozo();
		this.mano.add(c);
	}

	public boolean comprobarCorte() {
		boolean puedeCortar = false;
		puedeCortar = Juego.comprobarPosibleCorte(getPartidaActual().getRonda(), this.triosBajados, this.escalerasBajadas);
		return puedeCortar;
	}

    public void tirarAlPozo(int indiceCarta) {
		Carta cartaATirar = this.mano.get(indiceCarta); 
		this.mano.remove(indiceCarta);
		getPartidaActual().agregarAlPozo(cartaATirar);
	}

    public void agregarCarta(Carta c) {
		this.mano.add(c);
	}
    
    public boolean bajarJuego(Object[] cartasABajar) {
		boolean puedeBajar = false;
		ArrayList<Carta> juego = seleccionarCartasABajar(cartasABajar);
		int tipoJuego = Juego.comprobarJuego(juego, getPartidaActual().getRonda()); //si tipoJuego es 2, no es juego
		if(tipoJuego < 2) {
			puedeBajar = true;
			this.juegos.add(juego);
			this.eliminarDeLaMano(cartasABajar);
			if (tipoJuego == 0) {
				this.triosBajados++;
			} else if(tipoJuego == 1) {
				this.escalerasBajadas++;
			}
		}
		return puedeBajar;
	}

	public void incrementarEscalerasBajadas() {
		this.escalerasBajadas++;
	}

	public void incrementarTriosBajadas() {
		this.triosBajados++;
	}

	public int[] comprobarQueFaltaParaCortar() {
		int trios = 0;
		int escaleras = 0;
		int[] faltante = new int[2];
		switch (getPartidaActual().getRonda()) {
			case 1:
				trios = 2 - this.triosBajados;
				break;
			case 2:
				trios = 1 - this.triosBajados;
				escaleras = 1 - this.escalerasBajadas;
				break;
			case 3:
				escaleras = 2 - this.escalerasBajadas;
				break;
			case 4:
				trios = 3 - this.triosBajados;
				break;
			case 5:
				trios = 2 - this.triosBajados;
				escaleras = 1 - this.escalerasBajadas;
				break;
			case 6:
				trios = 1 - this.triosBajados;
				escaleras = 2 - this.escalerasBajadas;
				break;
			case 7:
				escaleras = 3 - this.escalerasBajadas;
				break;
		}
		faltante[0] = trios;
		faltante[1] = escaleras;
		return faltante;
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

	public ArrayList<ArrayList<Carta>> getJuegos() {
		return this.juegos;
	}

	public void setPuedeBajar() {
		this.puedeBajar = !this.puedeBajar;
	}

	public void setTriosBajados(int trios) {
		this.triosBajados = trios;
	}

	public void setEscalerasBajadas(int escaleras) {
		this.escalerasBajadas = escaleras;
	}
}
