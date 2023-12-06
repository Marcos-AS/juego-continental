package src.vista;

import src.modelo.jugadorActual;

import java.util.ArrayList;
import java.util.Scanner;

import src.modelo.Carta;
import src.controlador.Controlador;

public class Consola {
    Scanner s = new Scanner(System.in);
    Controlador ctrl = new Controlador();
    private static final int ELECCION_BAJARSE = 1;
    private static final int ELECCION_NO_BAJARSE = 2;
    private static final int ELECCION_ORDENAR_CARTAS = 3;
    private static final int ELECCION_CORTAR = 4;

    private int preguntarCantParaBajar() {
        int numCartas = 0;
		System.out.println("Cuantas cartas quiere bajar para el juego?");
        numCartas = this.s.nextInt();
        while (numCartas > 4) {
            System.out.println("No puede bajar más de 4 cartas");
            numCartas = this.s.nextInt();
        }
        return numCartas;
	}

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
	
	public int[] preguntarQueBajar() {
        int cantCartas = preguntarCantParaBajar();
        int [] indicesCartasABajar = new int[cantCartas];
        for (int i = 0; i < cantCartas; i++) {
            System.out.println("Indique la carta que quiere bajar (" + (i+1) + ")");
            indicesCartasABajar[i] = this.s.nextInt();
        }
        return indicesCartasABajar;
	}

    //MENUS-------------------------------
    public int menuRobar() {
		System.out.println("----------------------------------------");
		System.out.println("Quiere robar del pozo o robar del mazo?");
		System.out.println("1 - Robar del mazo");
		System.out.println("2 - Robar del pozo");
		System.out.println("Elija una opcion: ");
		return this.s.nextInt();
	}
	
    public int menuBajar() {
        System.out.println("1 - Bajar algún juego");
   		System.out.println("2 - No quiero bajarme");
  		System.out.println("3 - Ordenar cartas");
        System.out.println("4 - Cortar");
        return this.s.nextInt();
    }

	public int menuTirar() {
		System.out.println("1 - Tirar al pozo");
		System.out.println("2 - Acomodar cartas en un juego bajado");
		System.out.println("Elija una opcion: ");
        return this.s.nextInt();
	}

    public int[] ordenarCartas() {
        int[] elecciones = new int[2];
        System.out.println("Elija el número de carta que quiere mover: ");
        elecciones[0] = this.s.nextInt();
        System.out.println("Elija el número de destino al que quiere mover la carta: ");
        elecciones[1] = this.s.nextInt();
        return elecciones;
    }

    public void mostrarTurnoJugador(String nombreJugador) {
        System.out.println("Es el turno del jugador: " + nombreJugador);
    }

    public void mostrarLoQueFaltaParaCortar(int[] faltaParaCortar) {
        System.out.println("Faltan " + faltaParaCortar[0] + " trios");
        System.out.println("Faltan " + faltaParaCortar[1] + " escaleras");
    }

    public int preguntarSiDeseaContinuar() {
        System.out.println("Desea continuar?");
        System.out.println("1 - Si");
        System.out.println("2 - No");
        return this.s.nextInt();
    }

    //GETTERS Y SETTERS---------------------------
    public int getEleccionOrdenarCartas(){
        return ELECCION_ORDENAR_CARTAS;
    }

    public int getEleccionBajarse(){
        return ELECCION_BAJARSE;
    }

    public int getEleccionNoBajarse(){
        return ELECCION_NO_BAJARSE;
    }

    public int getEleccionCortar(){
        return ELECCION_CORTAR;
    }
}
