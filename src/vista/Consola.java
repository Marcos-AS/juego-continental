package src.vista;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifPartida;
import src.modelo.ifJugador;
import src.serializacion.Serializador;

public class Consola implements ifVista{
    private Controlador ctrl;
    private Scanner s = new Scanner(System.in);
    private String nombreVista;

    public Consola(){}

    //PRIVATE-------------------------------------------------------

    private int preguntarCantParaBajar() {
        int numCartas;
		System.out.println("Cuantas cartas quiere bajar para el juego?");
        numCartas = this.s.nextInt();
        this.s.nextLine();
        while (numCartas > 4 || numCartas < 3) {
            System.out.println("La cantidad de cartas a bajar debe ser entre 3 y 4");
            numCartas = this.s.nextInt();
            this.s.nextLine();
        }
        return numCartas;
	}

    private boolean paloEsCorrecto(String palo) {
        boolean paloCorrecto;
        palo = palo.toUpperCase();
        paloCorrecto = palo.equals("PICAS") || palo.equals("DIAMANTES") || 
        palo.equals("TREBOL") || palo.equals("CORAZONES");
        return paloCorrecto;
    }

    //PUBLIC-----------------------------------------------------------

    public void mostrarJuegos(ArrayList<ArrayList<String>> juegos) {
        int numJuego = 1;
        for (ArrayList<String> juego : juegos) {
            mostrarJuego(numJuego);
            mostrarCartas(juego);
            numJuego++;
        }
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
            boolean numValido = false;
            System.out.println("Carta " + (j+1) + ": ");
            System.out.println("Indique el numero o letra de la carta que quiere bajar: ");
            letraCarta = this.s.nextLine();
            letraCarta = letraCarta.toUpperCase();
            if (letraCarta.equals("J") || letraCarta.equals("Q") ||
                letraCarta.equals("K") || letraCarta.equals("A") ||
                letraCarta.equals("COMODIN")) {
                numCarta = ctrl.transformarLetraCarta(letraCarta);
                cartasABajar[i] = numCarta;
            } else {
                //si lo ingresado no es una letra trato de pasarlo a int
                while (!numValido) {                    
                    try {
                        cartasABajar[i] = Integer.parseInt(letraCarta);
                        numValido = true;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        System.out.println("Carta " + (j+1) + ": ");
                        System.out.println("Indique nuevamente el numero o letra de la carta que quiere bajar: ");
                        letraCarta = this.s.nextLine();
                    }

                }

            }

            if (letraCarta.equals("COMODIN")) {
                palo = "COMODIN";
            } else {
                System.out.println("Indique el palo de la carta que quiere bajar: ");
                System.out.println("(PICAS, DIAMANTES, TREBOL, CORAZONES) ");
                palo = this.s.nextLine();
                while (!paloEsCorrecto(palo)) {
                    System.out.println("El palo ingresado no es correcto, ingrese de nuevo: ");
                    palo = this.s.nextLine();
                }
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

    public int preguntarQueBajarParaPozo(int cantCartas) {
        int eleccion;
        System.out.println("Indique el indice de carta para tirar al pozo: ");
        eleccion = this.s.nextInt();
        System.out.println();
        while (eleccion < 0 || eleccion >= cantCartas) {
            System.out.println("Vuelva a ingresar un indice de carta");
            eleccion = this.s.nextInt();
            System.out.println();
        }
        return eleccion;
    }

	public int preguntarCartaParaAcomodar() {
		System.out.println("Indique el numero de carta que quiere acomodar en un juego");
		int numCarta = this.s.nextInt();
        System.out.println();
		return numCarta;
	}

    public int preguntarEnQueJuegoQuiereAcomodar() {
        System.out.println("En que numero de juego quiere acomodar su carta?");
        int eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
    }

    @Override
    public String preguntarNombreNuevoJugador() throws RemoteException {
        System.out.println("Indique su nombre:");
        String nombreJugador = this.s.nextLine();
        System.out.println("Jugador agregado.");
        return nombreJugador;
    }

    //MENUS-------------------------------
    public int menuRobar() {
		System.out.println("----------------------------------------");
		System.out.println("Quiere robar del pozo o robar del mazo?");
		System.out.println("1 - Robar del mazo");
		System.out.println("2 - Robar del pozo");
		System.out.println("Elija una opcion: ");
		int eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
	}

    public int menuBajar() {
        System.out.println("Elija una opcion: ");
        System.out.println("1 - Bajar algún juego");
   		System.out.println("2 - Ir a tirar");
  		System.out.println("3 - Ordenar cartas");
        System.out.println("4 - Cortar (para cortar debe tener ya los juegos bajados)");
        System.out.println("5 - Acomodar en un juego bajado propio");
        int eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
    }

    public int menuRobarDelPozo() {
		System.out.println("Quiere robar del pozo?");
        System.out.println("1 - No");
        System.out.println("2 - Si");
        int eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
    }

    @Override
    public void nuevaVentana() {}

    public int menuBienvenida() {
        System.out.println("Bienvenido al juego Continental.");
        System.out.println("Elija una opcion: ");
        System.out.println("1 - Iniciar partida nueva");
        System.out.println("2 - Ver ranking mejores jugadores");
        System.out.println("3 - Ver reglas de juego");
        System.out.println("4 - Jugar partida recién iniciada");
        System.out.println("-1 - Salir del juego");
        int eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
    }


    //MOSTRAR---------------------------------------------------------
    public void mostrarReglas() {
        System.out.println("""
                OBJETIVO\r
                ------------------------------------------------------------------------------------------------------------------------------------------------------------ \r
                El objetivo del juego es formar las combinaciones requeridas en cada una de las 7 rondas, procurando acumular la menor cantidad posible de puntos. \r
                Al final de todas las rondas, el jugador con menos puntos es el ganador.\r
                \r
                RONDAS\r
                ------------------------------------------------------------------------------------------------------------------------------------------------------------ \r
                Se juegan 7 rondas, cada una con requisitos específicos:\r
                - Ronda 1: Dos tríos\r
                - Ronda 2: Un trío y una escalera\r
                - Ronda 3: Dos escaleras\r
                - Ronda 4: Tres tríos\r
                - Ronda 5: Dos tríos y una escalera\r
                - Ronda 6: Un trío y dos escaleras\r
                - Ronda 7: Tres escaleras\r
                \r
                DEFINICIONES\r
                ------------------------------------------------------------------------------------------------------------------------------------------------------------ \r
                Trío: 3 cartas con el mismo número, sin importar el palo.\r
                Escalera: 4 o más cartas consecutivas del mismo palo. Puede comenzar con cualquier carta, y el as puede ser la carta intermedia entre la K y el 2. \r
                Comodín: Se puede tener un trío de comodines, pero no se pueden colocar dos comodines JUNTOS en una escalera.\r
                \r
                ROBO\r
                ------------------------------------------------------------------------------------------------------------------------------------------------------------ \r
                En cada turno, el jugador debe robar una carta y descartar otra. Si no roba del pozo, los siguientes jugadores pueden optar por robar del pozo, pero si lo hacen,\r
                 también deben robar otra del mazo (robo con "castigo"). Este proceso sigue en orden hacia la derecha. En caso de que ningún jugador desee robar del pozo,\r
                 cada jugador debe robar una carta en su turno, ya sea del mazo o del pozo.\r
                \r
                BAJAR JUEGOS Y CORTAR\r
                ------------------------------------------------------------------------------------------------------------------------------------------------------------ \r
                Para cortar, el jugador debe tener completa la combinación requerida para la ronda.\r
                Se puede cortar con la carta que sobra, o elegir no cortar si no hay cartas sobrantes. En estos casos, el jugador gana la ronda. \r
                Además, el jugador puede bajar sus juegos una vez durante la ronda, con las siguientes restricciones:\r
                - No puede robar con "castigo".\r
                - No puede bajar de nuevo.\r
                - Las cartas sobrantes se pueden colocar en los juegos bajados por otros jugadores.\r
                \r
                FIN DE LA RONDA\r
                ------------------------------------------------------------------------------------------------------------------------------------------------------------ \r
                Al finalizar cada ronda, se suman los puntos de las cartas que los jugadores tienen en la mano. El ganador de la ronda no suma puntos. \r
                Las cartas tienen valores específicos: los números valen su denominación, las figuras valen 10, el as vale 20 y el comodín 50.\r
                \r
                FIN DEL JUEGO\r
                ------------------------------------------------------------------------------------------------------------------------------------------------------------ \r
                Después de todas las rondas, el jugador con menos puntos es declarado ganador.""");
    }

    public void mostrarInicioPartida() {
        System.out.println("Se ha iniciado una nueva partida.");
        System.out.println("Para ingresar vaya a la opcion 4: Jugar partida recien iniciada.");
    }

    public void mostrarCartasNombreJugador(String nombreJugador) {
		System.out.println("Cartas de " + nombreJugador);
	}

    public void mostrarCartas(ArrayList<String> cartas) {
        int i = 0;
        for (String carta : cartas) {
            System.out.println(i + " - " + carta);
            i++;
        }
        System.out.println("----------");
        System.out.println("----------");
    }

    public void mostrarTurnoJugador(String nombreJugador) {
        System.out.println("*****************************************");
        System.out.println("Es el turno del jugador: " + nombreJugador);
        System.out.println("*****************************************");
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
    
    public void mostrarPozo(ifCarta c) {
        System.out.println("Pozo: ");
        mostrarCarta(ifVista.cartaToString(c));
    }

    public void mostrarNoPuedeBajarJuego(int i) {
        if (i == 1) {
            System.out.println("No puede bajar porque la combinacion elegida no forma un juego valido para la ronda\n");
        } else {
            System.out.println("No puede volver a bajar juegos en esta ronda.");
        }
    }

    public void mostrarJuego(int numJuego) {
        System.out.println("Juego N° " + numJuego+":\n");
    }

    public static void mostrarCombinacionRequerida(int ronda) {
        System.out.print("Para esta ronda deben bajarse: ");
        switch (ronda) {
            case 1:
                System.out.println("Ronda 1: 2 tríos");
                break;
            case 2:
                System.out.println("Ronda 2: 1 trío y 1 escalera");
                break;
            case 3:
                System.out.println("Ronda 3: 2 escaleras");
                break;
            case 4:
                System.out.println("Ronda 4: 3 tríos");
                break;
            case 5:
                System.out.println("Ronda 5: 2 tríos y 1 escalera");
                break;
            case 6:
                System.out.println("Ronda 6: 1 tríos y 2 escaleras");
                break;
            case 7:
                System.out.println("Ronda 7: 3 escaleras");
                break;

        }
    }

    public void jugadorPuedeRobarConCastigo(String nombreJugador) {
        System.out.println("El jugador " + nombreJugador + " puede robar con castigo.");
    }

    public void jugadorHaRobadoConCastigo(String nombreJugador) {
        System.out.println("El jugador "+ nombreJugador + " ha robado con castigo.");
    }

    public void mostrarNoPuedeRobarDelPozo() {
        System.out.println("No puede robar del pozo porque no tiene cartas");
    }

	public void mostrarGanador(String nombre) {
		System.out.println("El jugador " + nombre + " es el ganador!");// con " + puntos + " puntos!");
	}

	public void mostrarPuntosJugador(String nombreJugador, int puntos) {
		System.out.println("Jugador " + nombreJugador + ": " + puntos);
	}

    public void mostrarNoPuedeAcomodarJuegoPropio() {
        System.out.println("No puede acomodar porque no tiene juegos bajados.");
    }

    public void mostrarUltimoJugadorAgregado(ArrayList<ifJugador> js) {
        System.out.println("El jugador " + js.get(js.size()-1).getNombre() + " ha ingresado.");
    }

    public void noSePuedeIniciarPartida(int i) {
        if (i == 1) {
            System.out.println("No se puede iniciar la partida porque faltan jugadores para la cantidad deseada.");
        } else if (i == 2) {
            System.out.println("La partida aun no ha sido creada. Seleccione la opcion 1: 'Iniciar partida nueva' ");
        } else if (i == 3) {
            System.out.println("No se puede iniciar la partida porque faltan jugadores (minimo 2)");
        }
    }

    public void mostrarPuntosRonda(int[] puntos) {
        System.out.println("Puntuacion: ");
        for (int i = 1; i < puntos.length; i++) {
            mostrarPuntosJugador(this.ctrl.getJugadorPartida(i-1).getNombre(), puntos[i]);
        }
    }

    public void mostrarFinalizoPartida() {
        System.out.println("La partida ha finalizado.");
    }

    public int preguntarCantJugadores() {
        System.out.println("Cuantos jugadores desea para la nueva partida?");
        int cantJugadores = this.s.nextInt();
        System.out.println();
        return cantJugadores;
    }

    public void mostrarRanking(Object[] rankingJugadores) {
        System.out.println("Ranking: ");
        for (int i = 0; i < rankingJugadores.length; i++) {
            ifJugador j = (ifJugador) rankingJugadores[i];
            System.out.println((i+1) +": " + j.getNombre() + " --- puntos: " + j.getPuntosAlFinalizar());
        }
    }

    //GETTERS Y SETTERS---------------------------

    public String getNombreVista() {
        return this.nombreVista;
    }

    public void setNombreVista(String i) {
        this.nombreVista = i;
    }

    @Override
    public void actualizar(Object actualizacion, int indice) throws RemoteException {
        switch (indice) {//del 0 al 5 porque como maximo 6 jugadores
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                ifJugador jA = (ifJugador) actualizacion;
                String nombreJugador = jA.getNombre();
                mostrarTurnoJugador(nombreJugador);
                if (this.nombreVista.equals(nombreJugador)) {
                    ctrl.desarrolloTurno(jA);
                }
                break;
            }
            case 6: {
                mostrarInicioPartida();
                break;
            }
            case 7: {
                ArrayList<ifJugador> js = (ArrayList<ifJugador>) actualizacion;
                mostrarUltimoJugadorAgregado(js);
                break;
            }
            case 9: {
                mostrarCombinacionRequerida(((ifPartida) actualizacion).getRonda());
                mostrarPozo(((ifPartida) actualizacion).sacarPrimeraDelPozo());
                break;
            }
            case 10: {
                String s = (String) actualizacion;
                if (!this.ctrl.getEstadoPartida()) {
                    mostrarGanador(s);
                } else if (!s.equalsIgnoreCase(this.nombreVista)) {
                    System.out.println("El jugador " + s + " ha iniciado una partida nueva");
                }
                break;
            }
            case 11: { //un jugador puede robar con castigo
                int[] a = (int[]) actualizacion;
                ifJugador j = this.ctrl.getJugadorPartida(a[0]);
                if (this.nombreVista.equals(j.getNombre())) {
                    ctrl.desarrolloRoboConCastigo(ctrl.enviarManoJugador(j), j, a[2]);
                }
                break;
            }
            case 12: {
                String nombreJugador = this.ctrl.getJugadorPartida((int)actualizacion).getNombre();
                jugadorHaRobadoConCastigo(nombreJugador);
                break;
            }
            case 14: {
                System.out.println("Esta ronda ha finalizado.");
                System.out.println("--------------------------");
                break;
            }
            case 15: {
                int[] puntos = (int[]) actualizacion;
                mostrarPuntosRonda(puntos);
                break;
            }
            case 16: {
                mostrarRanking((Object[]) actualizacion);
                break;
            }
        }

    }

    @Override
    public void setControlador(Controlador ctrl) {
        this.ctrl = ctrl;
    }
}