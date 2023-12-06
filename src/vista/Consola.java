package src.vista;

import src.modelo.jugadorActual;

import java.util.ArrayList;
import java.util.Scanner;

import src.modelo.Carta;
import src.controlador.Controlador;

public class Consola {
    Scanner s = new Scanner(System.in);
    Controlador ctrl = new Controlador();

    private int preguntarCantParaBajar() {
		System.out.println("Cuantas cartas quiere bajar para el juego?");
        return this.s.nextInt();
	}

    // private void mostrarComodin(String numCarta) {
    //     System.out.println(numCarta);
	// }

    // private void mostrarCarta(String numero, Carta c) {
	// 	System.out.println(numero + " de " + c.getPalo());	
	// }

    //PUBLIC-----------------------------------------------------------
    public Consola(){}

    public void mostrarCartasNombreJugador(String nombreJugador) {
		System.out.println("Cartas de " + nombreJugador);
	}

    public void mostrarCartasJugador(ArrayList<String> mano) {
        int i = 1;
        for (String carta : mano) {
            System.out.println(i + " - " + carta);
            i++;
        }
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
