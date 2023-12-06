package src.controlador;

import java.util.ArrayList;

import src.modelo.Carta;
import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.Consola;

public class Controlador {
    private Partida juego;
	private Consola vista = new Consola();
	
//PRIVATE----------------------------------------------------------------------
    public String transformarNumCarta(int numCarta) {
        String num = ((Integer) numCarta).toString();
        if(numCarta <= 1 || numCarta >= 11) { 
            switch(num) {
                case "1":
                    num = "A";
                    break;
                case "11":
                    num = "J";
                    break;
                case "12":
                    num = "Q";
                    break;
                case "13":
                    num = "K";
                    break;
                case "-1":
                    num = "COMODIN";
                    break;
            }
        }
        return num;
    }    

    private ArrayList<String> recorrerMano(ArrayList<Carta> mano) {
        ArrayList<String> manoString = new ArrayList<>();
        for (Carta c : mano) {
    		String numString = transformarNumCarta(c.getNumero());
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
	public Controlador() {
		//this.miJuego.addObserver(this);
	}

    public ArrayList<String> enviarManoJugador(Partida p, String nombreJugador) {
        jugadorActual j = p.getJugador(nombreJugador);
        ArrayList<Carta> mano = j.getMano();
        return recorrerMano(mano);
    }

    public void bajarJuego(jugadorActual j) {
        int[] bajar = vista.preguntarQueBajar();
        if (bajar.length >= 3) {
            if(j.bajarJuego(bajar))
                j.setPuedeBajar();
        }
    }

    public boolean cortar(Partida p, jugadorActual j) {
        boolean corte = false;
        if (j.comprobarCorte()) {
            if (j.getMano().size()==1) {
                j.tirarAlPozo(0);
            }
            p.incrementarRonda();
            corte = true;
        }
        return corte;
    }
}