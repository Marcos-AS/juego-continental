package src.vista;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifJugador;

public class Consola implements ifVista{
    private Controlador ctrl;
    private final Scanner s = new Scanner(System.in);
    private String nombreVista;

    public Consola(){}

    public void bienvenida() throws RemoteException {
        ctrl.agregarNuevoJugador(preguntarNombreNuevoJugador()); //agrega jugador a juego y setea nombreVista
        int eleccion;
        int cantJugadores = 2; //minimo
        boolean partidaIniciada = false;
        do {
            eleccion = menuBienvenida();
            //1 - crear partida
            //2 - ver ranking mejores jugadores
            //3 - ver reglas de juego
            //4 - jugar partida recien creada
            //5 - cargar partida
            try {
                switch (eleccion) {
                    case 1: {
                        cantJugadores = preguntarCantJugadores();
                        ctrl.crearPartida(this, cantJugadores);
                        break;
                    }
                    case 2: {
                        ranking(ctrl);
                        break;
                    }
                    case 3: {
                        mostrarReglas();
                        break;
                    }
                    case 4: {
                        int inicioPartida = ctrl.jugarPartidaRecienIniciada();
                        if (inicioPartida == 0) {
                            noSePuedeIniciarPartida(0); // partida aun no creada
                        } else if (inicioPartida == 1) {
                            partidaIniciada = true;
                            noSePuedeIniciarPartida(1); //faltan jugadores para la cant deseada
                        } else if (inicioPartida == 2) {
                            partidaIniciada = true; //esto inicia el funcionamiento del juego
                            ctrl.notificarComienzoPartida();
                            if (!ctrl.partida()) {
                                partidaIniciada = false;
                                //partida finalizada
                            } //else {
                            //partida pausada, guardar
                            //}
                        }
                        break;
                    }
                    //                case 5: {
                    //                    Object[] partidas = srl.readObjects();
                    //                    for (Object o : partidas) {
                    //                        System.out.println(o.toString());
                    //                    }
                    //                }
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes ingresar un número entre 1 y 4");
            }
        } while (eleccion != -1 && !partidaIniciada);
    }

    private static void ranking(Controlador ctrl) throws RemoteException {
        System.out.println(ifVista.mostrarRanking(ctrl.getRanking()));
    }

    public void mostrarComienzaPartida(String[] jugadores) {
        System.out.println("\n*******************\nCOMIENZA LA PARTIDA\nJugadores:");
        int i = 1;
        for (String nombreJugador : jugadores) {
            System.out.println(i + "- " + nombreJugador);
            i++;
        }
        System.out.println("*******************\n");
    }

    public int preguntarCantParaBajar() {
        int numCartas;
		System.out.println("Cuantas cartas quieres bajar para el juego?");
        numCartas = s.nextInt();
        s.nextLine();
        while (numCartas > 4 || numCartas < 3) {
            System.out.println("La cantidad de cartas a bajar debe ser entre 3 y 4");
            numCartas = s.nextInt();
            s.nextLine();
        }
        return numCartas;
	}

    public void mostrarTurnoPropio() {
        System.out.println("************\nEs tu turno.");
    }

    public void mostrarJuegos(ArrayList<ArrayList<String>> juegos) {
        int numJuego = 1;
        if (juegos.isEmpty()) {
            System.out.println("No tiene juegos bajados.");
        } else {
            for (ArrayList<String> juego : juegos) {
                mostrarJuego(numJuego);
                mostrarCartas(juego);
                numJuego++;
            }
        }
    }

	public int[] preguntarQueBajarParaJuego(int cantCartas) {
        int[] cartasABajar = new int[cantCartas];
        for (int i = 0; i < cartasABajar.length; i++) {
            System.out.println("Carta " + (i + 1) + ": ");
            System.out.println("Indica el índice de la carta que quieres bajar: ");
            cartasABajar[i] = s.nextInt();
            System.out.println();
        }
        return cartasABajar;
	}

    public int[] preguntarParaOrdenarCartas(int cantCartas) {
        int[] elecciones = new int[2];
        int cartaSeleccion = -1;
        while (cartaSeleccion < 0 || cartaSeleccion > cantCartas-1) {
            System.out.println("Elije el número de carta que quieres mover: ");
            cartaSeleccion = s.nextInt();
        }
        elecciones[0] = cartaSeleccion;

        cartaSeleccion = -1;
        while (cartaSeleccion < 0 || cartaSeleccion > cantCartas-1) {
            System.out.println("Elije el número de destino al que quieres mover la carta: ");
            cartaSeleccion = s.nextInt();
        }
        elecciones[1] = cartaSeleccion;
        System.out.println();
        return elecciones;
    }

    public int preguntarQueBajarParaPozo(int cantCartas) {
        int eleccion;
        System.out.println("Indica el índice de carta para tirar al pozo: ");
        eleccion = s.nextInt();
        System.out.println();
        while (eleccion < 0 || eleccion >= cantCartas) {
            System.out.println("Vuelve a ingresar un índice de carta");
            eleccion = s.nextInt();
            System.out.println();
        }
        return eleccion;
    }

	public int preguntarCartaParaAcomodar() {
		System.out.println("Indica el número de carta que quieres acomodar en un juego");
		int numCarta = s.nextInt();
        System.out.println();
		return numCarta;
	}

    public int preguntarEnQueJuegoQuiereAcomodar() {
        System.out.println("En qué número de juego quieres acomodar tu carta?");
        int eleccion = s.nextInt();
        System.out.println();
        return eleccion;
    }

    public int preguntarEnLosJuegosDeQueJugadorAcomodar() {
        System.out.println("Ingresa el número de jugador en cuyo juegos bajados quieres acomodar: ");
        int eleccion = s.nextInt();
        System.out.println();
        return eleccion;
    }

    @Override
    public String preguntarNombreNuevoJugador() {
        System.out.println("Indica tu nombre:");
        String nombreJugador = s.nextLine();
        System.out.println("Jugador agregado.");
        return nombreJugador;
    }

    public void mostrarAdvertenciaBajarse() {
        System.out.println("Recuerda que sólo puedes bajar tus juegos dos veces durante la ronda,\n una en cualquier turno y otra si se procede a cortar.");
    }

    public void mostrarCortoPropio() {
        System.out.println("Has cortado. Felicitaciones!");
    }
    
    public boolean preguntarSiQuiereSeguirBajandoJuegos() {
        System.out.println("Deseas bajar un juego? (Si/No)");
        String resp = s.next();
        return resp.equalsIgnoreCase("Si") || resp.equalsIgnoreCase("S");
    }

    public void mostrarDebeCortar() {
        System.out.println("Debes tener los juegos requeridos para la ronda y cortar si deseas bajar ahora.");
    }

    public void mostrarDebeQuedarle1o0Cartas() {
        System.out.println("Para cortar debe quedarte en la mano 1 o 0 cartas");
    }

    //MENUS-------------------------------
    public int menuRobar() {
		System.out.println("Quieres robar del mazo o robar del pozo?");
		System.out.println("1 - Robar del mazo");
		System.out.println("2 - Robar del pozo");
		System.out.println("Elije una opción: ");
		int eleccion = s.nextInt();
        System.out.println();
        return eleccion;
	}

    public int menuBajar() {
        int eleccion = 0;
        while (eleccion < 1 || eleccion > 8) {
            System.out.println("Elije una opción: ");
            System.out.println("1 - Bajar uno o más juegos");
            System.out.println("2 - Tirar al pozo");
            System.out.println("3 - Ordenar cartas");
            System.out.println("4 - Acomodar en un juego bajado propio");
            System.out.println("5 - Ver juegos bajados propios");
            System.out.println("6 - Acomodar en un juego bajado ajeno");
            System.out.println("7 - Ver juegos bajados de todos los jugadores");
            System.out.println("8 - Ver pozo");
            eleccion = s.nextInt();
            System.out.println();
        }
        return eleccion;
    }

    @Override
    public void nuevaVentana() {}

    public int menuBienvenida() {
        System.out.println("\nBienvenido al juego Continental. <---------\n");
        System.out.println("Elije una opción: ");
        System.out.println("1 - Crear partida");
        System.out.println("2 - Ver ranking mejores jugadores");
        System.out.println("3 - Ver reglas de juego");
        System.out.println("4 - Jugar partida recién creada");
        System.out.println("-1 - Salir del juego");
        int eleccion = s.nextInt();
        System.out.println();
        return eleccion;
    }

    public int menuRobarConCastigo() {
        System.out.println("Quieres robar con castigo? (robar del pozo y robar del mazo)");
        System.out.println("1 - No");
        System.out.println("2 - Si");
        int eleccion = s.nextInt();
        System.out.println();
        return eleccion;
    }

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
        System.out.println("Se ha iniciado una partida.");
    }

