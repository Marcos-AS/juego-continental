package src.vista;

import src.modelo.jugadorActual;
import src.modelo.Carta;

public class Consola {
    
    private void mostrarCartasNombreJugador(String nombreJugador) {
		System.out.println("Cartas de " + nombreJugador);
	}

    private String transformarNumCarta(int numCarta) {
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
                case "50":
                    num = "COMODIN";
                    break;
			}
		}
		return num;
	}

    private void mostrarComodin(String numCarta) {
        System.out.println(numCarta);
	}

    private void mostrarCarta(String numero, Carta c) {
		System.out.println(numero + " de " + c.getPalo());	
	}


    //PUBLIC-----------------------------------------------------------
    public Consola(){}

    public void mostrarCartasJugador(jugadorActual j) {
        mostrarCartasNombreJugador(j.getNombre());
        for (Carta c : j.getMano()) {
    		String numString = transformarNumCarta(c.getNumero());
            if (numString == "COMODIN") {
                mostrarComodin(numString);
            } else {
                mostrarCarta(numString, c);        
            }
        }		
	}
}
