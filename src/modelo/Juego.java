package src.modelo;

import java.util.ArrayList;

public class Juego {
    private static final int FIGURA = 10;
    private static final int AS = 20;
    private ArrayList<Jugador> jugadores;

    public static int cartasPorRonda(int ronda) {
		int cantCartas = 7;
		switch(ronda) {
            case 2:
                cantCartas = 8;
                break;
            case 3:
                cantCartas = 9;
                break;
            case 4:
                cantCartas = 10;
                break;
            case 5:
                cantCartas = 11;
                break;
            case 6:
                cantCartas = 12;
                break;
            case 7:
                cantCartas = 13;
                break;
		}
		return cantCartas;
	}
}
