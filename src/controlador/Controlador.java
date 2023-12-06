package src.controlador;

import java.util.ArrayList;

import src.modelo.Carta;
import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.Consola;

public class Controlador {
    private Partida juego;
	private Consola vista;
	
//PRIVATE----------------------------------------------------------------------
    private ArrayList<String> recorrerMano(ArrayList<Carta> mano) {
        ArrayList<String> manoString = new ArrayList<>();
        for (Carta c : mano) {
    		String numString = vista.transformarNumCarta(c.getNumero());
            String palo = c.getPalo().name();
            String carta = "";
            if (palo == null) {
                carta = "COMODIN";
            } else {
                carta = numString + " de " + palo;
            }
            manoString.add(carta);
        }
        return manoString;
    }

    //PUBLIC---------------------------------------------------------------------
	public Controlador(Partida juego, Consola vista) {
		this.juego = juego;
		this.vista = vista;
		//this.miJuego.addObserver(this);
	}

    public ArrayList<String> enviarManoJugador(String nombreJugador) {
        jugadorActual j = this.juego.getJugador(nombreJugador);
        ArrayList<Carta> mano = j.getMano();
        return recorrerMano(mano);
    }
}
