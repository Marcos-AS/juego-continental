package src.vista;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifPartida;
import src.modelo.ifJugador;

public class Consola implements ifVista{
    private Controlador ctrl;
    private final Scanner s = new Scanner(System.in);
    private String nombreVista;
    private String nombreJugador;
    private static final int NUEVA_PARTIDA = 6;
    private static final int DESARROLLO_TURNO = 8;
    private static final int POZO = 9;
    private static final int ROBO_CASTIGO = 11;
    private static final int HUBO_ROBO_CASTIGO = 12;
    private static final int SALIR_DEL_JUEGO = -1;
    private static final int YA_NO_PUEDE_BAJAR = 1;

    public Consola(){}

    public int preguntarCantParaBajar() {
        int numCartas;
		System.out.println("Cuantas cartas quiere bajar para el juego?");
        numCartas = s.nextInt();
        s.nextLine();
        while (numCartas > 4 || numCartas < 3) {
            System.out.println("La cantidad de cartas a bajar debe ser entre 3 y 4");
            numCartas = s.nextInt();
            s.nextLine();
        }
        return numCartas;
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

	public int[] preguntarQueBajarParaJuego(int cantCartas) {
        int[] cartasABajar = new int[cantCartas];
        for (int i = 0; i < cartasABajar.length; i++) {
            System.out.println("Carta " + (i + 1) + ": ");
            System.out.println("Indique el indice de la carta que quiere bajar: ");
            cartasABajar[i] = s.nextInt();
            System.out.println();
        }
        return cartasABajar;
	}

    public int[] preguntarParaOrdenarCartas() {
        int[] elecciones = new int[2];
        System.out.println("Elija el número de carta que quiere mover: ");
        elecciones[0] = s.nextInt();
        System.out.println("Elija el número de destino al que quiere mover la carta: ");
        elecciones[1] = s.nextInt();
        System.out.println();
        return elecciones;
    }

    public int preguntarQueBajarParaPozo(int cantCartas) {
        int eleccion;
        System.out.println("Indique el indice de carta para tirar al pozo: ");
        eleccion = s.nextInt();
        System.out.println();
        while (eleccion < 0 || eleccion >= cantCartas) {
            System.out.println("Vuelva a ingresar un indice de carta");
            eleccion = s.nextInt();
            System.out.println();
        }
        return eleccion;
    }

	public int preguntarCartaParaAcomodar() {
		System.out.println("Indique el numero de carta que quiere acomodar en un juego");
		int numCarta = s.nextInt();
        System.out.println();
		return numCarta;
	}

    public int preguntarEnQueJuegoQuiereAcomodar() {
        System.out.println("En que numero de juego quiere acomodar su carta?");
        int eleccion = s.nextInt();
        System.out.println();
        return eleccion;
    }

    @Override
    public String preguntarNombreNuevoJugador() {
        System.out.println("Indique su nombre:");
        String nombreJugador = s.nextLine();
        System.out.println("Jugador agregado.");
        return nombreJugador;
    }

    //MENUS-------------------------------
    public int menuRobar() {
		System.out.println("----------------------------------------");
		System.out.println("Quiere robar del pozo o robar del mazo?");
		System.out.println("1 - Robar del mazo");
		System.out.println("2 - Robar del pozo");
        System.out.println("-1 - Salir y guardar partida");
		System.out.println("Elija una opcion: ");
		int eleccion = s.nextInt();
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
        System.out.println("-1 - Salir y guardar partida");
        int eleccion = s.nextInt();
        System.out.println();
        return eleccion;
    }

    public int menuRobarDelPozo() {
		System.out.println("Quiere robar del pozo?");
        System.out.println("1 - No");
        System.out.println("2 - Si");
        System.out.println("-1 - Salir y guardar partida");
        int eleccion = s.nextInt();
        System.out.println();
        return eleccion;
    }

    @Override
    public void nuevaVentana() {}

    public int menuBienvenida() {
        System.out.println("Bienvenido al juego Continental.");
        System.out.println("Elija una opcion: ");
        System.out.println("1 - Crear partida");
        System.out.println("2 - Ver ranking mejores jugadores");
        System.out.println("3 - Ver reglas de juego");
        System.out.println("4 - Jugar partida recién creada");
        System.out.println(("5 - Cargar partida"));
        System.out.println("-1 - Salir del juego");
        int eleccion = s.nextInt();
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
        System.out.println("Se ha iniciado la partida.");
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
        if (i == YA_NO_PUEDE_BAJAR) {
            System.out.println("No puede volver a bajar juegos en esta ronda.");
        } else {
            System.out.println("No puede bajar porque la combinacion elegida no forma un juego valido para la ronda\n");
        }
    }

    public void mostrarJuego(int numJuego) {
        System.out.println("Juego N° " + numJuego+":\n");
    }

    public static void mostrarCombinacionRequerida(int ronda) {
        System.out.print("Para esta ronda deben bajarse: ");
        String s = switch (ronda) {
            case 1 -> "Ronda 1: 2 tríos";
            case 2 -> "Ronda 2: 1 trío y 1 escalera";
            case 3 -> "Ronda 3: 2 escaleras";
            case 4 -> "Ronda 4: 3 tríos";
            case 5 -> "Ronda 5: 2 tríos y 1 escalera";
            case 6 -> "Ronda 6: 1 tríos y 2 escaleras";
            case 7 -> "Ronda 7: 3 escaleras";
            default -> "";
        };
        System.out.println(s);
    }

    public void mostrarPuedeRobarConCastigo(String nombreJugador) {
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
        if (i == 0) {
            System.out.println("La partida aun no ha sido creada. Seleccione la opcion 1: 'Crear partida' ");
        } else if (i == 1) {
            System.out.println("No se puede iniciar la partida porque faltan jugadores para la cantidad deseada.");
        }
    }

    public void mostrarPuntosRonda(int[] puntos) throws RemoteException {
        System.out.println("Puntuacion: ");
        for (int i = 1; i < puntos.length; i++) {
            mostrarPuntosJugador(ctrl.getJugadorPartida(i-1).getNombre(), puntos[i]);
        }
    }

    public void mostrarFinalizoPartida() {
        System.out.println("La partida ha finalizado.");
    }

    public int preguntarCantJugadores() {
        System.out.println("Cuantos jugadores desea para la nueva partida?");
        int cantJugadores = s.nextInt();
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

    public boolean partida() throws RemoteException {
        boolean estadoPartida = true;
        while (ctrl.getRonda() <= ctrl.getTotalRondas()) {
            ctrl.iniciarCartasPartida();
            int i = 0;
            while (!ctrl.getCorteRonda()) {
                ctrl.notificarTurno(i);
                ctrl.notificarDesarrolloTurno();
                //modificar estadoPartida
                i++;
                if (i>ctrl.getCantJugadoresPartida()-1) {
                    i = 0;
                }
            }
            ctrl.notificarRondaFinalizada();
            ctrl.partidaFinRonda();
        }
        ctrl.determinarGanador();
        return estadoPartida;
    }

    private boolean preguntarSiQuiereRobarCastigo(ArrayList<String> mano) throws RemoteException {
        mostrarCartas(mano);
        if (menuRobarDelPozo() == SALIR_DEL_JUEGO) {
            ctrl.guardarPartida();
        }
        return menuRobarDelPozo() == ifVista.getEleccionRobarDelPozo();
    }

    //GETTERS Y SETTERS---------------------------

    public String getNombreVista() {
        return nombreVista;
    }

    public void setNombreVista(String i) {
        nombreVista = i;
    }

    //la invoca el metodo actualizar del controlador
    @Override
    public void actualizar(Object actualizacion, int indice) throws RemoteException {
        switch (indice) {//del 0 al 5 porque como maximo 6 jugadores
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: { //lo que se tiene que mostrar en ambas vistas
                mostrarPozo(ctrl.getPozo());
                mostrarCombinacionRequerida(ctrl.getRonda());
                ifJugador jA = (ifJugador) actualizacion;
                String nombreJugador = jA.getNombre();
                mostrarTurnoJugador(nombreJugador);
                if (nombreJugador.equals(nombreVista)) {
                    ctrl.setTurno(jA.getNumeroJugador());
                }
                break;
            }
            case NUEVA_PARTIDA: {
                mostrarInicioPartida();
                break;
            }
            case 7: {
                ArrayList<ifJugador> js = (ArrayList<ifJugador>) actualizacion;
                mostrarUltimoJugadorAgregado(js);
                break;
            }
            case DESARROLLO_TURNO: {
                ifJugador j = (ifJugador) actualizacion;
                if (j.getNombre().equals(nombreVista)) {
                    ctrl.desarrolloTurno(j); //aca se modifica la variable corte del while
                }
                break;
            }
            case 9: {
                mostrarCombinacionRequerida(((ifPartida) actualizacion).getRonda());
                mostrarPozo(((ifPartida) actualizacion).sacarPrimeraDelPozo());
                break;
            }
            case 10: {
                String s = (String) actualizacion;
                if (!ctrl.getEstadoPartida()) {
                    mostrarGanador(s);
                } else if (!s.equalsIgnoreCase(nombreVista)) {
                    System.out.println("El jugador " + s + " ha iniciado una partida nueva");
                }
                break;
            }
            case ROBO_CASTIGO: {
                int[] a = (int[]) actualizacion;
                int numJugador = a[0];
                int numJNoPuedeRobar = a[2];
                boolean robo = false;
                int contador = 0;
                ifJugador j = ctrl.getJugadorPartida(numJugador); //obtiene jugador que puede robar
                mostrarPuedeRobarConCastigo(j.getNombre());
                if (nombreVista.equals(j.getNombre())) {
                    //los que pueden robar con castigo son el total - 1
                    while (contador < ctrl.getCantJugadoresPartida()-1 && !robo) {
                        if (preguntarSiQuiereRobarCastigo(ctrl.enviarManoJugador(j))) {
                            robo = true;
                            ctrl.robarConCastigo(j);
                            ctrl.haRobadoConCastigo(j.getNumeroJugador());
                        } else {
                            contador++;
                            numJugador++;
                            if (numJugador == numJNoPuedeRobar) {
                                numJugador++;
                            }
                        }
                    }
                }
                break;
            }
            case HUBO_ROBO_CASTIGO: {
                String nombreJugador = ctrl.getJugadorPartida((int)actualizacion).getNombre();
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