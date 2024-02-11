package src.controlador;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rmimvc.src.observer.IObservableRemoto;
import src.modelo.*;
import rmimvc.src.cliente.IControladorRemoto;
import src.serializacion.Serializador;
import src.vista.ifVista;


public class Controlador implements IControladorRemoto {
    ifJuego juego;
    ifVista vista;

    public Controlador(ifVista vista) {
        this.vista = vista;
    }

    //PRIVATE----------------------------------------------------------------------
    private String transformarNumCarta(int numCarta) {
        String num = ((Integer) numCarta).toString();
        if (numCarta <= 1 || numCarta >= 11) {
            switch (num) {
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

    private ArrayList<String> cartasToStringArray(ArrayList<Carta> mano) {
        ArrayList<String> manoString = new ArrayList<>();
        for (Carta c : mano) {
            String numString = transformarNumCarta(c.getNumero());
            Palo palo = c.getPalo();
            String carta = "";
            if (palo == Palo.COMODIN) {
                carta = "COMODIN";
            } else {
                carta = numString + " de " + palo;
            }
            manoString.add(carta);
        }
        return manoString;
    }

    //PUBLIC---------------------------------------------------------------------

    //jugador---------------------------

    public ArrayList<String> enviarManoJugador(String nombreJugador) throws RemoteException {
        ArrayList<String> manoString = new ArrayList<>();
        try {
            //Jugador j = Juego.getJugador(nombreJugador);
            //Partida p = j.getPartidaActiva();
            //jugadorActual jA = p.getJugador(nombreJugador);
            //ArrayList<Carta> mano = jA.getMano();
            //manoString = cartasToStringArray(mano);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return manoString;
    }

    public ArrayList<ArrayList<String>> enviarJuegosJugador(Partida p, String nombreJugador) {
        jugadorActual j = p.getJugador(nombreJugador);
        ArrayList<ArrayList<Carta>> juegos = j.getJuegos();
        ArrayList<ArrayList<String>> juegosString = new ArrayList<>();
        for (ArrayList<Carta> juego : juegos) {
            juegosString.add(cartasToStringArray(juego));
        }
        return juegosString;
    }

    public void agregarNuevoJugador(String nombreJugador) throws RemoteException {
        this.juego.agregarJugador(new Jugador(nombreJugador));
    }

    public void agregarJugadores() throws RemoteException {
        synchronized (this.juego.getPartidaActual()) {
            int i = 1;
            for (Jugador j : this.juego.getJugadores()) { //por ahora agrega a todos los jugadores
                vista.mostrarJugador(j.getNombre(), i);
                i++;
                this.juego.getPartidaActual().agregarJugador(j.getNombre());
            }
            ArrayList<jugadorActual> js = this.juego.getPartidaActual().getJugadoresActuales();
            if(js.size() >= 2) { //esto me tira 0
                iniciarPartida();
            }
        }
    }
    //cartas------------------------------

    public ArrayList<String> enviarMazo(Partida p) {
        ArrayList<Carta> mazo = p.getMazo();
        return cartasToStringArray(mazo);
    }

    public boolean bajarJuego(Partida p, String nombreJugador, Object[] cartasABajar) throws RemoteException {
        jugadorActual j = p.getJugador(nombreJugador);
        boolean puedeBajar = false;
        if (cartasABajar.length >= 6) {
            if (j.bajarJuego(cartasABajar)) {
                puedeBajar = true;
                j.setPuedeBajar();
            }
        }
        return puedeBajar;
    }

    public String enviarPrimeraCartaPozo(Partida p) {
        Carta c = p.sacarPrimeraDelPozo();
        String carta = transformarNumCarta(c.getNumero()) + " de " + c.getPalo().name();
        return carta;
    }

    public int transformarLetraCarta(String letraCarta) {
        int numCarta = 0;
        switch (letraCarta) {
            case "J":
                numCarta = 11;
                break;
            case "Q":
                numCarta = 12;
                break;
            case "K":
                numCarta = 13;
                break;
            case "A":
                numCarta = 1;
                break;
            case "COMODIN":
                numCarta = -1;
                break;
        }
        return numCarta;
    }

    //partida------------------
    public void iniciarPartida() throws RemoteException {
        Thread gameThread = new Thread(() -> {
            vista.mostrarInicioPartida();
            try {
                Partida partidaActual = this.juego.getPartidaActual();
                partidaActual.crearMazo();
                partidaActual.repartirCartas();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        });
        gameThread.start();
        //ArrayList<jugadorActual> jugadoresActuales = partidaNueva.getJugadoresActuales();
        //ArrayList<String> mano;
        //int eleccion;
        //boolean corte = false;
        //empiezan las rondas
        /*while (partidaNueva.getRonda()<=partidaNueva.getTotalRondas()) {
            int i = 0;
            while (!corte) {
                try {
                    consola.mostrarCombinacionRequerida(partidaNueva.getRonda());
                    consola.mostrarPozo(partidaNueva);
                    jugadorActual j = jugadoresActuales.get(i);
                    consola.mostrarTurnoJugador(j.getNombre());
                    mano = consola.getCartasJugador(j.getNombre());
                    consola.mostrarCartas(mano);

                    eleccion = consola.menuRobar();

                    //si no roba del pozo, los demas pueden hacerlo, con "castigo"
                    if (eleccion != consola.getEleccionRobarDelPozo()) {
                        ArrayList<jugadorActual> jugadoresRoboCastigo = new ArrayList<>();
                        jugadoresRoboCastigo.addAll(jugadoresActuales);
                        jugadoresRoboCastigo.remove(i);
                        jugadorActual jugadorR = null;
                        int k;
                        int l = 0;
                        int eleccionR = eleccion;
                        while (eleccionR != consola.getEleccionRobarDelPozo() && l < jugadoresRoboCastigo.size()) {
                            k = i+1;
                            if (k > jugadoresRoboCastigo.size()-1) k = 0;
                            jugadorR = jugadoresRoboCastigo.get(k);
                            consola.jugadorPuedeRobarConCastigo(jugadorR.getNombre());
                            eleccionR = consola.menuRobarDelPozo();
                            l++;
                        }
                        if (eleccionR == consola.getEleccionRobarDelPozo()) {
                            jugadorActual jugador = partidaNueva.getJugador(jugadorR.getNombre());
                            jugador.robarConCastigo();
                            mano = consola.getCartasJugador(jugador.getNombre());
                            consola.mostrarCartas(mano);
                        }
                        consola.mostrarContinuaTurno(j.getNombre());
                    }
                    //si el pozo esta vacio, se roba del mazo. Si se eligio robar del mazo en un principio tambien sucede aca
                    if(!j.eleccionMenuRobo(eleccion)) {
                        consola.mostrarNoPuedeRobarDelPozo();
                        j.eleccionMenuRobo(consola.getEleccionRobarDelMazo());
                    }
                    mano = consola.getCartasJugador(j.getNombre());
                    consola.mostrarCartas(mano);

                    eleccion = consola.menuBajar();//-------------------------------

                    //ordenar cartas en la mano
                    while (eleccion == consola.getEleccionOrdenarCartas()) {
                        int[] ordenar = consola.preguntarParaOrdenarCartas();
                        j.eleccionOrdenar(ordenar);
                        mano = consola.getCartasJugador(j.getNombre());
                        consola.mostrarCartas(mano);
                        eleccion = consola.menuBajar();
                    }

                    //acomodar en un juego
                    while (eleccion == consola.getEleccionAcomodarJuegoPropio()) {
                        int iCarta = consola.preguntarCartaParaAcomodar();
                        ArrayList<ArrayList<String>> juegos = consola.getJuegosJugador(partidaNueva, j.getNombre());
                        if (!juegos.isEmpty()) {
                            int numJuego = 1;
                            for (ArrayList<String> juego : juegos) {
                                consola.mostrarJuego(numJuego);
                                consola.mostrarCartas(juego);
                                numJuego++;
                            }
                            int e = consola.preguntarEnQueJuegoQuiereAcomodar();
                            if(j.acomodarCartaJuegoPropio(iCarta, e, partidaNueva.getRonda())) {
                                juegos = consola.getJuegosJugador(partidaNueva,j.getNombre());
                                ArrayList<String> juego = juegos.get(e);
                                consola.mostrarCartas(juego);
                            }
                        } else {
                            consola.mostrarNoPuedeAcomodarJuegoPropio();
                        }
                        mano = consola.getCartasJugador(j.getNombre());
                        consola.mostrarCartas(mano);
                        eleccion = consola.menuBajar();
                    }

                    //bajarse
                    while (eleccion == consola.getEleccionBajarse()) {
                        Object [] cartasABajar = consola.preguntarQueBajarParaJuego();
                        consola.bajarse(partidaNueva, j.getNombre(), cartasABajar);
                        mano = consola.getCartasJugador(j.getNombre());
                        consola.mostrarCartas(mano);
                        eleccion = consola.menuBajar();
                    }


                    //si quiere cortar, comprobar si puede
                    if (eleccion == consola.getEleccionCortar()) {
                        corte = j.cortar(partidaNueva);
                        if (!corte) {
                            int[] triosYEscalerasQueFaltan = j.comprobarQueFaltaParaCortar();
                            consola.mostrarLoQueFaltaParaCortar(triosYEscalerasQueFaltan);
                        }
                    }
                    //tirar
                    if (!corte) {
                        mano = consola.getCartasJugador(j.getNombre());
                        consola.mostrarCartas(mano);
                        eleccion = consola.preguntarQueBajarParaPozo(mano.size());
                        j.tirarAlPozo(eleccion);
                    }
                    i++;
                    if (i>jugadoresActuales.size()-1) i = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//while ronda
            partidaNueva.resetearJuegosJugadores();
            partidaNueva.sumarPuntos();
            int[] puntos = partidaNueva.getPuntosJugadores();
            int m = 0;
            for (jugadorActual j : jugadoresActuales) {
                consola.mostrarPuntosJugador(j.getNombre(), puntos[m]);
                m++;
            }
        }//while partida
        String ganador = partidaNueva.determinarGanador();
        consola.mostrarGanador(ganador);*/
    }

    //GETTERS AND SETTERS-----------------------------------
    public Partida getPartidaActual() throws RemoteException {
        return this.juego.getPartidaActual();
    }

    public void setPartidaActual(Serializador srl) throws RemoteException {
        this.juego.setPartidaActual((Partida) srl.readFirstObject());
    }


    //OBSERVER-----------------------------------------------------
    public Object getValor(int accion) throws RemoteException {
        Object o = null;
        switch (accion) {
            case 1: { //nuevo jugador agregado
                o = this.juego.getJugadores();
                break;
            }
            case 2: { //jugador ingreso a partida
               o = this.juego.getPartidaActual().getJugadoresActuales();
               break;
            }
        }
        return o;
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.juego = (ifJuego) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object cambio) throws RemoteException {
        if (cambio instanceof Integer) {
            int indice = (Integer) cambio;
            vista.actualizar(getValor(indice), indice);
        }
    }
}