package src.modelo;

import java.util.ArrayList;
import java.util.Collections;

public class Juego {
    private static final int FIGURA = 10;
    private static final int AS = 20;
	private static final int PUNTOS_COMODIN = 50;
	private static final int COMODIN = -1; //este valor para que al ordenar cartas queden los comodines primero
    private ArrayList<Jugador> jugadores;

	//PRIVATE-----------------------------------------------------
	private static int comprobarEscalera(ArrayList<Carta> juego) {
		int esEscalera = 0; //igual a false, lo pongo en numero para despues saber si es una escalera o un trio
		if(comprobarMismoPalo(juego)) {
			ArrayList<Carta> comodines = new ArrayList<>();
			int formaEscalera = 1;
			juego = ordenarCartas(juego);
			//si es un comodin lo almaceno, sino: si es un numero siguiente inc
			//contador, sino: veo si puedo usar comodin
			for(int i = 0; i < juego.size(); i++) {
				Carta cartaActual = juego.get(i);
				int numCartaActual = cartaActual.getNumero();
				int numCartaSiguiente = juego.get(i+1).getNumero();
				if(numCartaActual == COMODIN) {
					comodines.add(cartaActual);
					juego.remove(cartaActual);
				} else if(numCartaSiguiente == numCartaActual+1) {
					formaEscalera++;
				} else { 
					if(!comodines.isEmpty()) {
						if(numCartaSiguiente == numCartaActual+2) {
							formaEscalera++;
							comodines.remove(0);	
						} 
					} else {
						formaEscalera = 1;
					}
				}
			}
			if(formaEscalera >= 4)
				esEscalera = 1;
		}
		return esEscalera;
	}
	
	private static boolean comprobarMismoPalo(ArrayList<Carta> cartas) {
		boolean mismoPalo = false;
		for(int i = 0; i < cartas.size()-1; i++) {
			Palo palo = cartas.get(i).getPalo();
			mismoPalo = (palo == cartas.get(i+1).getPalo()) || palo == null;
		}
		return mismoPalo;
	}
	
	private static ArrayList<Carta> ordenarCartas(ArrayList<Carta> cartas) { //metodo de insercion
		cartas.sort(null);
		boolean intercambio = true;
		while(intercambio) {
			intercambio = false;
			for(int i = 0; i < cartas.size()-1; i++) {
				Carta cartaActual = cartas.get(i); 
				if(cartaActual.getNumero() > cartas.get(i+1).getNumero()) {
					intercambio = true;
					Carta swap = cartaActual;
					cartas.set(i, cartas.get(i+1));
					cartas.set(i+1, swap);
				}
			}
		}
		return cartas;
	}

	private static int comprobarTrio(ArrayList<Carta> juego) {
		int formaTrio = 1;
		int esTrio = 1; //igual a false, lo pongo en numero para despues saber si es una escalera o un trio
		for(int i = 0; i < juego.size()-1; i++) {
			int numCarta = juego.get(i).getNumero();
			if((numCarta == juego.get(i+1).getNumero()) || numCarta == COMODIN) 
				formaTrio++;
		}
		if(formaTrio >= 3)
			esTrio = 0;
		return esTrio;
	}


	//PUBLIC-------------------------------------------------------

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

    public static int comprobarJuego(ArrayList<Carta> juego, int ronda) {
        int esJuego = 2; //si no es juego entonces queda en 2, si es trio queda en 0, si es escalera queda en 1
		int trio = -1;
		int escalera = -1;
        switch (ronda) {
            case 1:
            case 4:
                esJuego = comprobarTrio(juego);
                break;
            case 2:
            case 5:
            case 6:
				trio = comprobarTrio(juego);
				if (trio==0) {
					esJuego = trio;
				} else {
					escalera = comprobarEscalera(juego);
					if (escalera == 1) {
						esJuego = escalera;
					}
				}
                break;
            case 3:
            case 7:
                esJuego = comprobarEscalera(juego);
                break;
        }
		return esJuego;
	}

    public static boolean comprobarPosibleCorte(int ronda, int trios, int escaleras) {
		boolean puedeCortar = false;
        switch (ronda) {
			case 1:
				puedeCortar = trios == 2;
				break;
			case 2:
				puedeCortar = trios == 1 && escaleras == 1;
				break;
			case 3:
				puedeCortar = escaleras == 2;
				break;
			case 4:
				puedeCortar = trios == 3;
				break;
			case 5:
				puedeCortar = trios == 2 && escaleras == 1;
				break;
			case 6:
				puedeCortar = trios == 1 && escaleras == 2;
				break;
			case 7:
				puedeCortar = escaleras == 3;
				break;
			default:
				break;
		}
		return puedeCortar;
    }
}