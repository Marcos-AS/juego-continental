package src.modelo;

import rmimvc.src.observer.ObservableRemoto;

import java.util.ArrayList;

public class Juego extends ObservableRemoto implements ifJuego {
	protected static final int FIGURA = 10;
	protected static final int AS = 20;
	protected static final int PUNTOS_COMODIN = 50;
	protected static final int COMODIN = -1; //este valor para que al ordenar cartas queden los comodines primero
	protected static final int CANT_CARTAS_INICIAL = 6;
	protected static ArrayList<Jugador> jugadores = new ArrayList<>();
	private static Juego instancia;

	//singleton
	public static Juego getInstancia() {
		if (instancia == null) instancia = new Juego();
		return instancia;
	}
	private Juego(){}


	//OBSERVER--------------------------------------------------
	public void setValorAccion1(Integer valor) {

	}
}