    public void mostrarCartas(ArrayList<String> cartas) {
        int i = 0;
        System.out.println();
        for (String carta : cartas) {
            System.out.println(i + " - " + carta);
            i++;
        }
        System.out.println("----------");
    }

    public void mostrarTurnoJugador(String nombreJugador) {
        System.out.println("--------------------------");
        System.out.println("Es el turno del jugador: " + nombreJugador);
        System.out.println("--------------------------");
    }

    public void mostrarLoQueFaltaParaCortar(int[] faltaParaCortar) {
        System.out.println("Para cortar: ");
        System.out.println("Faltan " + faltaParaCortar[0] + " trios");
        System.out.println("Faltan " + faltaParaCortar[1] + " escaleras");
        System.out.println("-----------------------------");
    }

    public void mostrarNoPuedeCortar() {
        System.out.println("No puedes cortar");
    }

    public void mostrarCorto(String nombreJugador) {
        System.out.println("El jugador " + nombreJugador + " ha cortado.");
    }

    public void mostrarAcomodoCarta() {
        System.out.println("Se acomodó la carta en el juego.");
    }

    public void mostrarJuegosJugador(int numJugador) {
        System.out.println("Juegos del jugador " + numJugador + ": ");
    }

    public void mostrarCarta(String carta) {
        System.out.println(carta);
    }
    
