package src.modelo;

import rmimvc.src.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Juego extends ObservableRemoto implements ifJuego {
	protected static final int FIGURA = 10;
	protected static final int AS = 20;
	protected static final int PUNTOS_COMODIN = 50;
	protected static final int COMODIN = -1; //este valor para que al ordenar cartas queden los comodines primero
	protected static final int CANT_CARTAS_INICIAL = 6;
	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private static Juego instancia;

	//singleton
	public static Juego getInstancia() {
		if (instancia == null) instancia = new Juego();
		return instancia;
	}

	private Juego() {
	}


	//OBSERVER--------------------------------------------------
//	public void setValorAccion1(Integer valor) {
//
//	}

	//PRIVATE----------------------------------------------------

	//PUBLIC----------------------------------------------------------------------


	public int cartasPorRonda(int ronda) throws RemoteException {
		int cantCartas = Juego.CANT_CARTAS_INICIAL;
		switch (ronda) {
			case 2:
				cantCartas = 7;
				break;
			case 3:
				cantCartas = 8;
				break;
			case 4:
				cantCartas = 9;
				break;
			case 5:
				cantCartas = 10;
				break;
			case 6:
				cantCartas = 11;
				break;
			case 7:
				cantCartas = 12;
				break;
		}
		return cantCartas;
	}

	public void agregarJugador(Jugador j) throws RemoteException {
		this.jugadores.add(j);
		notificarObservadores(1); //llama al actualizar del ctrl con (this, 1)
	}

	//GETTERS Y SETTERS
	public Jugador getJugador(String nombreJugador) throws RemoteException {
		boolean encontrado = false;
		int i = 0;
		while (i < this.jugadores.size() && !encontrado) {
			if (this.jugadores.get(i).getNombre().equals(nombreJugador)) {
				encontrado = true;
			} else {
				i++;
			}
		}
		return this.jugadores.get(i);
	}

	public ArrayList<Jugador> getJugadores() throws RemoteException {
		return this.jugadores;
	}


	public int getFigura() throws RemoteException {
		return FIGURA;
	}

	public int getAs() throws RemoteException {
		return AS;
	}

	public int getPuntosComodin() throws RemoteException {
		return PUNTOS_COMODIN;
	}
}