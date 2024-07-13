package src.vista;

import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifJugador;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifVista {
    int ELECCION_BAJARSE = 1;
    int ELECCION_TIRAR_AL_POZO = 2;
    int ELECCION_ORDENAR_CARTAS = 3;
    int ELECCION_ACOMODAR_JUEGO_PROPIO = 4;
    int ELECCION_VER_JUEGOS_BAJADOS = 5;
    int ELECCION_ACOMODAR_JUEGO_AJENO = 6;
    int ELECCION_VER_JUEGOS_BAJADOS_MESA = 7;
    int ELECCION_VER_POZO = 8;
    int ELECCION_ROBAR_DEL_MAZO = 1;
    int ELECCION_ROBAR_DEL_POZO = 2;
    int NUEVA_PARTIDA = 6;
    int NUEVO_JUGADOR = 7;
    int DESARROLLO_TURNO = 8;
    int GANADOR = 10;
    int ROBO_CASTIGO = 11;
    int HUBO_ROBO_CASTIGO = 12;
    int RONDA_FINALIZADA = 14;
    int PUNTOS_RONDA = 15;
    int RANKING = 16;
    int JUGADOR_INICIO_PARTIDA = 17;
    int ROBO = 18;
    int COMIENZA_PARTIDA = 19;
    int COMIENZA_RONDA = 20;
    int CORTE_RONDA = 21;
    int NUEVA_VENTANA = 23;
    int YA_NO_PUEDE_BAJAR = 1;
    int PARTIDA_AUN_NO_CREADA = 0;
    int FALTAN_JUGADORES = 1;
    int INICIAR_PARTIDA = 2;
    int VENTANA_ANCHO = 500;
    int VENTANA_ALTO = 700;
    String REGLAS = "OBJETIVO\r\n" +
            "------------------------------------------------------------------------------------------------------------------------------------------------------------ \r\n" +
            "El objetivo del juego es formar las combinaciones requeridas en cada una de las 7 rondas, procurando acumular la menor cantidad posible de puntos. \r\n" +
            "Al final de todas las rondas, el jugador con menos puntos es el ganador.\r\n" +
            "\r\n" +

            "RONDAS\r\n" +
            "------------------------------------------------------------------------------------------------------------------------------------------------------------ \r\n" +
            "Se juegan 7 rondas, cada una con requisitos específicos:\r\n" +
            "- Ronda 1: Dos tríos\r\n" +
            "- Ronda 2: Un trío y una escalera\r\n" +
            "- Ronda 3: Dos escaleras\r\n" +
            "- Ronda 4: Tres tríos\r\n" +
            "- Ronda 5: Dos tríos y una escalera\r\n" +
            "- Ronda 6: Un trío y dos escaleras\r\n" +
            "- Ronda 7: Tres escaleras\r\n" + "\r\n" +

            "DEFINICIONES\r\n" +
            "------------------------------------------------------------------------------------------------------------------------------------------------------------ \r\n" +
            "Trío: 3 cartas con el mismo número, sin importar el palo.\r\n" +
            "Escalera: 4 o más cartas consecutivas del mismo palo. Puede comenzar con cualquier carta, y el as puede ser la carta intermedia entre la K y el 2. \r\n" +
            "Comodín: Se puede tener un trío de comodines, pero no se pueden colocar dos comodines JUNTOS en una escalera.\r\n" + "\r\n" +

            "ROBO\r\n" +
            "------------------------------------------------------------------------------------------------------------------------------------------------------------ \r\n" +
            "En cada turno, el jugador debe robar una carta y descartar otra. Si no roba del pozo, los siguientes jugadores pueden optar por robar del pozo, pero si lo hacen,\r\n " +
            "también deben robar otra del mazo (robo con \"castigo\"). Este proceso sigue en orden hacia la derecha. En caso de que ningún jugador desee robar del pozo,\r\n" +
            " cada jugador debe robar una carta en su turno, ya sea del mazo o del pozo.\r\n" +
            "\r\n" +

            "BAJAR JUEGOS Y CORTAR\r\n" +
            "------------------------------------------------------------------------------------------------------------------------------------------------------------ \r\n" +
            "Para cortar, el jugador debe tener completa la combinación requerida para la ronda.\r\n" +
            "Se puede cortar con la carta que sobra, o elegir no cortar si no hay cartas sobrantes. En estos casos, el jugador gana la ronda. \r\n" +
            "Además, el jugador puede bajar sus juegos una vez durante la ronda, con las siguientes restricciones:\r\n" +
            "- No puede robar con \"castigo\".\r\n" +
            "- No puede bajar de nuevo.\r\n" +
            "- Las cartas sobrantes se pueden colocar en los juegos bajados por otros jugadores.\r\n" +
            "\r\n" +

            "FIN DE LA RONDA\r\n" +
            "------------------------------------------------------------------------------------------------------------------------------------------------------------ \r\n" +
            "Al finalizar cada ronda, se suman los puntos de las cartas que los jugadores tienen en la mano. El ganador de la ronda no suma puntos. \r\n" +
            "Las cartas tienen valores específicos: los números valen su denominación, las figuras valen 10, el as vale 20 y el comodín 50.\r\n" +
            "\r\n" +

            "FIN DEL JUEGO\r\n" +
            "------------------------------------------------------------------------------------------------------------------------------------------------------------ \r\n" +
            "Después de todas las rondas, el jugador con menos puntos es declarado ganador.";


    static String transformarNumCarta(int numCarta) {
        String num = ((Integer) numCarta).toString();
        if (numCarta <= 1 || numCarta >= 11) {
            num = switch (num) {
                case "1" -> "A";
                case "11" -> "J";
                case "12" -> "Q";
                case "13" -> "K";
                case "-1" -> "COMODIN";
                default -> num;
            };
        }
        return num;
    }

    static ArrayList<String> cartasToStringArray(ArrayList<ifCarta> mano) {
        ArrayList<String> manoString = new ArrayList<>();
        for (ifCarta c : mano)
            manoString.add(cartaToString(c));
        return manoString;
    }

    static String cartaToString(ifCarta c) {
        String carta;
        if (c.getPalo().toString().equalsIgnoreCase("COMODIN")) {
            carta = "COMODIN";
        } else {
            String numString = transformarNumCarta(c.getNumero());
            carta = numString + " de " + c.getPalo().toString();
        }
        return carta;
    }

    static StringBuilder mostrarRanking(Object[] ranking) {
        StringBuilder s = new StringBuilder("Ranking de mejores jugadores: \n");
        int i = 1;
        for (Object o : ranking) {
            s.append(i).append(" - ").append(o).append("\n");
            i++;
        }
        return s;
    }

    void setControlador(Controlador ctrl);

    int menuBienvenida();

    void mostrarReglas();

    String preguntarNombreNuevoJugador() throws RemoteException;

    void mostrarInicioPartida();

    void mostrarCombinacionRequerida(int ronda);

    void mostrarPozo(ifCarta c);

    void mostrarTurnoJugador(String nombreJugador);

    void mostrarCartas(ArrayList<String> cartas);

    void mostrarPuntosJugador(String nombre, int punto);

    void mostrarGanador(String ganador);

    String getNombreVista();

    void setNombreVista(String i);

    void noSePuedeIniciarPartida(int i);
    void mostrarFinalizoPartida();
    int preguntarCantJugadores();
    void mostrarJuegos(ArrayList<ArrayList<String>> juegos);
    void mostrarNoPuedeBajarJuego(int i);
    void mostrarNoPuedeAcomodarJuegoPropio();
    void mostrarLoQueFaltaParaCortar(int[] faltaParaCortar);
    void mostrarAcomodoCarta();
    void mostrarJuegosJugador(int numJugador);
    int menuBajar();
    int preguntarQueBajarParaPozo(int cantCartas);
    void mostrarNoPuedeRobarDelPozo();
    int menuRobar();
    int preguntarCartaParaAcomodar();
    int preguntarEnQueJuegoQuiereAcomodar();
    int[] preguntarQueBajarParaJuego(int cantCartas);
    void mostrarPuedeRobarConCastigo(String nombreJugador);
    void mostrarAdvertenciaBajarse();
    void mostrarFinalizoTurno(String nombreJugador);
    void mostrarCorto(String nombreJugador);
    boolean preguntarSiQuiereSeguirBajandoJuegos();
    void nuevaVentana();
    int[] preguntarParaOrdenarCartas(int cantCartas);
    int preguntarCantParaBajar();
    int preguntarEnLosJuegosDeQueJugadorAcomodar();
    void mostrarDebeCortar();
    void mostrarDebeQuedarle1o0Cartas();
    void mostrarTurnoPropio();
    boolean preguntarSiQuiereRobarCastigo();
    void mostrarComienzaPartida(String[] jugadores);
    void mostrarComienzoRonda(int ronda);
    void mostrarCortoPropio();
    int menuRobarConCastigo();
    void bienvenida() throws RemoteException;
    void mostrarUltimoJugadorAgregado(String nombreJugador);
    void jugadorInicioPartida(String nombreJugador);
    void comienzoTurno(ifJugador jA) throws RemoteException;
    void roboCastigo() throws RemoteException;
    void jugadorHaRobadoConCastigo(String nombreJugador);
    void mostrarPuntosRonda(int[] puntos) throws RemoteException;
}