    public void mostrarPozo(ifCarta c) {
        System.out.println("\nPozo: ");
        if (c != null) {
            mostrarCarta(ifVista.cartaToString(c));
        } else {
            System.out.println("Pozo vacío");
        }
    }

    public void mostrarNoPuedeBajarJuego(int i) {
        if (i == YA_NO_PUEDE_BAJAR) {
            System.out.println("No puedes volver a bajar juegos en esta ronda (tampoco robar con castigo).");
        } else {
            System.out.println("No puedes bajar porque la combinacion elegida no forma un juego valido para la ronda\n");
        }
    }

    public void mostrarJuego(int numJuego) {
        System.out.println("Juego N° " + numJuego+":");
    }

    public void mostrarCombinacionRequerida(int ronda) {
        System.out.print("******************************\nPara esta ronda deben bajarse: ");
        String s = switch (ronda) {
            case 1 -> "2 tríos";
            case 2 -> "1 trío y 1 escalera";
            case 3 -> "2 escaleras";
            case 4 -> "3 tríos";
            case 5 -> "2 tríos y 1 escalera";
            case 6 -> "1 tríos y 2 escaleras";
            case 7 -> "3 escaleras";
            default -> "";
        };
        System.out.println(s);
    }

    public void mostrarPuedeRobarConCastigo(String nombreJugador) {
        System.out.println("----------\nEl jugador " + nombreJugador + " puede robar con castigo.\n----------");
    }

    public void jugadorHaRobadoConCastigo(String nombreJugador) {
        System.out.println("El jugador "+ nombreJugador + " ha robado con castigo.");
    }

    public void mostrarNoPuedeRobarDelPozo() {
        System.out.println("No puedes robar del pozo porque no tiene cartas");
    }

	public void mostrarGanador(String nombre) {
		System.out.println("El jugador " + nombre + " es el ganador!");// con " + puntos + " puntos!");
	}

	public void mostrarPuntosJugador(String nombreJugador, int puntos) {
		System.out.println("Jugador " + nombreJugador + ": " + puntos);
	}

