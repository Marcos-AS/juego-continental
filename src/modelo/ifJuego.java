package src.modelo;

import rmimvc.src.observer.IObservableRemoto;
import src.serializacion.Serializador;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ifJuego extends IObservableRemoto {

    int TRIO = 0;
    int ESCALERA = 1;
    int JUEGO_INVALIDO = 2;

    //PRIVATE-----------------------------------------------------
    static int comprobarEscalera(ArrayList<Carta> juego) throws RemoteException {
        int esEscalera = JUEGO_INVALIDO; //igual a false, lo pongo en numero para despues saber si es una escalera o un trio
        if (comprobarMismoPalo(juego)) {
            ArrayList<Carta> comodines = new ArrayList<>();
            int contadorEscalera = 1;
            ordenarCartas(juego);
            //si es un comodin lo almaceno, sino: si es un numero siguiente incremento
            //contador, sino: veo si puedo usar comodin
            for (int i = 0; i < juego.size(); i++) {
                Carta cartaActual = juego.get(i);
                int numCartaActual = cartaActual.getNumero();
                int numCartaSiguiente = juego.get(i + 1).getNumero();
                if (numCartaActual == Juego.COMODIN) {
                    comodines.add(cartaActual);
                    juego.remove(cartaActual);
                } else if (numCartaSiguiente == numCartaActual + 1) {
                    contadorEscalera++;
                } else {
                    if (!comodines.isEmpty()) {
                        if (numCartaSiguiente == numCartaActual + 2) {
                            contadorEscalera++;
                            comodines.remove(0);
                        }
                    } else {
                        contadorEscalera = 1;
                    }
                }
            }
            if (contadorEscalera >= 4)
                esEscalera = ESCALERA;
        }
        return esEscalera;
    }

    static boolean comprobarMismoPalo(ArrayList<Carta> cartas) throws RemoteException {
        boolean mismoPalo = false;
        for (int i = 0; i < cartas.size() - 1; i++) {
            Palo palo = cartas.get(i).getPalo();
            mismoPalo = (palo == cartas.get(i + 1).getPalo()) || palo == null;
        }
        return mismoPalo;
    }

    static void ordenarCartas(ArrayList<Carta> cartas) throws RemoteException { //metodo de insercion
        cartas.sort(null);
        boolean intercambio = true;
        while (intercambio) {
            intercambio = false;
            for (int i = 0; i < cartas.size() - 1; i++) {
                Carta cartaActual = cartas.get(i);
                if (cartaActual.getNumero() > cartas.get(i + 1).getNumero()) {
                    intercambio = true;
                    cartas.set(i, cartas.get(i + 1));
                    cartas.set(i + 1, cartaActual);
                }
            }
        }
    }

    static int comprobarTrio(ArrayList<Carta> juego) throws RemoteException {
        int formaTrio = 1;
        //igual a false, lo pongo en numero para despues saber si es una escalera o un trio
        int esTrio = JUEGO_INVALIDO;
        int i = 0;
        int numCarta = juego.get(i).getNumero();
        while (numCarta == Juego.COMODIN) {
            i++;
            formaTrio++;
            numCarta = juego.get(i).getNumero();
        }
        while (i < juego.size()) {
            int numCartaSig = juego.get(i).getNumero();
            if (numCarta == numCartaSig || numCartaSig == Juego.COMODIN) {
                formaTrio++;
            } else {
                formaTrio = 0;
            }
            i++;
        }
        if (formaTrio >= 3)
            esTrio = TRIO;
        return esTrio;
    }

    static int comprobarAcomodarEnTrio(ArrayList<Carta> juego) throws RemoteException {
        int resp = JUEGO_INVALIDO;
        Carta cartaAcomodar = juego.get(juego.size()-1);
        if (cartaAcomodar.getNumero() == Juego.COMODIN) {
            resp = TRIO;
        } else {
            boolean continuar = true;
            int i = 0;
            Carta c;
            while (continuar && i < juego.size()) {
                c = juego.get(i);
                if (c.getNumero() == Juego.COMODIN) {
                    i++;
                }
                else if (c.getNumero() != cartaAcomodar.getNumero()) {
                    continuar = false;
                } else {
                    i++;
                }
            }
            if (!continuar) resp = TRIO;
        }
        return resp;
    }

    static int comprobarAcomodarEnEscalera(ArrayList<Carta> juego) throws RemoteException {
        int resp = JUEGO_INVALIDO;
        if (comprobarMismoPalo(juego)) {
            Carta cartaAcomodar = juego.get(juego.size()-1);
            Carta ultimaCarta = juego.get(juego.size()-2);
            Carta primeraCarta = juego.get(0);
            if (cartaAcomodar.getNumero() == ultimaCarta.getNumero()+1 ||
            cartaAcomodar.getNumero() == primeraCarta.getNumero()-1) {
                resp = ESCALERA;
            }
        }
        return resp;
    }

    static int cartasPorRonda(int ronda) throws RemoteException {
        int cantCartas = Juego.CANT_CARTAS_INICIAL;
        switch (ronda) {
            case 2:
                cantCartas = 7;
                break;
            case 3:
                cantCartas = 8;
                break;
            case 4:
                cantCartas = 9;
                break;
            case 5:
                cantCartas = 10;
                break;
            case 6:
                cantCartas = 11;
                break;
            case 7:
                cantCartas = 12;
                break;
        }
        return cantCartas;
    }

    ;

    static int comprobarJuego(ArrayList<Carta> juego, int ronda) throws RemoteException {
        int esJuego = JUEGO_INVALIDO;
        switch (ronda) {
            case 1:
            case 4:
                esJuego = comprobarTrio(juego);
                break;
            case 2:
            case 5:
            case 6:
                if (comprobarTrio(juego) == TRIO) {
                    esJuego = TRIO;
                } else {
                    if (comprobarEscalera(juego) == ESCALERA) {
                        esJuego = ESCALERA;
                    }
                }
                break;
            case 3:
            case 7:
                esJuego = comprobarEscalera(juego);
                break;
        }
        return esJuego;
    }

    static boolean comprobarPosibleCorte(int ronda, int trios, int escaleras) throws RemoteException {
        boolean puedeCortar = false;
        switch (ronda) {
            case 1:
                puedeCortar = trios == 2;
                break;
            case 2:
                puedeCortar = trios == 1 && escaleras == 1;
                break;
            case 3:
                puedeCortar = escaleras == 2;
                break;
            case 4:
                puedeCortar = trios == 3;
                break;
            case 5:
                puedeCortar = trios == 2 && escaleras == 1;
                break;
            case 6:
                puedeCortar = trios == 1 && escaleras == 2;
                break;
            case 7:
                puedeCortar = escaleras == 3;
                break;
            default:
                break;
        }
        return puedeCortar;
    }

    void agregarJugador(String nombreJugador) throws RemoteException;

    void agregarJugadorAPartidaActual(String nombreJugador) throws RemoteException;

    void notificarHaRobadoConCastigo(int numJ) throws RemoteException;

    void guardarPartida() throws RemoteException;

    //GETTERS Y SETTERS
    Jugador getJugador(String nombreJugador) throws RemoteException;

    ArrayList<Jugador> getJugadores() throws RemoteException;

    static int getFigura() throws RemoteException {
        return Juego.FIGURA;
    }

    static int getAs() throws RemoteException {
        return Juego.AS;
    }

    static int getPuntosComodin() throws RemoteException {
        return Juego.PUNTOS_COMODIN;
    }

    Partida getPartidaActual() throws RemoteException;

    void setPartidaActual(Serializador srl) throws RemoteException;

    void setRoboConCastigo(int numJugador, boolean valor) throws RemoteException;

    void crearPartida(String nombreVista, int cantJugadores) throws RemoteException;

    void nuevaVentana() throws RemoteException;

    void getRanking() throws RemoteException;

    void notificarRondaFinalizada() throws RemoteException;

    void notificarPuntos() throws RemoteException;

    void iniciarCartasPartida() throws RemoteException;

    void setCorteRonda() throws RemoteException;

    boolean getCorteRonda() throws RemoteException;

    int getCantJugadoresPartida() throws RemoteException;

    void partidaFinRonda() throws RemoteException;

    void determinarGanador() throws RemoteException;

    boolean isPozoEmpty() throws RemoteException;

    void notificarDesarrolloTurno(int numJugador) throws RemoteException;
    void setTurno(int numJugador, boolean valor) throws RemoteException;
    boolean getTurno(int numJugador) throws RemoteException;
    boolean getRoboConCastigo(int numJugador) throws RemoteException;
    void resetearRoboConCastigo() throws RemoteException;
    void ordenarCartasEnMano(int numJugador, int[] cartas) throws RemoteException;
    void robarDelPozo(int numJugador) throws RemoteException;
    void robarDelMazo(int numJugador) throws RemoteException;
    void incrementarPuedeBajar(int numJugador) throws RemoteException;
    int getPuedeBajar(int numJugador) throws RemoteException;
    boolean isManoEmpty(int numJugador) throws RemoteException;
    void tirarAlPozo(int numJugador, int eleccionCarta) throws RemoteException;
    void bajarJuego(int numJugador, int[] cartasABajar, int tipoJuego) throws RemoteException;
    boolean acomodarCartaJuegoPropio(int iCarta, int numJugador, int numJuego, int ronda) throws RemoteException;
    boolean acomodarCartaJuegoAjeno(int iCarta, int numCarta,  Palo paloCarta, int numJugador, int numJugadorAcomodar, int numJuego, int ronda) throws RemoteException;
    void notificarRobo(int numJugador) throws RemoteException;
    void setRoboDelMazo(int numJugador, boolean valor) throws RemoteException;
    boolean getRoboDelMazo(int numJugador) throws RemoteException;
    int getNumJugadorRoboCastigo() throws RemoteException;
    void setNumJugadorRoboCastigo(int numJugador) throws RemoteException;
    void notificarCorteRonda(int numJugador) throws RemoteException;
}
