package src.vista;

import src.modelo.jugadorActual;

import java.util.ArrayList;
import java.util.Scanner;

import src.modelo.Carta;
import src.controlador.Controlador;

public class Consola {
    Scanner s = new Scanner(System.in);
    Controlador ctrl;

    private void mostrarCartasNombreJugador(String nombreJugador) {
		System.out.println("Cartas de " + nombreJugador);
	}

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

    // private void mostrarComodin(String numCarta) {
    //     System.out.println(numCarta);
	// }

    // private void mostrarCarta(String numero, Carta c) {
	// 	System.out.println(numero + " de " + c.getPalo());	
	// }

    //PUBLIC-----------------------------------------------------------
    public Consola(){}

    public void mostrarCartasJugador(String nombreJugador) {
        int i = 1;
        mostrarCartasNombreJugador(nombreJugador);
        ArrayList<String> mano = this.ctrl.enviarManoJugador(nombreJugador);
        for (String carta : mano) {
            System.out.println(i + " - " + carta);
            i++;
        }
	}

    private int preguntarCantParaBajar() {
		System.out.println("Cuantas cartas quiere bajar para el juego?");
        return this.s.nextInt();
	}
	
	public int[] preguntarQueBajar(int k) {
        int cantCartas = preguntarCantParaBajar();
        int [] indicesCartasABajar = new int[cantCartas];
        for (int i = 0; i < cantCartas; i++) {
            System.out.println("Indique la carta que quiere bajar (" + (i+1) + ")");
            indicesCartasABajar[i] = this.s.nextInt();
        }
        return indicesCartasABajar;
	}
}
