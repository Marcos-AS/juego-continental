package src.modelo;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class jugadorActual extends Jugador implements Serializable {
	private int numeroJugador;
    private ArrayList<Carta> mano = new ArrayList<>();
    private int puntosPartida;
    private ArrayList<ArrayList<Carta>> juegos = new ArrayList<>();
	private int escalerasBajadas;
	private int triosBajados;
	private boolean puedeBajar = true;
	private boolean turno = false;

	public jugadorActual() {}

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
		int j = 0;
		for(int i = 0; i < cartasABajar.length/2; i++) {
			int numCarta = (int)cartasABajar[j];
			String paloCarta = cartasABajar[j+1].toString().toUpperCase();
			juego.add(getCartaDeLaMano(this.mano, numCarta, paloCarta));
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
			if (cI.getNumero() == numCarta && cI.getPalo().name().equals(paloCarta.toUpperCase())) {
				encontrada = true;
			} else {
				i++;
			}
		}
		return cI;
	}

	public void eliminarDeLaMano(Object[] cartasABajar) {
	 	int numCarta;
	 	String paloCarta;
		 for(int i = 0; i < cartasABajar.length; i+=2) {
	 		numCarta = (int)cartasABajar[i];
	 		paloCarta = cartasABajar[i+1].toString();
	 		this.mano.remove(getCartaDeLaMano(this.mano, numCarta, paloCarta));
	 	}
	 }
	

    public void moverCartaEnMano(int indCarta, int destino) {
		Carta c = this.mano.get(indCarta);
		this.mano.remove(indCarta);
		this.mano.add(destino, c);
	}

	//PUBLIC-----------------------------------------------------------------

	public boolean cortar(int ronda) throws RemoteException {
		boolean puedeCortar = false;
		if(ifJuego.comprobarPosibleCorte(ronda, this.triosBajados, this.escalerasBajadas)) {
			if (!this.getMano().isEmpty()) {
				Carta c = this.getCartaParaTirarAlPozo(0);
			}
			puedeCortar = true;
		}
		return puedeCortar;
	}

    public Carta getCartaParaTirarAlPozo(int indiceCarta) {
		Carta cartaATirar = this.mano.get(indiceCarta); 
		this.mano.remove(indiceCarta);
		return cartaATirar;
	}

    public void agregarCarta(Carta c) {
		this.mano.add(c);
	}
    
    public int bajarJuego(ArrayList<Carta> juego) throws RemoteException {
		return ifJuego.comprobarJuego(juego, getPartidaActual().getRonda()); //si tipoJuego es 2, no es juego
	}

	public ArrayList<Carta> getJuego(Object[] cartasABajar) {
		return seleccionarCartasABajar(cartasABajar);
	}

	public void addJuego(ArrayList<Carta> juego) {
		this.juegos.add(juego);
	}

	public void incrementarEscalerasBajadas() {
		this.escalerasBajadas++;
	}

	public void incrementarTriosBajados() {
		this.triosBajados++;
	}

	public int[] comprobarQueFaltaParaCortar(int ronda) {
		int trios = 0;
		int escaleras = 0;
		int[] faltante = new int[2];
		switch (ronda) {
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

	public boolean acomodarCartaJuegoPropio(int numCarta, int numJuego, int ronda) throws RemoteException {
		boolean acomodo = false;
		ArrayList<Carta> juegoElegido = this.juegos.get(numJuego);
		juegoElegido.add(mano.get(numCarta));
		if(ifJuego.comprobarJuego(juegoElegido, ronda) != 2) {
			acomodo = true;
			this.juegos.get(numJuego).add(mano.get(numCarta));
		}
		return acomodo;
	}

	public void addCarta(Carta c) {
		this.mano.add(c);
	}

    //SETTERS Y GETTERS-----------------------

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public int getNumeroJugador() {
		return this.numeroJugador;
	}

    public void setNumeroJugador(int num) {
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
	public boolean getPuedeBajar() { return this.puedeBajar;}

	public void setTriosBajados(int trios) {
		this.triosBajados = trios;
	}

	public void setEscalerasBajadas(int escaleras) {
		this.escalerasBajadas = escaleras;
	}

	public void setPuntos(int puntos) {
		this.puntosPartida = puntos;
	}

	public int getPuntos() {
		return this.puntosPartida;
	}

	public boolean getTurno() {
		return this.turno;
	}

	public void setTurno() {
		this.turno = !this.turno;
	}
}