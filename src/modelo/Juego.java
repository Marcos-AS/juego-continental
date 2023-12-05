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

    public static boolean comprobarJuego(ArrayList<Carta> juego, int ronda) {
        boolean esJuego = false;
        switch (ronda) {
            case 1:
            case 4:
                esJuego = comprobarTrio(juego);
                break;
            case 2:
            case 5:
            case 6:
                esJuego = comprobarTrio(juego) || comprobarEscalera(juego);
                break;
            case 3:
            case 7:
                esJuego = comprobarEscalera(juego);
                break;
        }
		return esJuego;
	}

	//usa las funciones comprobarMismoPalo y ordenarCartas 
	protected static boolean comprobarEscalera(ArrayList<Carta> juego) {
		boolean mismoPalo = comprobarMismoPalo(juego);
		boolean esEscalera = false;
		if(mismoPalo) {
			ArrayList<Carta> comodines = new ArrayList<>();
			int formaEscalera = 1;
			juego = ordenarCartas(juego);
			//si es un comodin lo almaceno, sino: si es un n�mero siguiente inc
			//contador, sino: veo si puedo usar comod�n
			for(int i = 0; i < juego.size(); i++) {
				Carta c = juego.get(i);
				if(c.getNumero() == COMODIN) {
					comodines.add(c);
					juego.remove(c);
				} else if(juego.get(i+1).getNumero() == c.getNumero() + 1) {
					formaEscalera++;
				} else { 
					if(!comodines.isEmpty()) {
						if(juego.get(i+1).getNumero() == c.getNumero() + 2) {
							formaEscalera++;
							comodines.remove(0);	
						} 
					} else {
						formaEscalera = 1;
			}}}
			if(formaEscalera >= 4)
				esEscalera = true;
		}
		return esEscalera;
	}

	private static boolean comprobarMismoPalo(ArrayList<Carta> cartas) {
		boolean mismoPalo = true;
		for(int i = 0; i < cartas.size()-1; i++) {
			if(cartas.get(i).getPalo() != cartas.get(i+1).getPalo())
				mismoPalo = false;
		}
		return mismoPalo;
	}
	
	protected static boolean comprobarTrio(ArrayList<Carta> juego) {
		int formaTrio = 1;
		boolean esTrio = false;
		for(int i = 0; i < juego.size()-1; i++) {
			if(juego.get(i).getNumero() == juego.get(i+1).getNumero()) 
				formaTrio++;
		}
		if(formaTrio >= 3)
			esTrio = true;
		return esTrio;
	}

}