    public void mostrarNoPuedeAcomodarJuegoPropio() {
        System.out.println("No puede acomodar porque no tienes o no hay juegos bajados o porque la carta que deseas acomodar no hace juego con el juego elegido.");
    }

    public void mostrarUltimoJugadorAgregado(String nombreJugador) {
        System.out.println("El jugador " + nombreJugador + " ha ingresado.");
    }

    public void noSePuedeIniciarPartida(int i) {
        if (i == 0) {
            System.out.println("La partida aun no ha sido creada. Seleccione la opción 1: 'Crear partida' ");
        } else if (i == 1) {
            System.out.println("Esperando que ingresen más jugadores...");
        }
    }

    public void mostrarPuntosRonda(int[] puntos) throws RemoteException {
        System.out.println("Puntuación: ");
        for (int i = 0; i < puntos.length; i++) {
            mostrarPuntosJugador(ctrl.getJugadorPartida(i).getNombre(), puntos[i]);
        }
        System.out.println("--------------------------------\n");
    }

    public void mostrarFinalizoTurno(String nombreJugador) {
        System.out.println("Finalizó el turno del jugador " + nombreJugador);
    }

    public void mostrarFinalizoPartida() {
        System.out.println("La partida ha finalizado.");
    }

    public void mostrarComienzoRonda(int ronda) {
        System.out.println("-------------------|\nComienza la ronda " + ronda+"|\n-------------------|");
    }

    public void jugadorInicioPartida(String nombreJugador) {
        System.out.println("El jugador " + nombreJugador + " ha iniciado una partida nueva");
    }

    public int preguntarCantJugadores() {
        System.out.println("Cuántos jugadores deseas para la nueva partida?");
        int cantJugadores = s.nextInt();
        System.out.println();
        return cantJugadores;
    }

    public boolean preguntarSiQuiereRobarCastigo() {
        int eleccion = menuRobarConCastigo();
//        if (eleccion == SALIR_DEL_JUEGO) {
//            ctrl.guardarPartida();
//        }
        return eleccion == ifVista.ELECCION_ROBAR_DEL_POZO;
    }

    public void comienzoTurno(ifJugador jA) throws RemoteException {
        mostrarCombinacionRequerida(ctrl.getRonda());
        mostrarPozo(ctrl.getPozo());
        String nombreJugador = jA.getNombre();
        if (!nombreJugador.equals(nombreVista)) {
            mostrarTurnoJugador(nombreJugador);
        }
        else {
            mostrarTurnoPropio();
            ctrl.setTurno(jA.getNumeroJugador(), true);
        }
    }

    public void roboCastigo() throws RemoteException {
        int numJugadorRoboCastigo = ctrl.getNumJugadorRoboCastigo();
        if (!ctrl.getRoboConCastigo(numJugadorRoboCastigo)) {
            boolean roboConCastigo = false;
            ifJugador j = ctrl.getJugadorPartida(numJugadorRoboCastigo);
            mostrarPuedeRobarConCastigo(j.getNombre());
            if (ctrl.getPuedeBajar(numJugadorRoboCastigo)==0) {
                mostrarCartas(ctrl.enviarManoJugador(numJugadorRoboCastigo));
                if (preguntarSiQuiereRobarCastigo()) {
                    ctrl.robarConCastigo(numJugadorRoboCastigo);
                    ctrl.notificarHaRobadoConCastigo(numJugadorRoboCastigo);
                    ctrl.setRoboConCastigo(numJugadorRoboCastigo, true);
                    roboConCastigo = true;
                }
            }
            if (!roboConCastigo) {
                numJugadorRoboCastigo++;
                if (numJugadorRoboCastigo > ctrl.getCantJugadoresPartida() - 1)
                    numJugadorRoboCastigo = 0;
                ctrl.setNumJugadorRoboCastigo(numJugadorRoboCastigo);
            }
        }
    }

    @Override
    public void setControlador(Controlador ctrl) {
        this.ctrl = ctrl;
    }

    public String getNombreVista() {
        return nombreVista;
    }

    public void setNombreVista(String i) {
        nombreVista = i;
    }
}