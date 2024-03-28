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
	private boolean turnoActual = false;

	public jugadorActual() {}

    //PRIVATE -------------------------------------------------------------
    private Partida getPartidaActual() {
        Partida partActual = null;
        if (!partidas.isEmpty()) {
            partActual = partidas.get(partidas.size()-1);
        }
        return partActual;
    }

	private ArrayList<Carta> seleccionarCartasABajar(int[] cartasABajar) {
		ArrayList<Carta> juego = new ArrayList<>();
		for(int i = 0; i < cartasABajar.length; i++)
			juego.add(mano.get(cartasABajar[i]));
		return juego;
	}

	public void eliminarDeLaMano(ArrayList<Carta> cartasABajar) {
		 for(Carta c : cartasABajar) {
	 		mano.remove(c);
	 	}
	 }
	

    public void moverCartaEnMano(int indCarta, int destino) {
		Carta c = mano.get(indCarta);
		mano.remove(indCarta);
		mano.add(destino, c);
	}

	//PUBLIC-----------------------------------------------------------------

	public boolean cortar(int ronda) throws RemoteException {
		boolean puedeCortar = false;
		if(ifJuego.comprobarPosibleCorte(ronda, triosBajados, escalerasBajadas)) {
			if (!mano.isEmpty()) {
				Carta c = getCartaParaTirarAlPozo(0);
			}
			puedeCortar = true;
		}
		return puedeCortar;
	}

    public Carta getCartaParaTirarAlPozo(int indiceCarta) {
		Carta cartaATirar = mano.get(indiceCarta);
		mano.remove(indiceCarta);
		return cartaATirar;
	}

    public void agregarCarta(Carta c) {
		mano.add(c);
	}
    
    public int bajarJuego(ArrayList<Carta> juego) throws RemoteException {
		return ifJuego.comprobarJuego(juego, getPartidaActual().getRonda()); //si tipoJuego es 2, no es juego
	}

	public ArrayList<Carta> getJuego(int[] cartasABajar) {
		return seleccionarCartasABajar(cartasABajar);
	}

	public void addJuego(ArrayList<Carta> juego) {
		juegos.add(juego);
	}

	public void incrementarEscalerasBajadas() {
		escalerasBajadas++;
	}

	public void incrementarTriosBajados() {
		triosBajados++;
	}

	public int[] comprobarQueFaltaParaCortar(int ronda) {
		int trios = 0;
		int escaleras = 0;
		int[] faltante = new int[2];
		switch (ronda) {
			case 1:
				trios = 2 - triosBajados;
				break;
			case 2:
				trios = 1 - triosBajados;
				escaleras = 1 - escalerasBajadas;
				break;
			case 3:
				escaleras = 2 - escalerasBajadas;
				break;
			case 4:
				trios = 3 - triosBajados;
				break;
			case 5:
				trios = 2 - triosBajados;
				escaleras = 1 - escalerasBajadas;
				break;
			case 6:
				trios = 1 - triosBajados;
				escaleras = 2 - escalerasBajadas;
				break;
			case 7:
				escaleras = 3 - escalerasBajadas;
				break;
		}
		faltante[0] = trios;
		faltante[1] = escaleras;
		return faltante;
	}

	public boolean acomodarCartaJuegoPropio(int numCarta, int numJuego, int ronda) throws RemoteException {
		boolean acomodo = false;
		ArrayList<Carta> juegoElegido = juegos.get(numJuego);
		juegoElegido.add(mano.get(numCarta));
		if(ifJuego.comprobarJuego(juegoElegido, ronda) != 2) {
			acomodo = true;
			juegos.get(numJuego).add(mano.get(numCarta));
		}
		return acomodo;
	}

	public void addCarta(Carta c) {
		mano.add(c);
	}

    //SETTERS Y GETTERS-----------------------

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public int getNumeroJugador() {
		return numeroJugador;
	}

    public void setNumeroJugador(int num) {
        numeroJugador = num;
    }
    
    public ArrayList<Carta> getMano() {
        return mano;
    }

	public ArrayList<ArrayList<Carta>> getJuegos() {
		return juegos;
	}

	public void setPuedeBajar() {
		puedeBajar = !puedeBajar;
	}
	public boolean getPuedeBajar() { return puedeBajar;}

	public void setTriosBajados(int trios) {
		triosBajados = trios;
	}

	public void setEscalerasBajadas(int escaleras) {
		escalerasBajadas = escaleras;
	}

	public void setPuntos(int puntos) {
		puntosPartida = puntos;
	}

	public int getPuntos() {
		return puntosPartida;
	}

	public boolean isTurnoActual() {
		return turnoActual;
	}

	public void setTurnoActual() {
		turnoActual = !turnoActual;
	}
}