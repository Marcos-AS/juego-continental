package src.vista;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import src.controlador.Controlador;
import src.modelo.*;

public class Consola implements ifVista{
    private Controlador ctrl;
    private Scanner s = new Scanner(System.in);
    private static final int ELECCION_BAJARSE = 1;
    private static final int ELECCION_NO_BAJARSE = 2;
    private static final int ELECCION_ORDENAR_CARTAS = 3;
    private static final int ELECCION_CORTAR = 4;
    private static final int ELECCION_ACOMODAR_JUEGO_PROPIO = 5;
    private static final int ELECCION_ACOMODAR_JUEGO_AJENO = 6;
    private static final int ELECCION_ROBAR_DEL_MAZO = 1;
    private static final int ELECCION_ROBAR_DEL_POZO = 2;
    private int numVista;

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
    public void bajarse(String nombreJugador, Object [] cartasABajar) throws RemoteException {
        if(!ctrl.bajarJuego(nombreJugador, cartasABajar)) {
            mostrarNoPuedeBajarJuego();
        } else {
            //ArrayList<ArrayList<String>> juegos = this.ctrl.enviarJuegosJugador(nombreJugador);
            int numJuego = 1;
            /*for (ArrayList<String> juego : juegos) {
                mostrarJuego(numJuego);
                mostrarCartas(juego);
                numJuego++;
            }*/
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

    public int preguntarSiDeseaContinuar() {
        int eleccion;
        System.out.println("Desea continuar?");
        System.out.println("1 - Si");
        System.out.println("2 - No");
        eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
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
    public void preguntarNombreNuevoJugador() throws RemoteException {
        int eleccion = 0;
        System.out.println("Indique su nombre:");
        String nombreJugador = this.s.next();
        System.out.println("Jugador agregado.");
        this.ctrl.agregarNuevoJugador(nombreJugador);
    }

    /*private void preguntarIniciarPartida(Object actualizacion) throws RemoteException {
        //String eleccion = this.s.next();
        //System.out.println();
        //if (eleccion.equalsIgnoreCase("si") || eleccion.equalsIgnoreCase("sí"))
            //this.ctrl.iniciarPartida((Partida) actualizacion);
    }*/

    //MENUS-------------------------------
    public int menuRobar() {
        int eleccion;
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
        int eleccion;
        System.out.println("Elija una opcion: ");
        System.out.println("1 - Bajar algún juego");
   		System.out.println("2 - Ir a tirar");
  		System.out.println("3 - Ordenar cartas");
        System.out.println("4 - Cortar (para cortar debe tener ya los juegos bajados)");
        System.out.println("5 - Acomodar en un juego bajado propio");
        System.out.println("6 - Acomodar en un juego bajado ajeno");
        eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
    }

    public int menuRobarDelPozo() {
        int eleccion;
		System.out.println("Quiere robar del pozo?");
        System.out.println("1 - No");
        System.out.println("2 - Si");
        eleccion = this.s.nextInt();
        System.out.println();
        return eleccion;
    }

    public int menuBienvenida() {
        System.out.println("Bienvenido al juego Continental.");
        System.out.println("Elija una opcion: ");
        System.out.println("1 - Iniciar partida nueva");
        System.out.println("2 - Continuar una partida");
        System.out.println("3 - Ver ranking de partidas");
        System.out.println("4 - Ver reglas de juego");
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
        System.out.println();
        //this.s.close();
        //this.s = new Scanner(System.in);
        System.out.println("Se ha iniciado una nueva partida.");
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
    
    public void mostrarPozo(ifPartida p) {
        ifCarta c = p.sacarPrimeraDelMazo();
        String carta = ifVista.transformarNumCarta(c.getNumero()) + " de " + c.getPalo().name();
        //String carta = this.ctrl.enviarPrimeraCartaPozo();
        System.out.println("Pozo: ");
        mostrarCarta(carta);
    }

    public void mostrarNoPuedeBajarJuego() {
        System.out.println("No puede bajar porque la combinacion elegida no forma un juego valido para la ronda\n");
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

    public void mostrarContinuaTurno(String nombreJugador) {
        System.out.println("Continua el turno del jugador " + nombreJugador);
    }

    public void mostrarNoPuedeRobarDelPozo() {
        System.out.println("No puede robar del pozo porque no hay cartas en el pozo");
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

    public void mostrarListaJugadores(Object jugadores) {
        int i = 1;
        ArrayList<ifJugador> js = (ArrayList<ifJugador>) jugadores;
        for (ifJugador j : js) {
            System.out.println("Jugador " + i + ": " +j.getNombre());
            i++;
        }
    }

    public void mostrarJugador(String nombreJugador, int numJugador) {
        System.out.println("Jugador " + numJugador + ": " + nombreJugador);
    }

    public void mostrarUltimoJugadorAgregado(Object jugadores, int indice) {
        ArrayList<ifJugador> js = (ArrayList<ifJugador>) jugadores;
        if (indice == 1) {
            System.out.println("El jugador " + js.get(js.size()-1).getNombre() + " ha ingresado.");
        } else {
            System.out.println("El jugador " + js.get(js.size()-1).getNombre() + " ha ingresado a una partida.");
        }
    }

    private int ordenarCartasTurno(ifJugador j) throws RemoteException {
        int[] ordenar = preguntarParaOrdenarCartas();
        j.eleccionOrdenar(ordenar);
        ArrayList<String> mano = this.ctrl.enviarManoJugador(j);
        mostrarCartas(mano);
        return menuBajar();
    }

    private int bajarJuegosTurno(ifJugador j) throws RemoteException {
        Object [] cartasABajar = preguntarQueBajarParaJuego();
        bajarse(j.getNombre(), cartasABajar);
        ArrayList<String> mano = this.ctrl.enviarManoJugador(j);
        mostrarCartas(mano);
        return menuBajar();
    }

    private boolean cortarTurno(ifJugador j) throws RemoteException {
        boolean corte = j.cortar();
        if (!corte) {
            int[] triosYEscalerasQueFaltan = j.comprobarQueFaltaParaCortar();
            mostrarLoQueFaltaParaCortar(triosYEscalerasQueFaltan);
        }
        return corte;
    }

    private void tirarAlPozoTurno(ifJugador j) throws RemoteException {
        ArrayList<String> mano = this.ctrl.enviarManoJugador(j);
        mostrarCartas(mano);
        int eleccion = preguntarQueBajarParaPozo(mano.size());
        j.tirarAlPozo(eleccion);
    }


    //GETTERS Y SETTERS---------------------------

    public void setNumVista(int i) {
        this.numVista = i;
    }

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

    public int getEleccionRobarDelMazo() {
        return ELECCION_ROBAR_DEL_MAZO;
    }

    public int getEleccionAcomodarJuegoPropio() {
        return ELECCION_ACOMODAR_JUEGO_PROPIO;
    }

    public int getEleccionAcomodarJuegoAjeno() {
        return ELECCION_ACOMODAR_JUEGO_AJENO;
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
                ifJugador j = (ifJugador) actualizacion;
                String nombreJugador = j.getNombre();
                mostrarTurnoJugador(nombreJugador);
                if (this.numVista == indice) {
                    ArrayList<String> mano;
                    mano = this.ctrl.enviarManoJugador(j);
                    mostrarCartas(mano);
                    int eleccion = menuRobar();

                    //aca va robo con castigo!!
                    //si el pozo esta vacio, se roba del mazo. Si se eligio robar del mazo en un principio tambien sucede aca
                    if(eleccion == ELECCION_ROBAR_DEL_MAZO) {
                        //mostrarNoPuedeRobarDelPozo();
                        this.ctrl.robarDelMazo(j);
                    }

                    mano = this.ctrl.enviarManoJugador(j);
                    mostrarCartas(mano);

                    eleccion = menuBajar();
                    boolean corte = false;

                    //ordenar cartas en la mano
                    while (eleccion == getEleccionOrdenarCartas())
                        eleccion = ordenarCartasTurno(j);

                    //aca va acomodar en juego!

                    //bajarse
                    while (eleccion == getEleccionBajarse())
                        eleccion = bajarJuegosTurno(j);

                    //si quiere cortar, comprobar si puede
                    if (eleccion == getEleccionCortar())
                        corte = cortarTurno(j);

                    //tirar
                    if (!corte)
                        tirarAlPozoTurno(j);
                }
            }
            case 6: {
                mostrarInicioPartida();
                break;
            }
            case 7: {
                //System.out.println("Jugadores: ");
                //mostrarListaJugadores(actualizacion);
                mostrarUltimoJugadorAgregado(actualizacion, 1);
                break;
            }
            case 8: {
                mostrarUltimoJugadorAgregado(actualizacion,2);
                break;
            }
            case 9: {
                mostrarCombinacionRequerida(((ifPartida) actualizacion).getRonda());
                mostrarPozo((ifPartida)actualizacion);
                break;
            }
        }

    }

    @Override
    public void setControlador(Controlador ctrl) {
        this.ctrl = ctrl;
    }
}