package src.vista;

import java.util.ArrayList;
import java.util.Scanner;
import src.controlador.Controlador;

public class Consola {
    private Controlador ctrl;
    private Scanner s = new Scanner(System.in);
    private static final int ELECCION_BAJARSE = 1;
    private static final int ELECCION_NO_BAJARSE = 2;
    private static final int ELECCION_ORDENAR_CARTAS = 3;
    private static final int ELECCION_CORTAR = 4;
    private static final int ELECCION_ROBAR_DEL_POZO = 2;


    //PRIVATE-------------------------------------------------------

    private int preguntarCantParaBajar() {
        int numCartas = 0;
		System.out.println("Cuantas cartas quiere bajar para el juego?");
        numCartas = this.s.nextInt();
        this.s.nextLine();
        while (numCartas > 4 || numCartas < 3) {
            System.out.println("La cantidad de cartas a bajar debe ser entre 3 y 4");
            numCartas = this.s.nextInt();
        }
        return numCartas;
	}

    private boolean paloEsCorrecto(String palo) {
        boolean paloCorrecto = false;
        palo = palo.toUpperCase();
        paloCorrecto = palo.equals("PICAS") || palo.equals("DIAMANTES") || 
        palo.equals("TREBOL") || palo.equals("CORAZONES");
        return paloCorrecto;
    }

    //PUBLIC-----------------------------------------------------------
    public Consola(Controlador ctrl){
        this.ctrl = ctrl;
    }
	
    //preguntar-------------------------------------

	public Object[] preguntarQueBajarParaJuego() {
        int cantCartas = preguntarCantParaBajar();
        Object [] cartasABajar = new Object[cantCartas*2];
        String palo;
        String letraCarta;
        int j = 0;
        int numCarta;
        for (int i = 0; i < cartasABajar.length; i+=2) {
            System.out.println("Carta " + (j+1) + ": ");
            System.out.println("Indique el numero de la carta que quiere bajar: ");
            letraCarta = this.s.nextLine();
            letraCarta = letraCarta.toUpperCase();
            if (letraCarta.equals("J") || letraCarta.equals("Q") ||
            letraCarta.equals("K") || letraCarta.equals("A") ||
            letraCarta.equals("COMODIN")) {
                numCarta = ctrl.transformarLetraCarta(letraCarta);
                cartasABajar[i] = numCarta;
            } else {
                cartasABajar[i] = Integer.parseInt(letraCarta);

            }
            //this.s.nextLine();
            System.out.println("Indique el palo de la carta que quiere bajar: ");
            System.out.println("(PICAS, DIAMANTES, TREBOL, CORAZONES) ");
            palo = this.s.nextLine();
            while (!paloEsCorrecto(palo)) {
                System.out.println("El palo ingresado no es correcto, ingrese de nuevo: ");
                palo = this.s.nextLine();
            }
            cartasABajar[i+1] = palo;
            j++;
        }
        System.out.println();
        return cartasABajar;
	}

    public int[] preguntarParaOrdenarCartas() {
        int[] elecciones = new int[2];
        System.out.println("Elija el número de carta que quiere mover: ");
        elecciones[0] = this.s.nextInt();
        System.out.println("Elija el número de destino al que quiere mover la carta: ");
        elecciones[1] = this.s.nextInt();
        System.out.println();
        return elecciones;
    }

    public int preguntarSiDeseaContinuar() {
        int eleccion = 0;
        System.out.println("Desea continuar?");
        System.out.println("1 - Si");
        System.out.println("2 - No");
        eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
    }

    public int preguntarQueBajarParaPozo() {
        int eleccion = 0;
        System.out.println("Indique el numero de carta para tirar al pozo: ");
        eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
    }

    //MENUS-------------------------------
    public int menuRobar() {
        int eleccion = 0;
		System.out.println("----------------------------------------");
		System.out.println("Quiere robar del pozo o robar del mazo?");
		System.out.println("1 - Robar del mazo");
		System.out.println("2 - Robar del pozo");
		System.out.println("Elija una opcion: ");
		eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
	}
	
    public int menuBajar() {
        int eleccion = 0;
        System.out.println("1 - Bajar algún juego");
   		System.out.println("2 - No quiero bajarme");
  		System.out.println("3 - Ordenar cartas");
        System.out.println("4 - Cortar");
        eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
    }

    //MOSTRAR---------------------------------------------------------

    public void mostrarCartasNombreJugador(String nombreJugador) {
		System.out.println("Cartas de " + nombreJugador);
	}

    public void mostrarCartasJugador(ArrayList<String> mano) {
        int i = 0;
        for (String carta : mano) {
            System.out.println(i + " - " + carta);
            i++;
        }
        System.out.println("----------");
        System.out.println("----------");
	}

    public void mostrarTurnoJugador(String nombreJugador) {
        System.out.println("Es el turno del jugador: " + nombreJugador);
    }

    public void mostrarLoQueFaltaParaCortar(int[] faltaParaCortar) {
        System.out.println("Faltan " + faltaParaCortar[0] + " trios");
        System.out.println("Faltan " + faltaParaCortar[1] + " escaleras");
    }

    public void mostrarNoPuedeCortar() {
        System.out.println("No puede cortar");
    }

    public void mostrarCarta(String carta) {
        System.out.println(carta);
    }
    
    public void mostrarPozo(String carta) {
        System.out.println("Pozo: ");
        mostrarCarta(carta);
    }

    public void mostrarNoPuedeBajarJuego() {
        System.out.println("No puede bajar porque la combinacion elegida no forma un juego valido para la ronda\n");
    }

    public void mostrarJuego(int numJuego) {
        System.out.println("Juego N° " + numJuego+":\n");
    }

    public void mostrarCombinacionRequerida(int ronda) {
        System.out.print("Para esta ronda deben bajarse: ");
        switch (ronda) {
            case 1:
                System.out.println("2 tríos");
                break;
            case 2:
                System.out.println("1 trío y 1 escalera");
                break;
            case 3:
                System.out.println("2 escaleras");
                break;
            case 4:
                System.out.println("3 tríos");
                break;
            case 5:
                System.out.println("2 tríos y 1 escalera");
                break;
            case 6:
                System.out.println("1 tríos y 2 escaleras");
                break;
            case 7:
                System.out.println("3 escaleras");
                break;
        
        }
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

    public int getEleccionRobarDelPozo() {
        return ELECCION_ROBAR_DEL_POZO;
    }
}
